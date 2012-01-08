/*
 * This file (pgrid.service.repair.internal.DefaultRepairService) is part of the libpgrid project.
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
import pgrid.entity.Host;
import pgrid.entity.routingtable.RoutingTable;
import pgrid.service.CommunicationException;
import pgrid.service.repair.RepairService;
import pgrid.service.repair.spi.FixNodeAlgorithm;

/**
 * @author Vourlakis Nikolas
 */
public class DefaultRepairService implements RepairService {
    private final RoutingTable routingTable_;
    private final ORB orb_;
    private FixNodeAlgorithm fix_;
    private int maxRef_;

    public DefaultRepairService(ORB orb, RoutingTable routingTable) {
        orb_ = orb;
        routingTable_ = routingTable;
    }

    public void setMaxRef(int maxRef) {
        maxRef_ = maxRef;
    }

    public void setFixNodeAlgorithm(FixNodeAlgorithm algorithm) {
        fix_ = algorithm;
    }

    @Override
    public void fixNode(Host failed, String path) throws CommunicationException {
        // TODO: implement fixNode
        System.out.println("FixNodeAlgorithm object: " + fix_);
    }

    @Override
    public void replace(Host solution) throws CommunicationException {
        // TODO: implement replace
    }
}
