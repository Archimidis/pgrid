/*
 * This file (pgrid.service.repair.spi.FixNodeAlgorithm) is part of the pgrid project.
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

package pgrid.service.repair.spi;

import pgrid.entity.Host;
import pgrid.entity.PGridPath;
import pgrid.entity.routingtable.RoutingTable;

/**
 * This interface represents the algorithm that will be executed in case of a
 * network failure. It's purpose is to select a peer in the pgrid network that
 * is either capable of fixing the issue himself or knows another peer most
 * likely to be able to solve the issue.
 *
 * @author Vourlakis Nikolas
 */
public interface FixNodeAlgorithm {

    /**
     * Given a failed host, the algorithm must decide which host in the network
     * is able to fix the problem. The algorithm must follow a certain path to
     * decide that starting from the conjugate path of the failed host. It will
     * end at the host that will give the solution. This path trace is
     * represented by the path argument and is essential information for the
     * host that will continue executing the algorithm. It shows the direction
     * that must be followed.
     *
     * @param routingTable of the local host to be used by the algorithm.
     * @param failed       host to be fixed.
     * @param path         the current path trace as described above.
     * @return the host most likely to know how to solve the issue.
     */
    public Host execute(RoutingTable routingTable, Host failed, PGridPath path);
}
