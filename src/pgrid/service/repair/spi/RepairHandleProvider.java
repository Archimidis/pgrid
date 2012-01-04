/*
 * This file (pgrid.service.repair.spi.RepairHandleProvider) is part of the libpgrid project.
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
import pgrid.service.spi.corba.repair.RepairHandlePOA;

import javax.inject.Inject;
import javax.inject.Provider;

/**
 * @author Vourlakis Nikolas
 */
public class RepairHandleProvider implements Provider<RepairHandlePOA> {
    private LocalPeerContext context_;
    private final int MAX_REF;
    private FixNodeAlgorithm fix_;
    private ReplaceStrategy replace_;

    @Inject
    public RepairHandleProvider(LocalPeerContext context, FixNodeAlgorithm fix, ReplaceStrategy replace, @MaxRef int maxRef) {
        context_ = context;
        fix_ = fix;
        replace_ = replace;
        MAX_REF = maxRef;
    }

    @Override
    public RepairHandlePOA get() {
        DefaultRepairHandle service = new DefaultRepairHandle(context_.getCorba(), context_.getLocalRT());
        service.setFixNodeAlgorithm(fix_);
        service.setReplaceAlgorithm(replace_);
        service.setMaxRef(MAX_REF);
        return service;
    }
}
