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

package pgrid.service.repair;

import pgrid.entity.Host;

/**
 * This interface defines the repair service. During the life of a pgrid
 * network, peer failures will occur. This services gives the capability to the
 * local peer to a repair session. In cooperation with part or all the
 * network, these failures will be fixed. By fixing it is meant the maintenance
 * of the trie topology that the pgrid network forms. Finally the local peer
 * may have started the repair session, but may not be the one that will
 * finally fix the issue by itself.
 *
 * @author Vourlakis Nikolas
 */
public interface RepairService {

    /**
     * If the local peer came across a single node failure, this is the method
     * to call. At the end, a set of peers not necessarily containing the local
     * peer, will fix the issue. After this operation, all peers of the network
     * are informed of the failed peer and who where part of the solution and
     * update their routing table accordingly.
     *
     * @param failedHost host to be repaired.
     */
    public void fixNode(Host failedHost);

    /**
     * In the case where the local peer knows that a complete subtree of the
     * pgrid network has failed, this method must be called. At the end, a set
     * of peers not necessarily containing the local peer, will fix the
     * subtree. After this operation, all peers of the network are informed
     * of the failures and who where part of the solution and update their
     * routing table accordingly.
     *
     * @param subtreePath the path of the failed subtree to be repaired.
     * @param failedGroup the group of failed hosts belonging to same subtree
     *                    and must be repaired.
     */
    public void fixSubtree(String subtreePath, Host... failedGroup);
}
