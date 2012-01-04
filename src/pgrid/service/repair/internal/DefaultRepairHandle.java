/*
 * This file (pgrid.service.repair.internal.DefaultRepairHandle) is part of the libpgrid project.
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
import pgrid.service.repair.spi.ReplaceStrategy;
import pgrid.service.spi.corba.repair.RepairHandlePOA;
import pgrid.service.spi.corba.repair.RepairIssue;
import pgrid.service.spi.corba.repair.RepairSolution;

/**
 * @author Vourlakis Nikolas
 */
public class DefaultRepairHandle extends RepairHandlePOA {

    private final RoutingTable routingTable_;
    private final ORB orb_;
    private FixNodeAlgorithm fix_;
    private ReplaceStrategy replace_;
    private int maxRef_;

    public DefaultRepairHandle(ORB orb, RoutingTable routingTable) {
        orb_ = orb;
        routingTable_ = routingTable;
    }

    public void setMaxRef(int maxRef) {
        maxRef_ = maxRef;
    }

    public void setFixNodeAlgorithm(FixNodeAlgorithm algorithm) {
        fix_ = algorithm;
    }

    public void setReplaceAlgorithm(ReplaceStrategy algorithm) {
        replace_ = algorithm;
    }

    @Override
    public void fixNode(RepairIssue issue) {
        // TODO: implement fixNode
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
