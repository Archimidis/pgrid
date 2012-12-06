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

package pgrid.service.repair.spi;

import pgrid.entity.Host;
import pgrid.entity.PGridPath;
import pgrid.entity.routingtable.RoutingTable;

import java.util.List;

/**
 * This interface represents the algorithm that will be executed in case of a
 * network failure. It's purpose is to select a set of peers belonging to a
 * path that are either capable of fixing the issue or
 * knows other peer most likely to be able to solve the issue.
 *
 * @author Vourlakis Nikolas
 */
public interface FindContinuationAlgorithm {

    /**
     * Given a path trace followed till now by the algorithm in the network, it
     * must be decided which must be the direction to go to next. The algorithm
     * will start from the conjugate path of the failed host. It must end at a
     * path close to a host that is capable to solve the issue.
     *
     * @param routingTable of the local host to be used by the algorithm.
     * @param pathTrace    the current path trace as described above.
     * @return a list of hosts able to solve the issue.
     */
    public List<Host> execute(RoutingTable routingTable, PGridPath pathTrace);
}
