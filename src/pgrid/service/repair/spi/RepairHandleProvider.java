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

import pgrid.service.LocalPeerContext;
import pgrid.service.anotations.constants.MaxRef;
import pgrid.service.repair.internal.DefaultRepairHandle;
import pgrid.service.repair.internal.RepairIssueRegistry;
import pgrid.service.spi.corba.repair.RepairHandlePOA;

import javax.inject.Inject;
import javax.inject.Provider;

/**
 * @author Vourlakis Nikolas
 */
public class RepairHandleProvider implements Provider<RepairHandlePOA> {
    private final DefaultRepairHandle poa_;

    @Inject
    public RepairHandleProvider(LocalPeerContext context, RepairIssueRegistry registry, FixNodeAlgorithm fix, @MaxRef int maxRef) {
        poa_ = new DefaultRepairHandle(context.getCorba(), context.getLocalRT(), registry);
        poa_.setFixNodeAlgorithm(fix);
        poa_.setMaxRef(maxRef);
    }

    @Override
    public RepairHandlePOA get() {
        return poa_;
    }
}
