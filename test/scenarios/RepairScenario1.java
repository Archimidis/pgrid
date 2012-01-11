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

package scenarios;

import com.google.inject.Guice;
import com.google.inject.Injector;
import com.sun.corba.se.spi.logging.CORBALogDomains;
import org.junit.Assert;
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
import pgrid.service.CommunicationException;
import pgrid.service.LocalPeerContext;
import pgrid.service.ServiceModule;
import pgrid.service.exchange.ExchangeService;
import pgrid.service.repair.RepairService;
import pgrid.service.spi.corba.exchange.ExchangeHandleHelper;
import pgrid.service.spi.corba.exchange.ExchangeHandlePOA;
import pgrid.service.spi.corba.repair.RepairHandleHelper;
import pgrid.service.spi.corba.repair.RepairHandlePOA;

import java.net.UnknownHostException;
import java.util.Collection;
import java.util.logging.Level;

/**
 * The first scenario is a sequence of a failed exchange followed by a repair.
 * The pgrid network contains only two peers in paths "0" and "1" respectively.
 * The peer on path "1" is considered dead and the peer on path "0" must
 * replace him. The result is the peer on path "0" to be responsible for the
 * whole key space, that is its new path will be "".
 *
 * @author Nikolas Vourlakis <nvourlakis@gmail.com>
 */
public class RepairScenario1 {
    private static final Logger logger_ = LoggerFactory.getLogger(RepairScenario1.class);
    private Thread orbThread_;

    private final int localPort_ = 3000;
    private final String localIP_ = "127.0.0.1";
    private final String localInitPath_ = "0";
    private final String expectedFinalPath_ = "";

    @Test
    public void test() throws UnknownHostException {
        logger_.info("[Example] Sequence of a failed exchange followed by repair.");
        Injector injector = Guice.createInjector(new EntityModule(), new ServiceModule());
        localPeerContextInit(injector);
        serviceRegistration(injector);

        LocalPeerContext context = injector.getInstance(LocalPeerContext.class);
        ExchangeService exchangeService = injector.getInstance(ExchangeService.class);

        Collection<Host> conjugateList = context.getLocalRT().getLevel(0);
        if (conjugateList.size() != 1) {
            logger_.error("Conjugate subtree is overpopulated.");
            System.exit(1);
        }
        Host conjugate = null;
        for (Host host : conjugateList) {
            conjugate = host;
        }

        logger_.info("I will communicate with conjugate {}:{} [path: {}]",
                new Object[]{
                        conjugate.getAddress(),
                        conjugate.getPort(),
                        conjugate.getHostPath()});

        try {
            exchangeService.execute(conjugate);
        } catch (CommunicationException e) {
            logger_.warn("{}", e.getMessage());
            RepairService repairService = injector.getInstance(RepairService.class);
            try {
                repairService.fixNode(conjugate);
            } catch (CommunicationException comExc) {
                comExc.printStackTrace();
            }
        }

        context.getCorba().shutdown(true);
        try {
            orbThread_.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        logger_.info("Localhost instance: {}:{} [path: {}]",
                new Object[]{
                        context.getLocalRT().getLocalhost().getAddress(),
                        context.getLocalRT().getLocalhost().getPort(),
                        context.getLocalRT().getLocalhost().getHostPath()});
        logger_.info("[Example] End.");

        Assert.assertTrue(context.getLocalRT().getLocalhost().getAddress().getHostAddress().compareTo(localIP_) == 0);
        Assert.assertTrue(context.getLocalRT().getLocalhost().getPort() == localPort_);
        Assert.assertTrue(context.getLocalRT().getLocalhost().getHostPath().toString().compareTo(expectedFinalPath_) == 0);
    }

    //*******************************************************************************************//
    private void localPeerContextInit(Injector injector) throws UnknownHostException {
        EntityFactory entityFactory = injector.getInstance(EntityFactory.class);
        Host localhost = entityFactory.newHost(localIP_, localPort_);
        localhost.setHostPath(localInitPath_);

        RoutingTable routingTable = injector.getInstance(RoutingTable.class);
        routingTable.setLocalhost(localhost);

        Host conjugate = entityFactory.newHost(localIP_, 1111);
        conjugate.setHostPath("1");

        routingTable.addReference(0, conjugate);

        LocalPeerContext context = injector.getInstance(LocalPeerContext.class);
        CorbaFactory corbaFactory = injector.getInstance(CorbaFactory.class);
        ORB orb = corbaFactory.getInstance(localIP_, localPort_);

        context.setOrb(orb);
        context.setRoutingTable(routingTable);
        logger_.info("Localhost instance: {}:{} [path: {}]",
                new Object[]{
                        context.getLocalRT().getLocalhost().getAddress(),
                        context.getLocalRT().getLocalhost().getPort(),
                        context.getLocalRT().getLocalhost().getHostPath()});
    }

    private void serviceRegistration(Injector injector) {
        final ORB orb = injector.getInstance(LocalPeerContext.class).getCorba();

        try {
            POA rootPOA = null;
            try {
                rootPOA = POAHelper.narrow(orb.resolve_initial_references("RootPOA"));
            } catch (InvalidName invalidName) {
                invalidName.printStackTrace();
            }
            rootPOA.the_POAManager().activate();

            //********* Exchange Service Registration  *********//
            ExchangeHandlePOA exchangeServant = injector.getProvider(ExchangeHandlePOA.class).get();
            rootPOA.activate_object(exchangeServant);
            String[] ID = ExchangeHandleHelper.id().split(":");
            ((com.sun.corba.se.spi.orb.ORB) orb).register_initial_reference(
                    ID[1],
                    rootPOA.servant_to_reference(exchangeServant)
            );
            logger_.info("Exchange service registered");

            //********** Repair Service Registration  **********//
            RepairHandlePOA repairServant = injector.getProvider(RepairHandlePOA.class).get();
            rootPOA.activate_object(repairServant);
            ID = RepairHandleHelper.id().split(":");
            ((com.sun.corba.se.spi.orb.ORB) orb).register_initial_reference(
                    ID[1],
                    rootPOA.servant_to_reference(repairServant)
            );
            logger_.info("Repair service registered");
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

        orbThread_ = new Thread(new Runnable() {
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
}
