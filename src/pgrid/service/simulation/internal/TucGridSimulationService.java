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
import pgrid.service.corba.PeerReference;
import pgrid.service.corba.simulation.SimulationHandle;
import pgrid.service.corba.simulation.SimulationHandleHelper;
import pgrid.service.simulation.SimulationService;
import pgrid.utilities.ArgumentCheck;
import pgrid.utilities.Deserializer;

/**
 * @author Nikolas Vourlakis <nvourlakis@gmail.com>
 */
public class TucGridSimulationService implements SimulationService {

    private static final Logger logger_ = LoggerFactory.getLogger(TucGridSimulationService.class);

    private final ORB orb_;
    private final SimulationDelegate delegate_;

    public TucGridSimulationService(ORB orb, SimulationDelegate simulationDelegate) {
        ArgumentCheck.checkNotNull(orb, "Cannot initialize a DefaultSimulationHandler object with a null ORB value.");
        ArgumentCheck.checkNotNull(simulationDelegate, "Cannot initialize a DefaultSimulationHandler object with a null SimulationDelegate value.");

        orb_ = orb;
        delegate_ = simulationDelegate;
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
        logger_.debug("Hello");
        for (Host host : network) {
            logger_.debug("Trying to get a handle to {}:{}", host, host.getPort());
            try {
                SimulationHandle simulationHandle = getRemoteHandle(host);
                logger_.debug("I have a handle to {}:{}", host, host.getPort());
                simulationHandle.terminateSimulation();
                logger_.info("{}:{} terminated.", host, host.getPort());
            } catch (CommunicationException e) {
                logger_.warn("{}:{} is already terminated.", host, host.getPort());
            }
        }
    }

    @Override
    public Host info(Host host) throws CommunicationException {
        if (host == null) {
            throw new NullPointerException("A null host was given.");
        }

        try {
            SimulationHandle simulationHandle = getRemoteHandle(host);
            PeerReference peerReference = simulationHandle.getInfo();
            Host result = Deserializer.deserializeHost(peerReference);
            return result;
        } catch (CommunicationException e) {
            throw new CommunicationException(
                    "Host " + host + ":" + host.getPort() + " cannot be reached.");
        }
    }

    private SimulationHandle getRemoteHandle(Host host) throws CommunicationException {
        ArgumentCheck.checkNotNull(host, "Cannot retrieve a SimulationHandle for a null host.");

        String[] simulationHandleID = SimulationHandleHelper.id().split(":");
        String corbaloc = "corbaloc:iiop:[" +
                host.getAddress().getHostAddress() + "]:" + host.getPort()
                + "/" + simulationHandleID[1];
        logger_.debug("CORBALOC: {}", corbaloc);
        org.omg.CORBA.Object object = null;
        object = orb_.string_to_object(corbaloc);

        System.out.println("pass 1");
        SimulationHandle handle = null;
        try {
            handle = SimulationHandleHelper.narrow(object);
            System.out.println("pass 2");
        } catch (SystemException e) {
            System.out.println("error");
            throw new CommunicationException(e.getCause());
        }
        System.out.println("pass 3");
        return handle;
    }
}
