/*
 * This file is part of the pgrid project.
 *
 * Copyright (c) 2012. Vourlakis Nikolas. All rights reserved.
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package client;

import com.google.inject.Guice;
import com.google.inject.Injector;
import java.io.FileNotFoundException;
import java.net.UnknownHostException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import pgrid.entity.EntityModule;
import pgrid.entity.Host;
import pgrid.process.ProcessModule;
import pgrid.process.SystemInitializationProcess;
import pgrid.service.CommunicationException;
import pgrid.service.LocalPeerContext;
import pgrid.service.ServiceModule;
import pgrid.service.bootstrap.FileBootstrapService;
import pgrid.service.bootstrap.PersistencyException;
import pgrid.service.exchange.ExchangeService;
import pgrid.service.repair.RepairService;

public class Main {

    private static final Logger logger_ = LoggerFactory.getLogger(Main.class);

    // arg[0] -> load routingTable.xlm
    // arg[1] -> store routingTable.xlm
    public static void main(String[] args) {
        if (args.length != 2) {
            logger_.error("No file given to load and store routing table.");
            System.exit(1);
        }

        Injector injector = Guice.createInjector(
                new EntityModule(),
                new ServiceModule(),
                new ProcessModule());

        SystemInitializationProcess initProcess =
                injector.getInstance(SystemInitializationProcess.class);
        try {
            initProcess.load(args[0]);
            initProcess.serviceRegistration(injector);
        } catch (UnknownHostException e) {
            logger_.error("{}", e);
            System.exit(2);
        } catch (PersistencyException e) {
            logger_.error("{}", e);
            System.exit(2);
        } catch (FileNotFoundException e) {
            logger_.error("{}", e);
            System.exit(2);
        }

        try {
            // wait 2 secs to make sure the other peers are initialized already
            Thread.sleep(2000);
        } catch (InterruptedException e) {}

        initProcess.start();

        LocalPeerContext context = injector.getInstance(LocalPeerContext.class);
        Host failed = context.getLocalRT().getLevelArray(0)[0];

        logger_.info("I will communicate with peer {}:{} [path: {}]",
                new Object[]{
                        failed.getAddress(),
                        failed.getPort(),
                        failed.getHostPath()});

        // exchange with failed peer
        ExchangeService exchangeService =
                injector.getProvider(ExchangeService.class).get();
        try {
            exchangeService.execute(failed);
        } catch (CommunicationException e) {
            // repair failed peer
            logger_.warn("{}", e.getMessage());
            RepairService repairService = injector.getProvider(RepairService.class).get();
            repairService.fixNode(failed);
        }

        try {
            // wait just in case ...
            Thread.sleep(500);
        } catch (InterruptedException e) {}
        context.getCorba().shutdown(true);

        FileBootstrapService bootstrapService =
                injector.getInstance(FileBootstrapService.class);
        try {
            bootstrapService.store(args[1], context.getLocalRT());
        } catch (FileNotFoundException e) {
            logger_.error("An error occurred while storing the routing table.");
            System.exit(3);
        }

    }
}
