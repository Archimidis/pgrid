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
import pgrid.service.simulation.SimulationService;
import pgrid.service.simulation.internal.SimulationDelegate;
import pgrid.service.simulation.internal.TucGridSimulationService;
import pgrid.utilities.ArgumentCheck;

import javax.inject.Inject;
import javax.inject.Provider;

/**
 * @author Nikolas Vourlakis <nvourlakis@gmail.com>
 */
public class SimulationProvider implements Provider<SimulationService> {
    private final SimulationService service_;

    @Inject
    public SimulationProvider(LocalPeerContext context, PersistencyDelegate persistency) {
        ArgumentCheck.checkNotNull(persistency, "Cannot initialize a SimulationProvider object with a null PersistencyDelegate value.");
        ArgumentCheck.checkNotNull(context, "Cannot initialize a SimulationProvider object with a null LocalPeerContext value.");
        ArgumentCheck.checkNotNull(context.getCorba(), "Uninitialized ORB in LocalPeerContext object passed to SimulationProvider.");
        ArgumentCheck.checkNotNull(context.getLocalRT(), "Uninitialized RoutingTable in LocalPeerContext object passed to SimulationProvider.");

        SimulationDelegate delegate =
                new SimulationDelegate(context.getCorba(), context.getLocalRT(), persistency);
        service_ = new TucGridSimulationService(context.getCorba(), delegate);
    }

    @Override
    public SimulationService get() {
        return service_;
    }
}
