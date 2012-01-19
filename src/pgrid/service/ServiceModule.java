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

package pgrid.service;

import com.google.inject.AbstractModule;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import pgrid.service.anotations.constants.MaxRecursions;
import pgrid.service.anotations.constants.MaxRef;
import pgrid.service.anotations.constants.RepairTimeout;
import pgrid.service.exchange.ExchangeModule;
import pgrid.service.repair.RepairModule;
import pgrid.service.simulation.SimulationModule;

/**
 * @author Vourlakis Nikolas
 */
public class ServiceModule extends AbstractModule {

    private static final Logger logger_ = LoggerFactory.getLogger(ServiceModule.class);

    @Override
    protected void configure() {
        logger_.debug("Setting up service module");
        bind(LocalPeerContext.class).asEagerSingleton();

        binder().install(new ExchangeModule());
        binder().install(new RepairModule());
        binder().install(new SimulationModule());

        bindConstant()
                .annotatedWith(MaxRef.class)
                .to(1);
        bindConstant()
                .annotatedWith(MaxRecursions.class)
                .to(2);
        bindConstant()
                .annotatedWith(RepairTimeout.class)
                .to(1000); // milliseconds
    }
}
