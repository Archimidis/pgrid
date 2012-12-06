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
import pgrid.service.corba.repair.RepairHandlePOA;
import pgrid.service.repair.internal.DefaultRepairHandle;
import pgrid.service.repair.internal.RepairDelegate;
import pgrid.service.repair.internal.RepairIssueRegistry;
import pgrid.utilities.ArgumentCheck;

import javax.inject.Inject;
import javax.inject.Provider;

/**
 * @author Vourlakis Nikolas
 */
public class RepairHandleProvider implements Provider<RepairHandlePOA> {
    private DefaultRepairHandle poa_;
    private final RepairDelegate delegate_;

    @Inject
    public RepairHandleProvider(LocalPeerContext context, RepairIssueRegistry registry, @MaxRef int maxRef) {
        ArgumentCheck.checkNotNull(context, "Cannot initialize a RepairHandleProvider object with a null LocalPeerContext value.");
        ArgumentCheck.checkNotNull(context.getCorba(), "Uninitialized ORB in LocalPeerContext object passed to RepairHandleProvider.");
        ArgumentCheck.checkNotNull(context.getLocalRT(), "Uninitialized RoutingTable in LocalPeerContext object passed to RepairHandleProvider.");
        ArgumentCheck.checkNotNull(registry, "Cannot initialize a RepairHandleProvider object with a null RepairIssueRegistry value.");

        delegate_ = new RepairDelegate(context.getCorba(), context.getLocalRT(), registry);
        delegate_.setMaxRef(maxRef);
        poa_ = null;
    }

    @Inject
    protected void setFixNodeAlgorithm(FindContinuationAlgorithm fix) {
        ArgumentCheck.checkNotNull(fix, "Tried to pass a null FindContinuationAlgorithm object at RepairHandleProvider.");
        delegate_.setFixNodeAlgorithm(fix);
    }

    @Inject
    protected void setReplaceStrategy(ReplaceStrategy replace) {
        ArgumentCheck.checkNotNull(replace, "Tried to pass a null ReplaceStrategy object at RepairHandleProvider.");
        delegate_.setReplaceStrategy(replace);
    }

    @Override
    public RepairHandlePOA get() {
        if (poa_ == null) {
            poa_ = new DefaultRepairHandle(delegate_);
        }
        return poa_;
    }
}
