/*
 * This file (pgrid.service.exchange.internal.DefaultExchangeService) is part of the libpgrid project.
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

import org.omg.CORBA.ORB;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import pgrid.entity.Host;
import pgrid.entity.routingtable.RoutingTable;
import pgrid.service.exchange.ExchangeService;
import pgrid.service.exchange.spi.ExchangeAlgorithm;
import pgrid.service.exchange.spi.ExchangeContext;
import pgrid.service.spi.corba.ExchangeHandle;
import pgrid.service.spi.corba.ExchangeHandleHelper;
import pgrid.service.utilities.Deserializer;
import pgrid.service.utilities.Serializer;

/**
 * @author Vourlakis Nikolas
 */
public class DefaultExchangeService implements ExchangeService {
    private static final Logger logger_ = LoggerFactory.getLogger(DefaultExchangeService.class);
    private final RoutingTable routingTable_;
    private final ORB orb_;
    private ExchangeAlgorithm algorithm_;

    public DefaultExchangeService(ORB orb, RoutingTable routingTable, ExchangeAlgorithm algorithm) {
        orb_ = orb;
        routingTable_ = routingTable;
        algorithm_ = algorithm;
    }

    @Override
    public void execute(Host host) {
        logger_.info("Executing exchange service.");

        String[] exchangeHandleID = ExchangeHandleHelper.id().split(":");
        String corbaloc = "corbaloc:iiop:[" +
                host.getAddress() + "]:" +host.getPort()
                + "/" + exchangeHandleID[1];
        logger_.debug("CORBALOC: {}", corbaloc);
        org.omg.CORBA.Object object = orb_.string_to_object(corbaloc);
        ExchangeHandle handle = ExchangeHandleHelper.narrow(object);

        // send local routing table
        handle.exchange(Serializer.serializeRoutingTable(routingTable_));
        // receive remote routing table
        RoutingTable remoteRT = Deserializer.deserializeRoutingTable(handle.routingTable());

        ExchangeContext context = new ExchangeContext(routingTable_, false);
        context.setRemoteInfo(remoteRT);

        logger_.debug("Local peer has all the information needed to execute the exchange algorithm.");
        algorithm_.execute(context);
    }
}
