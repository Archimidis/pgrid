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

package pgrid.service.simulation;

import com.google.inject.AbstractModule;
import com.google.inject.Scopes;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import pgrid.service.ServiceRegistration;
import pgrid.service.corba.simulation.SimulationHandlePOA;
import pgrid.service.simulation.internal.SimulationRegistration;
import pgrid.service.simulation.internal.XMLPersistencyService;
import pgrid.service.simulation.spi.PersistencyDelegate;
import pgrid.service.simulation.spi.SimulationHandlerProvider;
import pgrid.service.simulation.spi.SimulationProvider;

/**
 * @author Vourlakis Nikolas <nvourlakis@gmail.com>
 */
public class SimulationModule extends AbstractModule {
    private static final Logger logger_ = LoggerFactory.getLogger(SimulationModule.class);

    @Override
    protected void configure() {
        logger_.debug("Setting up simulation service module");

        bind(PersistencyDelegate.class).to(XMLPersistencyService.class);

        // returns the same instance
        bind(SimulationService.class).toProvider(SimulationProvider.class);
        bind(SimulationProvider.class).in(Scopes.SINGLETON);

        // returns the same handle every time
        bind(SimulationHandlePOA.class).toProvider(SimulationHandlerProvider.class);
        bind(SimulationHandlerProvider.class).in(Scopes.SINGLETON);

        bind(ServiceRegistration.class)
                .annotatedWith(Simulation.class)
                .to(SimulationRegistration.class);
    }
}
