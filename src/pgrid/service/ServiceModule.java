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

package pgrid.service;

import com.google.inject.AbstractModule;
import org.omg.CORBA.ORB;
import org.omg.CORBA.ORBPackage.InvalidName;
import org.omg.PortableServer.POA;
import org.omg.PortableServer.POAHelper;
import org.omg.PortableServer.POAManagerPackage.AdapterInactive;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import pgrid.entity.CorbaFactory;
import pgrid.entity.Host;
import pgrid.entity.internal.PGridHost;
import pgrid.entity.routingtable.RoutingTable;
import pgrid.service.anotations.constants.MaxRecursions;
import pgrid.service.anotations.constants.MaxRef;
import pgrid.service.anotations.constants.RepairTimeout;
import pgrid.service.exchange.ExchangeModule;
import pgrid.service.initialization.InitializationModule;
import pgrid.service.repair.RepairModule;
import pgrid.service.simulation.SimulationModule;

import java.net.UnknownHostException;

/**
 * @author Vourlakis Nikolas
 */
public class ServiceModule extends AbstractModule {

    private static final Logger logger_ = LoggerFactory.getLogger(ServiceModule.class);
    private final int maxRef_;
    private final String address_;
    private final int port_;

    public ServiceModule(String address, int port, int maxRef) {
        address_ = address;
        port_ = port;
        try {
            // hack to get the exception at initialization time
            Host host = new PGridHost(address_, port_);
        } catch (UnknownHostException e) {
            throw new IllegalArgumentException(e.getCause());
        }
        maxRef_ = maxRef;
    }


    protected void bindLocalPeerContext() throws UnknownHostException {
        Host localhost = new PGridHost(address_, port_);
        RoutingTable rt = new RoutingTable();
        rt.setLocalhost(localhost);

        CorbaFactory corbaFactory = new CorbaFactory();
        ORB orb = corbaFactory.getInstance(address_, port_);
        try {
            POA rootPOA = POAHelper.narrow(orb.resolve_initial_references("RootPOA"));
            rootPOA.the_POAManager().activate();
        } catch (InvalidName invalidName) {
            logger_.error("{}", invalidName);
        } catch (AdapterInactive adapterInactive) {
            logger_.error("{}", adapterInactive);
        }

        LocalPeerContext instance = new LocalPeerContext();
        instance.setOrb(orb);
        instance.setRoutingTable(rt);
        bind(LocalPeerContext.class).toInstance(instance);
    }

    @Override
    protected void configure() {
        logger_.debug("Setting up service module");

        binder().install(new ExchangeModule());
        binder().install(new RepairModule());
        binder().install(new SimulationModule());
        binder().install(new InitializationModule());

        try {
            bindLocalPeerContext();
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }
        //bind(LocalPeerContextFactory.class);

        bindConstant()
                .annotatedWith(MaxRef.class)
                .to(maxRef_);
        bindConstant()
                .annotatedWith(MaxRecursions.class)
                .to(2);
        bindConstant()
                .annotatedWith(RepairTimeout.class)
                .to(1000); // milliseconds
    }
}
