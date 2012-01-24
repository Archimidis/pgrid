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

import org.omg.CORBA.ORB;
import org.omg.CORBA.ORBPackage.InvalidName;
import org.omg.PortableServer.POA;
import org.omg.PortableServer.POAHelper;
import org.omg.PortableServer.POAManagerPackage.AdapterInactive;
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

/**
 * Sample initialization process for setting up a peer in the tuc grid network.
 *
 * @author Vourlakis Nikolas <nvourlakis@gmail.com>
 */
public class DefaultInitializationProcess implements SystemInitializationProcess {

    static class OrbRunnable implements Runnable {
        private final ORB orb_;

        public OrbRunnable(ORB orb) {
            orb_ = orb;
        }

        @Override
        public void run() {
            orb_.run();
        }
    }

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
        try {
            POA rootPOA = POAHelper.narrow(orb.resolve_initial_references("RootPOA"));
            rootPOA.the_POAManager().activate();
        } catch (InvalidName ignored) {

        } catch (AdapterInactive ignored) {

        }
        System.out.println(orb);

        // shutdown logging
        //((com.sun.corba.se.spi.orb.ORB) orb).getLogger(CORBALogDomains.RPC).setLevel(Level.OFF);
//        Thread orbThread = new Thread(new OrbRunnable(orb));
//        orbThread.start();
        context_.setOrb(orb);
        logger_.info("[init] Localhost instance: {}:{} [path: {}]",
                new Object[]{
                        localhost.getAddress(),
                        localhost.getPort(),
                        localhost.getHostPath()});
    }

    public void serviceRegistration(ServiceRegistration... registrations) throws ServiceRegistrationException {
        System.out.println("process: " + context_.getCorba());
        for (ServiceRegistration registration : registrations) {
            registration.register();
        }
    }

    public void startServer() {
        final ORB orb = context_.getCorba();
        new Thread(new Runnable() {
            @Override
            public void run() {
                orb.run();
            }
        }).start();
    }

    @Override
    public void shutdownServer() {
        context_.getCorba().shutdown(false);
    }
}
