/*
 * This file (pgrid.service.LocalPeerContext) is part of the libpgrid project.
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

package pgrid.service;

import org.omg.CORBA.ORB;
import pgrid.entity.routingtable.RoutingTable;

/**
 * A context where information about the local peer are stored, like references
 * to its routing table and the corba facility.
 *
 * @author Vourlakis Nikolas
 */
public class LocalPeerContext {

    private RoutingTable localRT_ = null;
    private ORB orb_ = null;

    /**
     * Accepts a reference to the routing table associated with the local peer.
     *
     * @param localRT a reference to the RoutingTable.
     */
    public synchronized void setRoutingTable(RoutingTable localRT) {
        localRT_ = localRT;
    }

    /**
     * Accepts a reference to the corba facility associated with the local
     * peer.
     *
     * @param orb a reference to the corba facility.
     */
    public synchronized void setOrb(ORB orb) {
        orb_ = orb;
    }

    /**
     * Returns the routing table stored by the local peer.
     *
     * @return the local RoutingTable.
     */
    public synchronized RoutingTable getLocalRT() {
        return localRT_;
    }

    /**
     * Returns the corba facility associated with the local peer.
     *
     * @return a reference to the local corba facility.
     */
    public synchronized ORB getCorba() {
        return orb_;
    }
}
