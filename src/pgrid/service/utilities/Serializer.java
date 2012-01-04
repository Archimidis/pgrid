/*
 * This file (pgrid.service.utilities.Serializer) is part of the libpgrid project.
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

package pgrid.service.utilities;

import pgrid.entity.Host;
import pgrid.entity.routingtable.RoutingTable;
import pgrid.service.spi.corba.CorbaRoutingTable;
import pgrid.service.spi.corba.PeerReference;

import java.util.Collection;

/**
 * @author Vourlakis Nikolas
 */
public class Serializer {

    /**
     * Serializes a {@link pgrid.entity.Host} object.
     *
     * @param host the host to be serialized.
     * @return the serialized result.
     */
    public static PeerReference serializeHost(Host host) {
        if (host == null) {
            throw new NullPointerException("Cannot transform a null value to a CORBA object");
        }

        return new PeerReference(
                host.getAddress().getHostAddress(),
                host.getPort(),
                host.getHostPath().toString(),
                host.getTimestamp(),
                host.getUUID().toString()
        );
    }

    /**
     * Serializes a {@link RoutingTable} object.
     *
     * @param routingTable the routing table to be serialized.
     * @return the serialized result.
     */
    public static CorbaRoutingTable serializeRoutingTable(RoutingTable routingTable) {
        if (routingTable == null) {
            throw new NullPointerException("Cannot transform a null value to a CORBA object");
        }
        if (routingTable.getLocalhost() == null) {
            throw new IllegalStateException("RoutingTable is not in a legal state. Its host owner reference is null.");
        }
        if (routingTable.levelNumber() == 0) {
            return new CorbaRoutingTable(
                    new PeerReference[0][0],
                    serializeHost(routingTable.getLocalhost()));
        }

        Collection<Collection<Host>> refs = routingTable.getAllHostsByLevels();
        PeerReference[][] peerRefs = new PeerReference[refs.size()][];

        int levelIndex = 0;
        for (Collection<Host> level : refs) {
            PeerReference[] levelRefs = new PeerReference[level.size()];
            int hostIndex = 0;
            for (Host host : level) {
                levelRefs[hostIndex] = serializeHost(host);
                hostIndex++;
            }
            peerRefs[levelIndex] = levelRefs;
            levelIndex++;
        }

        PeerReference localhost = serializeHost(routingTable.getLocalhost());
        return new CorbaRoutingTable(peerRefs, localhost);
    }
}
