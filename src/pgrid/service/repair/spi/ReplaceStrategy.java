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

import pgrid.entity.PGridPath;
import pgrid.entity.routingtable.RoutingTable;

/**
 * This interface represents the strategy that will be followed when the
 * {@link FindContinuationAlgorithm} will reach the appropriate hosts. These hosts are
 * responsible for replacing the failed host.
 * This algorithm will only affect the path of the hosts involved in the
 * repairing.
 *
 * @author Vourlakis Nikolas <nvourlakis@gmail.com>
 */
public interface ReplaceStrategy {

    /**
     * The localhost must change his path in such a way that the failed path
     * will be replaced. A group of hosts can run this method and decide who
     * will replace the path and how the others will act.
     *
     * @param routingTable of the localhost.
     * @param failedPath   to be replaced.
     */
    public void execute(RoutingTable routingTable, PGridPath failedPath);
}
