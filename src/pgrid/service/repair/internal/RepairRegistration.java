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

package pgrid.service.repair.internal;

import org.omg.CORBA.ORB;
import org.omg.CORBA.ORBPackage.InvalidName;
import org.omg.PortableServer.POA;
import org.omg.PortableServer.POAHelper;
import org.omg.PortableServer.POAPackage.ServantAlreadyActive;
import org.omg.PortableServer.POAPackage.ServantNotActive;
import org.omg.PortableServer.POAPackage.WrongPolicy;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import pgrid.service.LocalPeerContext;
import pgrid.service.ServiceRegistration;
import pgrid.service.ServiceRegistrationException;
import pgrid.service.corba.repair.RepairHandleHelper;
import pgrid.service.corba.repair.RepairHandlePOA;
import pgrid.service.repair.spi.RepairHandleProvider;
import pgrid.utilities.ArgumentCheck;

import javax.inject.Inject;

/**
 * @author Nikolas Vourlakis <nvourlakis@gmail.com>
 */
public class RepairRegistration implements ServiceRegistration {
    private static final Logger logger_ = LoggerFactory.getLogger(RepairRegistration.class);

    private final ORB orb_;
    private final RepairHandleProvider provider_;

    @Inject
    public RepairRegistration(RepairHandleProvider provider, LocalPeerContext context) {
        ArgumentCheck.checkNotNull(context, "Cannot initialize a RepairRegistration object with a null LocalPeerContext value.");
        ArgumentCheck.checkNotNull(context.getCorba(), "Uninitialized ORB in LocalPeerContext object passed to RepairRegistration.");
        ArgumentCheck.checkNotNull(context.getLocalRT(), "Uninitialized RoutingTable in LocalPeerContext object passed to RepairRegistration.");
        ArgumentCheck.checkNotNull(provider, "Cannot initialize a RepairRegistration object with a null RepairHandleProvider value.");

        orb_ = context.getCorba();
        provider_ = provider;
    }

    @Override
    public void register() throws ServiceRegistrationException {
        try {
            POA rootPOA = POAHelper.narrow(orb_.resolve_initial_references("RootPOA"));
            RepairHandlePOA repairServant = provider_.get();
            rootPOA.activate_object(repairServant);
            String[] ID = RepairHandleHelper.id().split(":");
            ((com.sun.corba.se.spi.orb.ORB) orb_).register_initial_reference(
                    ID[1],
                    rootPOA.servant_to_reference(repairServant)
            );
            logger_.info("Repair service registered");
        } catch (InvalidName invalidName) {
            throw new ServiceRegistrationException(invalidName);
        } catch (WrongPolicy wrongPolicy) {
            throw new ServiceRegistrationException(wrongPolicy);
        } catch (ServantNotActive servantNotActive) {
            throw new ServiceRegistrationException(servantNotActive);
        } catch (ServantAlreadyActive servantAlreadyActive) {
            throw new ServiceRegistrationException(servantAlreadyActive);
        }
    }
}
