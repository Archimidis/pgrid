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
import pgrid.entity.EntityFactory;
import pgrid.entity.Host;
import pgrid.service.CommunicationException;
import pgrid.service.simulation.SimulationService;
import pgrid.service.simulation.spi.SimulationProvider;
import pgrid.utilities.IOUtilities;

import javax.inject.Inject;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * @author Vourlakis Nikolas <nvourlakis@gmail.com>
 */
public class ControlProcess {

    private static final Logger logger_ = LoggerFactory.getLogger(ControlProcess.class);

    private SimulationService simulation_;
    private List<Host> network_;

    @Inject
    public ControlProcess(SimulationProvider provider) {
        simulation_ = provider.get();
        network_ = new ArrayList<Host>();
    }

    public void loadNetworkFile(String filename, EntityFactory entityFactory) {
        BufferedReader bufferedReader = null;
        try {
            bufferedReader = new BufferedReader(new FileReader(filename));
            String strLine;
            while ((strLine = bufferedReader.readLine()) != null) {
                String[] split = strLine.split(",");
                Host host = entityFactory.newHost(split[0], Integer.valueOf(split[1]));
                split[2] = split[2].trim();
                host.setHostPath((split[2].isEmpty() ? "" : split[2]));
                host.setUUID(UUID.fromString(split[4]));
                network_.add(host);
            }
        } catch (IOException e) {
            logger_.error("{}", e);
            System.exit(1);
        } finally {
            IOUtilities.closeQuietly(bufferedReader);
        }
    }

    public void killHost(String hostname) {
        if (hostname == null || hostname.isEmpty()) {
            return;
        }

        Host hostToKill = null;
        for (Host host : network_) {
            if (host.getAddress().getHostName().equals(hostname)) {
                hostToKill = host;
            }
        }
        if (hostToKill != null) {
            logger_.info("Killing remote host {}:{} on path {}",
                    new Object[]{hostToKill,
                            hostToKill.getPort(),
                            hostToKill.getHostPath()});
            network_.remove(hostToKill);
            simulation_.killHost(hostToKill);
        }
    }

    public void forceRepairSingleHost(String initiator, String failed) {
    }

    public void forceRepairSubtree(String initiator, String subtree) {
    }

    public void info() {
        List<Host> newList = new ArrayList<Host>(network_.size());

        for (Host host : network_) {
            try {
                newList.add(simulation_.info(host));
            } catch (CommunicationException e) {
                logger_.warn("{}", e.getMessage());
            }
        }
        network_ = newList;
        if (network_.isEmpty()) {
            System.out.println("No are hosts present in the network.");
        }
        for (Host host : network_) {
            System.out.println("["+host.getHostPath()+"] "+host+":"+host.getPort());
        }
    }

    public void exit() {
        logger_.info("Shutting down remote hosts.");
        Host[] hosts = network_.toArray(new Host[network_.size()]);
        simulation_.terminateSimulation(hosts);
        logger_.info("Shutting down local host");
        simulation_.shutdownLocalPeer();
    }
}
