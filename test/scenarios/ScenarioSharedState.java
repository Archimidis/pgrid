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
import pgrid.entity.EntityModule;
import pgrid.service.LocalPeerContext;
import pgrid.service.ServiceModule;
import pgrid.service.corba.exchange.ExchangeHandleHelper;
import pgrid.service.corba.exchange.ExchangeHandlePOA;
import pgrid.service.corba.repair.RepairHandleHelper;
import pgrid.service.corba.repair.RepairHandlePOA;

import java.net.UnknownHostException;
import java.util.logging.Level;

/**
 * @author Vourlakis Nikolas <nvourlakis@gmail.com>
 */
public class ScenarioSharedState {
    private static final Logger logger_ = LoggerFactory.getLogger(ScenarioSharedState.class);
    private static final Injector injector_;

    private static final String localIP_ = "127.0.0.1";
    private static final int localPort_ = 3000;

    static {
        injector_ = Guice.createInjector(new EntityModule(), new ServiceModule(localIP_, localPort_, Integer.MAX_VALUE));
        try {
            localPeerContextInit();
            serviceRegistration();
        } catch (UnknownHostException e) {
            logger_.error("Something went wrong with the initialization of CORBA.");
        }

    }

    public static Injector getInjector() {
        return injector_;
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

            //********* Exchange Service Registration  *********//
            ExchangeHandlePOA exchangeServant = injector_.getProvider(ExchangeHandlePOA.class).get();
            rootPOA.activate_object(exchangeServant);
            String[] ID = ExchangeHandleHelper.id().split(":");
            ((com.sun.corba.se.spi.orb.ORB) orb).register_initial_reference(
                    ID[1],
                    rootPOA.servant_to_reference(exchangeServant)
            );
            logger_.info("Exchange service registered");

            //********** Repair Service Registration  **********//
            RepairHandlePOA repairServant = injector_.getProvider(RepairHandlePOA.class).get();
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
}
