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

package demo;

import com.google.inject.Guice;
import com.google.inject.Injector;
import com.sun.corba.se.spi.logging.CORBALogDomains;
import org.junit.Test;
import org.omg.CORBA.ORB;
import org.omg.CORBA.ORBPackage.InvalidName;
import org.omg.PortableServer.POA;
import org.omg.PortableServer.POAHelper;
import org.omg.PortableServer.POAManagerPackage.AdapterInactive;
import org.omg.PortableServer.POAPackage.ServantAlreadyActive;
import org.omg.PortableServer.POAPackage.ServantNotActive;
import org.omg.PortableServer.POAPackage.WrongPolicy;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import pgrid.entity.CorbaFactory;
import pgrid.entity.EntityFactory;
import pgrid.entity.EntityModule;
import pgrid.entity.Host;
import pgrid.entity.routingtable.RoutingTable;
import pgrid.service.LocalPeerContext;
import pgrid.service.ServiceModule;
import pgrid.service.corba.storage.StorageHandleHelper;
import pgrid.service.corba.storage.StorageHandlePOA;
import pgrid.service.storage.StorageService;

import java.io.File;
import java.net.UnknownHostException;
import java.util.logging.Level;

/**
 * @author Nikolas Vourlakis <nvourlakis@gmail.com>
 */
public class StorageTest {
    private static final Logger logger_ = LoggerFactory.getLogger(StorageTest.class);

    private static Injector injector_;

    private static final String localIP_ = "127.0.0.1";
    private static final int localPort_ = 3000;
    private static final String LOCAL_PATH = "000";

    static {
        injector_ = Guice.createInjector(new EntityModule(), new ServiceModule(localIP_, localPort_, Integer.MAX_VALUE));
        try {
            localPeerContextInit();
            routingTableInit(injector_);
            serviceRegistration();
        } catch (UnknownHostException e) {
            logger_.error("Something went wrong with the initialization of CORBA.");
        }
    }

    @Test
    public void execute() throws UnknownHostException {

        logger_.info("[Storage test start]");
        StorageService storageService = injector_.getInstance(StorageService.class);
        storageService.store(new File("Going to california"));

        Host host = storageService.ownerOf("Going to california");
        logger_.info("Owner found => {}:{} [path: {}]",
                new Object[]{host.getAddress(), host.getPort(), host.getHostPath()});
    }

    private static void localPeerContextInit() throws UnknownHostException {
        LocalPeerContext context = injector_.getInstance(LocalPeerContext.class);
        CorbaFactory corbaFactory = injector_.getInstance(CorbaFactory.class);
        ORB orb = corbaFactory.getInstance(localIP_, localPort_);

        context.setOrb(orb);
    }

    private static void serviceRegistration() {
        final ORB orb = injector_.getInstance(LocalPeerContext.class).getCorba();

        try {
            POA rootPOA = null;
            try {
                rootPOA = POAHelper.narrow(orb.resolve_initial_references("RootPOA"));
            } catch (InvalidName invalidName) {
                invalidName.printStackTrace();
            }
            rootPOA.the_POAManager().activate();

            //********** Storage Service Registration  **********//
            StorageHandlePOA storageServant = injector_.getProvider(StorageHandlePOA.class).get();
            rootPOA.activate_object(storageServant);
            String[] ID = StorageHandleHelper.id().split(":");
            ((com.sun.corba.se.spi.orb.ORB) orb).register_initial_reference(
                    ID[1],
                    rootPOA.servant_to_reference(storageServant)
            );
            logger_.info("Storage service registered");
        } catch (ServantNotActive servantNotActive) {
            servantNotActive.printStackTrace();
        } catch (WrongPolicy wrongPolicy) {
            wrongPolicy.printStackTrace();
        } catch (InvalidName invalidName) {
            invalidName.printStackTrace();
        } catch (AdapterInactive adapterInactive) {
            adapterInactive.printStackTrace();
        } catch (ServantAlreadyActive servantAlreadyActive) {
            servantAlreadyActive.printStackTrace();
        }

        Thread orbThread_ = new Thread(new Runnable() {
            private final ORB orb_ = orb;

            @Override
            public void run() {
                orb_.run();
            }
        });
        orbThread_.start();
        logger_.info("Initialization finished!");

        // shutdown logging
        ((com.sun.corba.se.spi.orb.ORB) orb).getLogger(CORBALogDomains.RPC).setLevel(Level.OFF);
    }

    private static void routingTableInit(Injector injector) throws UnknownHostException {
        EntityFactory entityFactory = injector.getInstance(EntityFactory.class);
        Host localhost = entityFactory.newHost(localIP_, localPort_);
        localhost.setHostPath(LOCAL_PATH);

        RoutingTable routingTable = injector.getInstance(RoutingTable.class);
        routingTable.setLocalhost(localhost);

        Host A = entityFactory.newHost(localIP_, 1111);
        A.setHostPath("001");
        routingTable.addReference(0, A);

        Host B = entityFactory.newHost(localIP_, 2222);
        B.setHostPath("01");
        routingTable.addReference(0, B);

        Host C = entityFactory.newHost(localIP_, 3333);
        C.setHostPath("1");
        routingTable.addReference(0, C);
        routingTable.refresh(3);

        LocalPeerContext context = injector.getInstance(LocalPeerContext.class);
        CorbaFactory corbaFactory = injector.getInstance(CorbaFactory.class);
        ORB orb = corbaFactory.getInstance(localIP_, localPort_);

//        int index = 0;
//        for (Collection<Host> hosts : routingTable.getAllHostsByLevels()) {
//            for (Host host : hosts) {
//                logger_.debug("{}: {}:{} [path: {}]",
//                        new Object[]{
//                                index, host.getAddress(), host.getPort(), host.getHostPath()
//                        });
//            }
//            index++;
//        }

        context.setOrb(orb);
        context.setRoutingTable(routingTable);
//        logger_.info("Localhost instance: {}:{} [path: {}]",
//                new Object[]{
//                        context.getLocalRT().getLocalhost().getAddress(),
//                        context.getLocalRT().getLocalhost().getPort(),
//                        context.getLocalRT().getLocalhost().getHostPath()});
    }
}

/**
 * Storage file per paths:
 * [1] => "Al Di Meola"
 * [1] => "Led Zeppelin"
 * [1] => "Dogs"
 * [1] => "How to build a house"
 *
 * [01] => "Coast to Coast"
 * [01] => "1234"
 * [01] => "Arduino"
 * [01] => "Elegant"
 *
 * [001] => "Games"
 * [001] => "Knight"
 * [001] => "Games2"
 *
 * [000] => "Going to california"
 */
