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
import pgrid.entity.routingtable.RoutingTable;
import pgrid.service.simulation.spi.PersistencyDelegate;

import java.io.FileNotFoundException;

/**
 * @author Vourlakis Nikolas <nvourlakis@gmail.com>
 */
public class SimulationDelegate {
    private static final Logger logger_ = LoggerFactory.getLogger(SimulationDelegate.class);

    private final RoutingTable routingTable_;
    private final ORB orb_;
    private final PersistencyDelegate delegate_;

    public SimulationDelegate(ORB orb, RoutingTable routingTable, PersistencyDelegate delegate) {
        orb_ = orb;
        routingTable_ = routingTable;
        delegate_ = delegate;
    }

    public void die() {
        logger_.info("Local peer is shutting down");
        String filename = routingTable_.getLocalhost().toString() + ".xml";
        try {
            delegate_.store(filename, routingTable_);
        } catch (FileNotFoundException e) {
        }
        logger_.info("Routing table stored in {}", filename);
        orb_.shutdown(false);
        //orb_.destroy();
        logger_.info("Corba facility terminated");

        logger_.info("Local peer {}:{} is terminating",
                routingTable_.getLocalhost(), routingTable_.getLocalhost().getPort());
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
        }
        System.exit(0);
    }
}
