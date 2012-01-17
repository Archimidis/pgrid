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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import pgrid.entity.Host;
import pgrid.service.LocalPeerContext;
import pgrid.service.simulation.PersistencyException;
import pgrid.service.simulation.SimulationService;
import pgrid.service.simulation.spi.PersistencyDelegate;

import java.io.FileNotFoundException;

/**
 * @author Nikolas Vourlakis <nvourlakis@gmail.com>
 */
public class TucGridSimulationService implements SimulationService {

    private static final Logger logger_ = LoggerFactory.getLogger(TucGridSimulationService.class);

    private final LocalPeerContext localPeerContext_;
    private final PersistencyDelegate delegate_;

    public TucGridSimulationService(LocalPeerContext localPeerContext, PersistencyDelegate delegate) {
        localPeerContext_ = localPeerContext;
        delegate_ = delegate;
    }

    @Override
    public void initializeLocalPeer(String filename)
            throws PersistencyException, FileNotFoundException {
        delegate_.load(filename, localPeerContext_.getLocalRT());
    }

    @Override
    public void startLocalPeer() {
        final ORB orb = localPeerContext_.getCorba();

        Thread orbThread = new Thread(new Runnable() {
            @Override
            public void run() {
                orb.run();
            }
        });
        orbThread.start();
    }

    @Override
    public void shutdownLocalPeer(String filename) throws FileNotFoundException {
        delegate_.store(filename, localPeerContext_.getLocalRT());
        localPeerContext_.getCorba().shutdown(true);
    }

    @Override
    public void killHost(Host host) {
        // TODO: implement killHost
    }
}
