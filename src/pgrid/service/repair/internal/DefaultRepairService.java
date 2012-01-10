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

package pgrid.service.repair.internal;

import org.omg.CORBA.ORB;
import org.omg.CORBA.SystemException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import pgrid.entity.Host;
import pgrid.entity.PGridPath;
import pgrid.entity.routingtable.RoutingTable;
import pgrid.service.CommunicationException;
import pgrid.service.repair.RepairService;
import pgrid.service.repair.spi.FixNodeAlgorithm;
import pgrid.service.spi.corba.exchange.ExchangeHandleHelper;
import pgrid.service.spi.corba.repair.RepairHandle;
import pgrid.service.spi.corba.repair.RepairHandleHelper;
import pgrid.service.spi.corba.repair.RepairIssue;
import pgrid.service.utilities.Serializer;

import java.util.Collection;
import java.util.UUID;

/**
 * @author Vourlakis Nikolas
 */
public class DefaultRepairService implements RepairService {

    private static final Logger logger_ = LoggerFactory.getLogger(DefaultRepairService.class);

    private final RepairIssueRegistry registry_;
    private final RoutingTable routingTable_;
    private final ORB orb_;
    private FixNodeAlgorithm fix_;
    private int maxRef_;

    // TODO: @RepairTimeout constant
    private int REPAIR_TIMEOUT = 1000; // 1 min

    public DefaultRepairService(ORB orb, RoutingTable routingTable, RepairIssueRegistry registry) {
        orb_ = orb;
        routingTable_ = routingTable;
        registry_ = registry;
    }

    public void setMaxRef(int maxRef) {
        maxRef_ = maxRef;
    }

    public void setFixNodeAlgorithm(FixNodeAlgorithm algorithm) {
        fix_ = algorithm;
    }

    @Override
    public void fixNode(Host failed) throws CommunicationException {
        // TODO: complete fixNode
        if (failed == null) {
            throw new NullPointerException("Cannot execute the repair service with a null failed host.");
        }

        if (routingTable_.contains(failed)) {
            routingTable_.removeReference(failed);
        }

        PGridPath initialPath = algorithmPathExecution(failed.getHostPath());
        Host hostToContinue = fix_.execute(routingTable_, failed, initialPath);

        Host localhost = routingTable_.getLocalhost();
        if (hostToContinue.compareTo(localhost) == 0) {
            // the localhost and its conjugate will solve the issue
            logger_.info("Localhost will give the solution.");
            replace(failed);
        } else { // the algorithm must continue remotely
            RepairHandle remoteHandle = getRemoteHandle(hostToContinue);

            RepairIssue repairIssue = new RepairIssue();
            repairIssue.repairID = UUID.randomUUID().toString();
            repairIssue.senderPeer = Serializer.serializeHost(localhost);
            repairIssue.failedPeerPath = failed.getHostPath().toString();
            repairIssue.failedPeer = Serializer.serializeHost(failed);
            repairIssue.footpath = hostToContinue.getHostPath().toString();
            repairIssue.repairTimestamp = System.currentTimeMillis();
            repairIssue.timeoutTimestamp = System.currentTimeMillis() + REPAIR_TIMEOUT;

            remoteHandle.fixNode(repairIssue);
        }
    }

    private PGridPath algorithmPathExecution(PGridPath failedHostPath) {
        String root = failedHostPath.subPath(0, failedHostPath.length() - 1);
        char lastChar = failedHostPath.value(failedHostPath.length() - 1);
        PGridPath initialPath = new PGridPath(root);
        initialPath.revertAndAppend(lastChar);
        return initialPath;
    }

    private RepairHandle getRemoteHandle(Host host) throws CommunicationException {
        String[] exchangeHandleID = ExchangeHandleHelper.id().split(":");
        String corbaloc = "corbaloc:iiop:[" +
                host.getAddress().getHostAddress() + "]:" + host.getPort()
                + "/" + exchangeHandleID[1];
        logger_.debug("CORBALOC: {}", corbaloc);
        org.omg.CORBA.Object object = orb_.string_to_object(corbaloc);

        RepairHandle handle;
        try {
            handle = RepairHandleHelper.narrow(object);
        } catch (SystemException e) {
            logger_.warn("Cannot reach the host {}:{} needed for repairing.", host, host.getPort());
            logger_.warn("[Exception] {}", e.getCause().getMessage());
            throw new CommunicationException(e.getCause());
        }
        return handle;
    }

    private void replace(Host failed) throws CommunicationException {
        Collection<Host> conjugateLevel = routingTable_.getLevel(routingTable_.levelNumber() - 1);
        if (conjugateLevel.size() > 1) {
            logger_.error("Conjugate subtree contains more than 1 hosts. " +
                    "Something went wrong with the algorithm execution.");
            return;
        }

        Host conjugate = null;
        for (Host host : conjugateLevel) {
            conjugate = host;
        }

        Host localhost = routingTable_.getLocalhost();
        PGridPath localhostPath = localhost.getHostPath();
        if (conjugate != null) {
            // conjugate path != failed path
            if (localhostPath.value(localhostPath.length()) == '1') {
                logger_.info("Localhost new path: {}.", failed.getHostPath());
                logger_.info("Conjugate will reduce its path by one.");
                // conjugate reduce path
                RepairHandle handle = getRemoteHandle(conjugate);
                //handle.replace();
                // localhost path = failed path
                localhost.setHostPath(failed.getHostPath().toString());
            } else { // == '0'
                logger_.info("Conjugate new path: {}.", failed.getHostPath());
                logger_.info("Localhost will reduce its path by one.");
                // localhost reduce path
                int end = localhostPath.length() - 2;
                String newPath = end < 0 ? "" : localhostPath.subPath(0, end);
                localhost.setHostPath(newPath);
                // conjugate path = failed path
            }
        } else { // conjugate is the failed peer
            // conjugate path == failed path
            logger_.info("Conjugate is the failed peer.");
            if (routingTable_.levelNumber() <= 1) {
                logger_.info("Localhost will reduce its path by one.");
                int end = localhostPath.length() - 2;
                String newPath = end < 0 ? "" : localhostPath.subPath(0, end);
                localhost.setHostPath(newPath);
                // the network was consisted only by the localhost and the failed peer. // TODO: test case
            } else {
                logger_.info("Conjugate new path: {}.", failed.getHostPath());
                // localhost path = failed path
                localhost.setHostPath(failed.getHostPath().toString());
            }
        }
        logger_.debug("Localhost new path: {}", localhost.getHostPath());
        // TODO: refresh routing table after path change
        logger_.info("Propagating solution");
        // propagate change to network
    }
}
