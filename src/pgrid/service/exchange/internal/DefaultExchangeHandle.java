/*
 * This file (pgrid.service.exchange.internal.DefaultExchangeHandle) is part of the libpgrid project.
 *
 * Copyright (c) 2011. Vourlakis Nikolas. All rights reserved.
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

import pgrid.service.spi.corba.CorbaRoutingTable;
import pgrid.service.spi.corba.ExchangeHandlePOA;

/**
 * @author Vourlakis Nikolas
 */
public class DefaultExchangeHandle extends ExchangeHandlePOA {

    @Override
    public CorbaRoutingTable routingTable() {
        // a remote peer wants the local to sends its routing table
        return null; // TODO: implement routingTable
    }

    @Override
    public void exchange(CorbaRoutingTable routingTable) {
        // a remote peer wants the local to execute the exchange algorithm
        // TODO: implement exchange
    }
}
