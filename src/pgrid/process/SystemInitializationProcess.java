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

package pgrid.process;

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
import pgrid.entity.Host;
import pgrid.entity.routingtable.RoutingTable;
import pgrid.service.LocalPeerContext;
import pgrid.service.bootstrap.FileBootstrapService;
import pgrid.service.bootstrap.PersistencyException;
import pgrid.service.bootstrap.internal.XMLBootstrapService;
import pgrid.service.spi.corba.exchange.ExchangeHandleHelper;
import pgrid.service.spi.corba.exchange.ExchangeHandlePOA;
import pgrid.service.spi.corba.repair.RepairHandleHelper;
import pgrid.service.spi.corba.repair.RepairHandlePOA;

import javax.inject.Inject;
import java.io.FileNotFoundException;
import java.net.UnknownHostException;
import java.util.logging.Level;

/**
 * Sample initialization process for setting up a peer in the tuc grid network.
 *
 * @author Vourlakis Nikolas <nvourlakis@gmail.com>
 */
public class SystemInitializationProcess {
    private static final Logger logger_ = LoggerFactory.getLogger(SystemInitializationProcess.class);
    private final LocalPeerContext context_;

    @Inject
    public SystemInitializationProcess(LocalPeerContext context) {
        context_ = context;
    }

    public void load(String file) throws UnknownHostException, PersistencyException, FileNotFoundException {
        RoutingTable routingTable = new RoutingTable();
        FileBootstrapService bootstrapService = new XMLBootstrapService();
        bootstrapService.load(file, routingTable);
        
        context_.setRoutingTable(routingTable);
        Host localhost = routingTable.getLocalhost();
        logger_.info("[init] Localhost instance: {}:{} [path: {}]",
                new Object[]{
                        localhost.getAddress(),
                        localhost.getPort(),
                        localhost.getHostPath()});

        CorbaFactory corbaFactory = new CorbaFactory();
        context_.setOrb(corbaFactory.getInstance(
                localhost.getAddress().getHostName(), localhost.getPort()));
    }
    
    public void serviceRegistration(Injector injector) {
        // TODO: spi interface for each service for registering
        ORB orb = context_.getCorba();

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

        // shutdown logging
        ((com.sun.corba.se.spi.orb.ORB) orb).getLogger(CORBALogDomains.RPC).setLevel(Level.OFF);
    }

    public void start() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                context_.getCorba().run();
            }
        }).start();
    }
}
