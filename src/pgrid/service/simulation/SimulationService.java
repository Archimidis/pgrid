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

import pgrid.entity.Host;
import pgrid.service.CommunicationException;

/**
 * A simulation service that will be used during the experiments.
 *
 * @author Nikolas Vourlakis <nvourlakis@gmail.com>
 */
public interface SimulationService {

    /**
     * Shutdowns the local peer and all its started service in a normal way.
     * Before it stops, the peer will store its routing table. The name of the
     * file is name of the localhost.
     */
    public void shutdownLocalPeer();

    /**
     * Communicates with a remote host and force him to shutdown. The purpose
     * of this method is to simulate a failure in the pgrid network.
     *
     * @param host to be killed.
     */
    public void killHost(Host host);

    /**
     * Terminates the ongoing simulation. All peers in the pgrid network must
     * close. After this operation, the pgrid peer-to-peer is non existent
     * and the simulation is over. Time to check the results.
     *
     * @param network An array that contains all the hosts of the pgrid
     *                network. This is obviously needed only for the simulator
     *                peer.
     */
    public void terminateSimulation(Host... network);


    /**
     * Updates the information about the state (e.g. path) of the the given
     * host.
     *
     * @param host to be contacted and get his state.
     * @return an updated version of the given host.
     */
    public Host info(Host host) throws CommunicationException;
}
