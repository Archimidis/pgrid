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

package pgrid.service.simulation.spi;

import pgrid.service.LocalPeerContext;
import pgrid.service.corba.simulation.SimulationHandlePOA;
import pgrid.service.simulation.internal.DefaultSimulationHandler;
import pgrid.service.simulation.internal.SimulationDelegate;

import javax.inject.Inject;
import javax.inject.Provider;

/**
 * @author Vourlakis Nikolas <nvourlakis@gmail.com>
 */
public class SimulationHandlerProvider implements Provider<SimulationHandlePOA> {
    private DefaultSimulationHandler poa_;

    @Inject
    public SimulationHandlerProvider(LocalPeerContext context, PersistencyDelegate persistency) {
        SimulationDelegate delegate = new SimulationDelegate(context.getCorba(), context.getLocalRT(), persistency);
        poa_ = new DefaultSimulationHandler(delegate);
    }


    @Override
    public SimulationHandlePOA get() {
        return poa_;
    }
}
