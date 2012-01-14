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

package pgrid.service.bootstrap;

import pgrid.entity.routingtable.RoutingTable;

import java.io.FileNotFoundException;

/**
 * This is the interface to be implemented by a routing table to gain persistency
 * functionality.
 * <p/>
 * TODO: This interface and its implementation can be promoted to services cause they act upon entities.
 *
 * @author Vourlakis Nikolas <nvourlakis@gmail.com>
 */
public interface FileBootstrapService {

    /**
     * It opens the given filename and constructs the routing table based on
     * the information stored in the file.
     *
     * @param filename     the file to be opened.
     * @param routingTable that will be initialized from the file.
     * @throws FileNotFoundException when a failed attempt to open the file
     *                               occurs.
     * @throws PersistencyException  when a failure occurs during the
     *                               initialization of the routing table.
     *                               Examples of such failures are invalid
     *                               file format or failure to construct a
     *                               valid {@link pgrid.entity.internal.PGridHost} or other
     *                               types that the implementor can think of.
     *                               If such failures occur then the load
     *                               process must stop.
     */
    public void load(String filename, RoutingTable routingTable) throws FileNotFoundException, PersistencyException;

    /**
     * Captures the current state of the routing table and writes to the given
     * file.
     *
     * @param filename     the file where to write the routing table.
     * @param routingTable to be stored.
     * @throws FileNotFoundException in case of a failed attempt to open the
     *                               file.
     */
    public void store(String filename, RoutingTable routingTable) throws FileNotFoundException;
}

