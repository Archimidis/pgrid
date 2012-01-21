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

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import pgrid.entity.routingtable.RoutingTable;
import pgrid.service.corba.CorbaRoutingTable;
import pgrid.service.corba.exchange.ExchangeHandlePOA;
import pgrid.service.exchange.spi.ExchangeAlgorithm;
import pgrid.service.exchange.spi.ExchangeContext;
import pgrid.service.utilities.Deserializer;
import pgrid.service.utilities.Serializer;

/**
 * @author Vourlakis Nikolas
 */
public class DefaultExchangeHandle extends ExchangeHandlePOA {

    private static final Logger logger_ = LoggerFactory.getLogger(DefaultExchangeHandle.class);

    private final RoutingTable localRoutingTable_;
    private ExchangeAlgorithm algo_;

    private final int REF_MAX;
    private final int MAX_RECURSIONS;

    public DefaultExchangeHandle(RoutingTable localRoutingTable, ExchangeAlgorithm algo, int maxRef, int maxRecur) {
        localRoutingTable_ = localRoutingTable;
        algo_ = algo;
        REF_MAX = maxRef;
        MAX_RECURSIONS = maxRecur;
    }

    @Override
    public CorbaRoutingTable routingTable() {
        // a remote peer wants the local to send its routing table
        return Serializer.serializeRoutingTable(localRoutingTable_);
    }

    @Override
    public void exchange(CorbaRoutingTable routingTable) {
        if (routingTable == null) {
            logger_.warn("Received an exchange request but was provided with a null CorbaRoutingTable object.");
            return;
        }
        // a remote peer wants the local to execute the exchange algorithm
        RoutingTable remoteRT = Deserializer.deserializeRoutingTable(routingTable);
        ExchangeContext context = new ExchangeContext(localRoutingTable_, true, REF_MAX);
        context.setRemoteInfo(remoteRT);
        algo_.execute(context);
        // TODO: Implement the reaction to the recursion case of the exchange algorithm.
    }
}
