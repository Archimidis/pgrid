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

package pgrid.service.exchange.internal;

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
import pgrid.service.corba.exchange.ExchangeHandleHelper;
import pgrid.service.corba.exchange.ExchangeHandlePOA;
import pgrid.service.exchange.Exchange;
import pgrid.service.exchange.spi.ExchangeHandleProvider;

import javax.inject.Inject;

/**
 * @author Nikolas Vourlakis <nvourlakis@gmail.com>
 */
@Exchange
public class ExchangeRegistration implements ServiceRegistration {

    private static final Logger logger_ = LoggerFactory.getLogger(ExchangeRegistration.class);

    private final ORB orb_;
    private final ExchangeHandleProvider provider_;

    @Inject
    public ExchangeRegistration(ExchangeHandleProvider provider, LocalPeerContext context) {
        orb_ = context.getCorba();
        provider_ = provider;
    }

    @Override
    public void register() throws ServiceRegistrationException {
        try {
            POA rootPOA = POAHelper.narrow(orb_.resolve_initial_references("RootPOA"));
            ExchangeHandlePOA exchangeServant = provider_.get();
            rootPOA.activate_object(exchangeServant);
            String[] ID = ExchangeHandleHelper.id().split(":");
            ((com.sun.corba.se.spi.orb.ORB) orb_).register_initial_reference(
                    ID[1],
                    rootPOA.servant_to_reference(exchangeServant)
            );
            logger_.info("Exchange service registered");
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
