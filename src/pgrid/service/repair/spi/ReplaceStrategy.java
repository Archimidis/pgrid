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
import pgrid.entity.routingtable.RoutingTable;

/**
 * This interface represents the strategy that will be followed when the
 * {@link FixNodeAlgorithm} will reach the appropriate hosts. These hosts are
 * responsible for replacing the failed host. There are two methods based on
 * the identity of the failed host. Different path must be taken by the
 * localhost if the failed host is its conjugate.
 * These methods will only affect the path of the hosts involved in the
 * repairing.
 *
 * @author Vourlakis Nikolas <nvourlakis@gmail.com>
 */
public interface ReplaceStrategy {

    /**
     * The failed host is the conjugate of the localhost. This method will run
     * that portion of the replace algorithm that makes that assumption and
     * affects only the path of the localhost.
     *
     * @param routingTable of the localhost.
     * @param failed       to be replaced by the caller host.
     */
    public void execute(RoutingTable routingTable, Host failed);

    /**
     * The failed host is not the conjugate of the localhost. This method is
     * meant to be run both from the localhost and its conjugate at the same
     * time. They must work together in order to replace the failed host. By
     * passing a null Host object in the place of the conjugate argument
     * {@link ReplaceStrategy#execute(pgrid.entity.routingtable.RoutingTable, pgrid.entity.Host)}
     * must not run. Instead a {@link NullPointerException} must be thrown.     *
     *
     * @param routingTable of the localhost.
     * @param conjugate    of the localhost.
     * @param failed       to be replaced by the localhost and its conjugate.
     */
    public void execute(RoutingTable routingTable, Host conjugate, Host failed);
}
