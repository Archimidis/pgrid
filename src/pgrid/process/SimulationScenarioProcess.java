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

package pgrid.process;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import pgrid.entity.Host;
import pgrid.service.repair.RepairService;
import pgrid.service.simulation.SimulationService;

import javax.inject.Inject;

/**
 * @author Vourlakis Nikolas <nvourlakis@gmail.com>
 */
public class SimulationScenarioProcess {

    private static final Logger logger_ = LoggerFactory.getLogger(SimulationScenarioProcess.class);

    private SimulationService simulation_;
    private RepairService repair_;

    @Inject
    public SimulationScenarioProcess(SimulationService simulation, RepairService repair) {
        simulation_ = simulation;
        repair_ = repair;
    }

    public void singleHostFailure(String filename, Host hostToKill, Host... network) {
        simulation_.killHost(hostToKill);

        try {
            Thread.sleep(1000);
        } catch (InterruptedException ignored) {
        }

        repair_.fixNode(hostToKill);

        try {
            Thread.sleep(1000);
        } catch (InterruptedException ignored) {
        }

        for (Host host : network) {
            simulation_.terminateSimulation(network);
        }

        try {
            Thread.sleep(1000);
        } catch (InterruptedException ignored) {
        }

        simulation_.shutdownLocalPeer();
    }
}
