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

package pgrid.service.repair.internal;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import pgrid.entity.Host;
import pgrid.service.spi.corba.repair.RepairHandlePOA;
import pgrid.service.spi.corba.repair.RepairIssue;
import pgrid.service.spi.corba.repair.RepairSolution;
import pgrid.service.utilities.Deserializer;

/**
 * @author Vourlakis Nikolas
 */
public class DefaultRepairHandle extends RepairHandlePOA {

    private static final Logger logger_ = LoggerFactory.getLogger(DefaultRepairHandle.class);

    RepairDelegate delegate_;

    public DefaultRepairHandle(RepairDelegate delegate) {
        delegate_ = delegate;
    }

    @Override
    public void fixNode(String footpath, RepairIssue issue) {
        delegate_.fixNode(footpath, Deserializer.deserializeHost(issue.failedPeer));
    }

    @Override
    public void fixSubtree(String footpath, String subtreePrefix, RepairIssue[] issues) {
        Host[] failedHosts = new Host[issues.length];
        for (int i = 0; i < issues.length; i++) {
            failedHosts[i] = Deserializer.deserializeHost(issues[i].failedPeer);
        }
        delegate_.fixSubtree(footpath, subtreePrefix, failedHosts);
    }

    @Override
    public void replace(String failedPath, RepairIssue[] issues) {
        Host[] failedHosts = new Host[issues.length];
        for (int i = 0; i < issues.length; i++) {
            failedHosts[i] = Deserializer.deserializeHost(issues[i].failedPeer);
        }
        delegate_.replace(failedPath, failedHosts);
    }

    @Override
    public void broadcastSolution(RepairSolution solution) {
        // TODO: Implement broadcastSolution
    }
}
