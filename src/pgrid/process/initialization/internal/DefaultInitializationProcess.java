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

package pgrid.process.initialization.internal;

import com.sun.corba.se.spi.logging.CORBALogDomains;
import org.omg.CORBA.ORB;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import pgrid.entity.CorbaFactory;
import pgrid.entity.Host;
import pgrid.entity.routingtable.RoutingTable;
import pgrid.process.initialization.SystemInitializationProcess;
import pgrid.service.LocalPeerContext;
import pgrid.service.ServiceRegistration;
import pgrid.service.ServiceRegistrationException;
import pgrid.service.simulation.PersistencyException;
import pgrid.service.simulation.internal.XMLPersistencyService;
import pgrid.service.simulation.spi.PersistencyDelegate;

import javax.inject.Inject;
import java.io.FileNotFoundException;
import java.net.UnknownHostException;
import java.util.logging.Level;

/**
 * Sample initialization process for setting up a peer in the tuc grid network.
 *
 * @author Vourlakis Nikolas <nvourlakis@gmail.com>
 */
public class DefaultInitializationProcess implements SystemInitializationProcess {
    private static final Logger logger_ = LoggerFactory.getLogger(DefaultInitializationProcess.class);
    private final LocalPeerContext context_;

    @Inject
    public DefaultInitializationProcess(LocalPeerContext context) {
        context_ = context;
    }

    public void load(String file) throws UnknownHostException, PersistencyException, FileNotFoundException {
        RoutingTable routingTable = new RoutingTable();
        PersistencyDelegate bootstrapService = new XMLPersistencyService();
        bootstrapService.load(file, routingTable);

        context_.setRoutingTable(routingTable);
        Host localhost = routingTable.getLocalhost();

        CorbaFactory corbaFactory = new CorbaFactory();
        ORB orb = corbaFactory.getInstance(
                localhost.getAddress().getHostName(), localhost.getPort());
        // shutdown logging
        ((com.sun.corba.se.spi.orb.ORB) orb).getLogger(CORBALogDomains.RPC).setLevel(Level.OFF);
        context_.setOrb(orb);
        logger_.info("[init] Localhost instance: {}:{} [path: {}]",
                new Object[]{
                        localhost.getAddress(),
                        localhost.getPort(),
                        localhost.getHostPath()});
    }

    public void serviceRegistration(ServiceRegistration... registrations) throws ServiceRegistrationException {
        for (ServiceRegistration registration : registrations) {
            registration.register();
        }
    }

    public void startServer() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                context_.getCorba().run();
            }
        }).start();
    }
}
