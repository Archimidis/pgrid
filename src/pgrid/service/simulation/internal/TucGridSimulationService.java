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

import org.omg.CORBA.ORB;
import org.omg.CORBA.SystemException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import pgrid.entity.Host;
import pgrid.service.CommunicationException;
import pgrid.service.simulation.PersistencyException;
import pgrid.service.simulation.SimulationService;
import pgrid.service.spi.corba.simulation.SimulationHandle;
import pgrid.service.spi.corba.simulation.SimulationHandleHelper;

import java.io.FileNotFoundException;

/**
 * @author Nikolas Vourlakis <nvourlakis@gmail.com>
 */
public class TucGridSimulationService implements SimulationService {

    private static final Logger logger_ = LoggerFactory.getLogger(TucGridSimulationService.class);

    private final ORB orb_;
    private final SimulationDelegate delegate_;

    public TucGridSimulationService(ORB orb, SimulationDelegate simulationDelegate) {
        orb_ = orb;
        delegate_ = simulationDelegate;
    }

    @Override
    public void initializeLocalPeer(String filename)
            throws PersistencyException, FileNotFoundException {
        delegate_.initLocal(filename);
    }

    @Override
    public void shutdownLocalPeer() {
        delegate_.die();
    }

    @Override
    public void killHost(Host host) {
        try {
            SimulationHandle simulationHandle = getRemoteHandle(host);
            simulationHandle.die();
        } catch (CommunicationException e) {
            logger_.warn("{}:{} is already killed.", host, host.getPort());
        }
    }

    @Override
    public void terminateSimulation(Host... network) {
        for (Host host : network) {
            try {
                SimulationHandle simulationHandle = getRemoteHandle(host);
                simulationHandle.terminateSimulation();
                logger_.warn("{}:{} terminated.", host, host.getPort());
            } catch (CommunicationException e) {
                logger_.warn("{}:{} is already terminated.", host, host.getPort());
            }
        }
    }

    private SimulationHandle getRemoteHandle(Host host) throws CommunicationException {
        String[] simulationHandleID = SimulationHandleHelper.id().split(":");
        String corbaloc = "corbaloc:iiop:[" +
                host.getAddress().getHostAddress() + "]:" + host.getPort()
                + "/" + simulationHandleID[1];
        logger_.debug("CORBALOC: {}", corbaloc);
        org.omg.CORBA.Object object = orb_.string_to_object(corbaloc);

        SimulationHandle handle;
        try {
            handle = SimulationHandleHelper.narrow(object);
        } catch (SystemException e) {
            throw new CommunicationException(e.getCause());
        }
        return handle;
    }
}
