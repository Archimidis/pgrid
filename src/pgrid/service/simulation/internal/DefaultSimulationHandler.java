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

package pgrid.service.simulation.internal;

import pgrid.service.corba.PeerReference;
import pgrid.service.corba.simulation.SimulationHandlePOA;
import pgrid.utilities.ArgumentCheck;

/**
 * @author Vourlakis Nikolas <nvourlakis@gmail.com>
 */
public class DefaultSimulationHandler extends SimulationHandlePOA {

    private final SimulationDelegate delegate_;

    public DefaultSimulationHandler(SimulationDelegate simulationDelegate) {
        ArgumentCheck.checkNotNull(simulationDelegate, "Cannot initialize a DefaultSimulationHandler object with a null SimulationDelegate value.");
        delegate_ = simulationDelegate;
    }

    @Override
    public PeerReference getInfo() {
        return delegate_.sendInfo();
    }

    @Override
    public void die() {
        delegate_.die();
    }

    @Override
    public void terminateSimulation() {
        delegate_.die();
    }
}
