/*
 * This file (entity.routingtable.PersistentRoutingTable) is part of the libpgrid project.
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

package entity.routingtable;

import java.io.FileNotFoundException;

/**
 * This is the interface to be implemented by a routing table to gain persistency
 * functionality.
 *
 * @author Vourlakis Nikolas <nvourlakis@gmail.com>
 */
public interface PersistenceDelegate {

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
     *                               valid {@link entity.PGridHost} or other
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

