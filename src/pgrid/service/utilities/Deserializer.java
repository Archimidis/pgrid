/*
 * This file (pgrid.service.utilities.Deserializer) is part of the libpgrid project.
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

package pgrid.service.utilities;

import pgrid.entity.Host;
import pgrid.entity.internal.PGridHost;
import pgrid.entity.routingtable.RoutingTable;
import pgrid.service.spi.corba.CorbaRoutingTable;
import pgrid.service.spi.corba.PeerReference;

import java.net.UnknownHostException;
import java.util.UUID;

/**
 * @author Vourlakis Nikolas
 */
public class Deserializer {
    /**
     * Transforms a {@link PeerReference} to a {@link PGridHost} object.
     *
     * @param peer the object to be deserialized.
     * @return the {@link PGridHost} object.
     */
    public static Host deserializeHost(PeerReference peer) {
        if (peer == null) {
            throw new NullPointerException("Cannot transform a null value to a Host object");
        }

        Host host = null;
        try {
            host = new PGridHost(peer.address, peer.port);
            host.setHostPath(peer.path);
            if (peer.timestamp > 0) {
                host.setTimestamp(peer.timestamp - 1); // based on how Lamport clock works
            }
            host.setUUID(UUID.fromString(peer.uuid));
        } catch (UnknownHostException e) {
            // Serialization guarantees that valid hosts will be deserialized.
        }
        return host;
    }

    /**
     * Accepts the corba type representing the routing table and transform it
     * to a {@link RoutingTable} object.
     *
     * @param corbaRoutingTable the corba routing table object to be deserialized.
     * @return the {@link RoutingTable} object.
     */
    public static RoutingTable deserializeRoutingTable(CorbaRoutingTable corbaRoutingTable) {
        if (corbaRoutingTable == null) {
            throw new NullPointerException("Cannot transform a null value to a Host object");
        }

        RoutingTable routingTable = new RoutingTable();
        Host localhost = deserializeHost(corbaRoutingTable.peer);
        routingTable.setLocalhost(localhost);

        int levelIndex = 0;
        for (PeerReference[] serializedLevel : corbaRoutingTable.refs) {
            for (PeerReference serializedHost : serializedLevel) {
                routingTable.addReference(levelIndex, deserializeHost(serializedHost));
            }
            levelIndex++;
        }
        return routingTable;
    }
}
