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
import pgrid.service.repair.RepairService;
import pgrid.service.repair.internal.DefaultRepairService;
import pgrid.service.repair.internal.RepairIssueRegistry;

import javax.inject.Inject;
import javax.inject.Provider;

/**
 * @author Vourlakis Nikolas
 */
public class RepairProvider implements Provider<RepairService> {
    private LocalPeerContext context_;
    private final int MAX_REF;
    private FixNodeAlgorithm fix_;
    private RepairIssueRegistry registry_;

    @Inject
    public RepairProvider(LocalPeerContext context, RepairIssueRegistry registry, FixNodeAlgorithm fix, @MaxRef int maxRef) {
        context_ = context;
        registry_ = registry;
        fix_ = fix;
        MAX_REF = maxRef;
    }

    @Override
    public RepairService get() {
        DefaultRepairService service = new DefaultRepairService(context_.getCorba(), context_.getLocalRT(), registry_);
        service.setFixNodeAlgorithm(fix_);
        service.setMaxRef(MAX_REF);
        return service;
    }
}
