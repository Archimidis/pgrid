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

import com.google.inject.AbstractModule;
import com.google.inject.Scopes;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import pgrid.service.ServiceRegistration;
import pgrid.service.repair.internal.RepairIssueRegistry;
import pgrid.service.repair.internal.RepairRegistration;
import pgrid.service.repair.internal.ThesisFixNodeAlgorithm;
import pgrid.service.repair.internal.TwinsReplaceStrategy;
import pgrid.service.repair.spi.FixNodeAlgorithm;
import pgrid.service.repair.spi.RepairHandleProvider;
import pgrid.service.repair.spi.RepairProvider;
import pgrid.service.repair.spi.ReplaceStrategy;
import pgrid.service.spi.corba.repair.RepairHandlePOA;

/**
 * @author Vourlakis Nikolas
 */
public class RepairModule extends AbstractModule {

    private static final Logger logger_ = LoggerFactory.getLogger(RepairModule.class);

    @Override
    protected void configure() {
        logger_.debug("Setting up repair service module");

        bind(RepairIssueRegistry.class).asEagerSingleton();

        bind(FixNodeAlgorithm.class).to(ThesisFixNodeAlgorithm.class);
        bind(ReplaceStrategy.class).to(TwinsReplaceStrategy.class);

        bind(RepairService.class).toProvider(RepairProvider.class);
        // returns the same handle every time
        bind(RepairHandlePOA.class).toProvider(RepairHandleProvider.class);
        bind(RepairHandleProvider.class).in(Scopes.SINGLETON);

        bind(ServiceRegistration.class)
                .annotatedWith(Repair.class)
                .to(RepairRegistration.class);
    }
}
