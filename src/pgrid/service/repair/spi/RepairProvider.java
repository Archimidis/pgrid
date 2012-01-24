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
import pgrid.service.repair.internal.RepairDelegate;
import pgrid.service.repair.internal.RepairIssueRegistry;
import pgrid.utilities.ArgumentCheck;

import javax.inject.Inject;
import javax.inject.Provider;

/**
 * @author Vourlakis Nikolas
 */
public class RepairProvider implements Provider<RepairService> {
    private final LocalPeerContext context_;
    private final int MAX_REF;
    private FixNodeAlgorithm fix_;
    private ReplaceStrategy replace_;
    private final RepairIssueRegistry registry_;

    @Inject
    public RepairProvider(LocalPeerContext context, RepairIssueRegistry registry, @MaxRef int maxRef) {
        ArgumentCheck.checkNotNull(context, "Cannot initialize a RepairProvider object with a null LocalPeerContext value.");
        ArgumentCheck.checkNotNull(context.getCorba(), "Uninitialized ORB in LocalPeerContext object passed to RepairProvider.");
        ArgumentCheck.checkNotNull(context.getLocalRT(), "Uninitialized RoutingTable in LocalPeerContext object passed to RepairProvider.");
        ArgumentCheck.checkNotNull(registry, "Cannot initialize a RepairProvider object with a null RepairIssueRegistry value.");

        context_ = context;
        registry_ = registry;
        MAX_REF = maxRef;
    }

    @Inject
    protected void setFixNodeAlgorithm(FixNodeAlgorithm fix) {
        ArgumentCheck.checkNotNull(fix, "Tried to pass a null FixNodeAlgorithm object at RepairProvide.");
        fix_ = fix;
    }

    @Inject
    protected void setReplaceStrategy(ReplaceStrategy replace) {
        ArgumentCheck.checkNotNull(replace, "Tried to pass a null ReplaceStrategy object at RepairProvide.");
        replace_ = replace;
    }

    @Override
    public RepairService get() {
        RepairDelegate delegate = new RepairDelegate(context_.getCorba(), context_.getLocalRT(), registry_);
        delegate.setFixNodeAlgorithm(fix_);
        delegate.setReplaceStrategy(replace_);
        delegate.setMaxRef(MAX_REF);

        return new DefaultRepairService(delegate);
    }
}
