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
import pgrid.process.SimulationScenarioProcess;
import pgrid.process.initialization.SystemInitializationProcess;
import pgrid.service.LocalPeerContext;
import pgrid.service.ServiceModule;
import pgrid.service.ServiceRegistration;
import pgrid.service.exchange.Exchange;
import pgrid.service.repair.Repair;
import pgrid.service.simulation.Simulation;

import java.net.UnknownHostException;
import java.util.HashMap;
import java.util.Map;

/**
 * @author Vourlakis Nikolas <nvourlakis@gmail.com>
 */
public class Controller {

    private static final Logger logger_ = LoggerFactory.getLogger(GridPeer.class);
    private static Map<String, Host> network_ = new HashMap<String, Host>();

    // arg[0] -> file in xml where the routing table will be loaded from.
    // arg[1] -> file with all the hosts in the network.
    public static void main(String[] args) throws UnknownHostException {
//        if (args.length != 3) {
//            logger_.error("No file given to load and store routing table.");
//            System.exit(1);
//        }

        args = new String[]{"experiments/Topology1/A.xml"};
        Injector injector = Guice.createInjector(
                new EntityModule(),
                new ServiceModule(10),
                new ProcessModule());

        SystemInitializationProcess initProcess =
                injector.getInstance(SystemInitializationProcess.class);

        LocalPeerContext peerContext = injector.getInstance(LocalPeerContext.class);

        try {
            initProcess.load(args[0]);
            ServiceRegistration[] registrations = {
                    injector.getInstance(Key.get(ServiceRegistration.class, Exchange.class)),
                    injector.getInstance(Key.get(ServiceRegistration.class, Repair.class)),
                    injector.getInstance(Key.get(ServiceRegistration.class, Simulation.class))
            };
            initProcess.serviceRegistration(registrations);
        } catch (Exception e) {
            logger_.error("{}", e);
            System.exit(2);
        }

        try {
            // wait 2 secs to make sure the other peers are initialized
            Thread.sleep(2000);
        } catch (InterruptedException ignored) {
        }

        initProcess.startServer();


        LocalPeerContext context = peerContext;
        // select failed peer
//        Host failed = ;
//
//        logger_.info("I will kill peer {}:{} on path: {}",
//                new Object[]{
//                        failed.getAddress(),
//                        failed.getPort(),
//                        failed.getHostPath()});

        SimulationScenarioProcess simProcess = injector.getInstance(SimulationScenarioProcess.class);
//        simProcess.singleHostFailure();
    }
}
