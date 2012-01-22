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
import pgrid.process.ProcessModule;
import pgrid.process.initialization.SystemInitializationProcess;
import pgrid.service.ServiceModule;
import pgrid.service.ServiceRegistration;
import pgrid.service.exchange.Exchange;
import pgrid.service.repair.Repair;
import pgrid.service.simulation.Simulation;

import java.net.UnknownHostException;

/**
 * A peer that initializes the services needed for simulation and then it waits
 * patiently for orders by the controller of the experiments.
 * This code will be run on each node of the grid system of TUC.
 * The file where the routing table is stored for this peer must be given in
 * order to initialize properly and take a place to the pgrid topology.
 *
 * @author Vourlakis Nikolas <nvourlakis@gmail.com>
 */
public class GridPeer {

    private static final Logger logger_ = LoggerFactory.getLogger(GridPeer.class);

    // arg[0] -> xml file where the routing table will be loaded from.
    public static void main(String[] args) throws UnknownHostException {
        if (args.length != 1) {
            logger_.error("No file given to load and store routing table.");
            System.exit(1);
        }

        Injector injector = Guice.createInjector(
                new EntityModule(),
                new ServiceModule(10),
                new ProcessModule());

        SystemInitializationProcess initProcess =
                injector.getInstance(SystemInitializationProcess.class);

        try {
            initProcess.load(args[0]);
            ServiceRegistration[] registrations = {
                    injector.getInstance(Key.get(ServiceRegistration.class, Exchange.class)),
                    injector.getInstance(Key.get(ServiceRegistration.class, Repair.class)),
                    injector.getInstance(Key.get(ServiceRegistration.class, Simulation.class))
            };
            initProcess.serviceRegistration(registrations);
        } catch (Exception e) {
            logger_.error("Error during service registration. {}", e);
            System.exit(2);
        }

        initProcess.startServer();
        // Waiting orders from the controller!
    }
}
