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

package pgrid.service.exchange.spi;

import pgrid.service.LocalPeerContext;
import pgrid.service.anotations.constants.MaxRecursions;
import pgrid.service.anotations.constants.MaxRef;
import pgrid.service.corba.exchange.ExchangeHandlePOA;
import pgrid.service.exchange.internal.DefaultExchangeHandle;
import pgrid.utilities.ArgumentCheck;

import javax.inject.Inject;
import javax.inject.Provider;

/**
 * The provider for the ExchangeHandlePOA used by the CORBA facility. It
 * returns the same object. Probably the provider will be asked for an object
 * only one time and that is during the initialization of CORBA layer, so it
 * can register the service.
 *
 * @author Vourlakis Nikolas
 */
public class ExchangeHandleProvider implements Provider<ExchangeHandlePOA> {
    private final ExchangeHandlePOA poa_;

    /**
     * Constructor.
     *
     * @param context contains all the information about the local peer.
     * @param algo    an implementation of the exchange algorithm.
     */
    @Inject
    public ExchangeHandleProvider(LocalPeerContext context, ExchangeAlgorithm algo, @MaxRef int maxRef, @MaxRecursions int maxRecur) {
        ArgumentCheck.checkNotNull(context, "Cannot initialize a ExchangeHandleProvider object with a null LocalPeerContext value.");
        ArgumentCheck.checkNotNull(context.getCorba(), "Uninitialized ORB in LocalPeerContext object passed to ExchangeHandleProvider.");
        ArgumentCheck.checkNotNull(context.getLocalRT(), "Uninitialized RoutingTable in LocalPeerContext object passed to ExchangeHandleProvider.");
        ArgumentCheck.checkNotNull(algo, "Cannot initialize a ExchangeHandleProvider object with a null ExchangeAlgorithm value.");

        poa_ = new DefaultExchangeHandle(context.getLocalRT(), algo, maxRef, maxRecur);
    }

    /**
     * Returns the same instance.
     *
     * @return the created instance of ExchangeHandlePOA type.
     */
    @Override
    public ExchangeHandlePOA get() {
        return poa_;
    }
}
