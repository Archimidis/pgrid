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

import org.omg.CORBA.ORB;
import pgrid.entity.routingtable.RoutingTable;
import pgrid.service.repair.spi.FixNodeAlgorithm;
import pgrid.service.spi.corba.repair.RepairHandlePOA;
import pgrid.service.spi.corba.repair.RepairIssue;
import pgrid.service.spi.corba.repair.RepairSolution;

/**
 * @author Vourlakis Nikolas
 */
public class DefaultRepairHandle extends RepairHandlePOA {

    private final RepairIssueRegistry registry_;
    private final RoutingTable routingTable_;
    private final ORB orb_;
    private FixNodeAlgorithm fix_;
    private int maxRef_;

    public DefaultRepairHandle(ORB orb, RoutingTable routingTable, RepairIssueRegistry registry) {
        orb_ = orb;
        routingTable_ = routingTable;
        registry_ = registry;
    }

    public void setMaxRef(int maxRef) {
        maxRef_ = maxRef;
    }

    public void setFixNodeAlgorithm(FixNodeAlgorithm algorithm) {
        fix_ = algorithm;
    }

    @Override
    public void fixNode(RepairIssue issue) {
        // TODO: implement fixNode
        // check registry if it
    }

    @Override
    public void replace(RepairIssue issue) {
        // TODO: implement replace
    }

    @Override
    public void propagate(RepairSolution solution) {
        // TODO: implement propagate
    }
}
