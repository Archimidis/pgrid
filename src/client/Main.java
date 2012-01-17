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
import com.google.inject.Key;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import pgrid.entity.EntityModule;
import pgrid.entity.Host;
import pgrid.process.ProcessModule;
import pgrid.process.SystemInitializationProcess;
import pgrid.service.*;
import pgrid.service.exchange.Exchange;
import pgrid.service.exchange.ExchangeService;
import pgrid.service.repair.Repair;
import pgrid.service.repair.RepairService;
import pgrid.service.simulation.PersistencyException;
import pgrid.service.simulation.spi.PersistencyDelegate;

import java.io.FileNotFoundException;
import java.net.UnknownHostException;

// pscp pgrid.jar nvourlakis@ui.grid.tuc.gr:/storage/tuclocal/nvourlakis/pgrid/
public class Main {

    private static final Logger logger_ = LoggerFactory.getLogger(Main.class);

    // arg[0] -> load routingTable.xlm
    // arg[1] -> store routingTable.xlm
    public static void main(String[] args) throws UnknownHostException {
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

//        RoutingTableFactory routingTableFactory = injector.getInstance(RoutingTableFactory.class);
//        EntityFactory entityFactory = injector.getInstance(EntityFactory.class);
//        RoutingTable rt = routingTableFactory.create(entityFactory.newHost("127.0.0.1", 3000));
//        CorbaFactory corbaFactory = injector.getInstance(CorbaFactory.class);
//        ORB orb = corbaFactory.getInstance("127.0.0.1", 3000);
//
        LocalPeerContext peerContext = injector.getInstance(LocalPeerContext.class);
//        peerContext.setRoutingTable(rt);
//        peerContext.setOrb(orb);

        try {
            initProcess.load(args[0]);
            ServiceRegistration[] registrations = {
                    injector.getInstance(Key.get(ServiceRegistration.class, Exchange.class)),
                    injector.getInstance(Key.get(ServiceRegistration.class, Repair.class))
            };
            initProcess.serviceRegistration(registrations);
        } catch (UnknownHostException e) {
            logger_.error("{}", e);
            System.exit(2);
        } catch (PersistencyException e) {
            logger_.error("{}", e);
            System.exit(2);
        } catch (FileNotFoundException e) {
            logger_.error("{}", e);
            System.exit(2);
        } catch (ServiceRegistrationException e) {
            logger_.error("{}", e);
            System.exit(2);
        }

        try {
            // wait 2 secs to make sure the other peers are initialized already
            Thread.sleep(2000);
        } catch (InterruptedException e) {
        }

        initProcess.start();

        LocalPeerContext context = peerContext;
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
        } catch (InterruptedException e) {
        }
        context.getCorba().shutdown(false);

        PersistencyDelegate bootstrapService =
                injector.getInstance(PersistencyDelegate.class);
        try {
            bootstrapService.store(args[1], context.getLocalRT());
        } catch (FileNotFoundException e) {
            logger_.error("An error occurred while storing the routing table.");
            System.exit(3);
        }

    }
}
