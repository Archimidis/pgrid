/*
 * This file (pgrid.service.repair.internal.DefaultRepairService) is part of the libpgrid project.
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

    private final RoutingTable routingTable_;
    private final Host localhost_;
    private final ORB orb_;
    private FixNodeAlgorithm fix_;
    private int maxRef_;

    public DefaultRepairService(ORB orb, RoutingTable routingTable) {
        orb_ = orb;
        routingTable_ = routingTable;
        localhost_ = routingTable_.getLocalhost();
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
        Host hostToContinue = fix_.execute(failed, initialPath);
        RepairHandle remoteHandle = getRemoteHandle(hostToContinue);

        if (hostToContinue.compareTo(localhost_) == 0) {
            // the localhost and its conjugate will solve the issue
            replace(failed);
        }

        RepairIssue repairIssue = new RepairIssue();
        repairIssue.repairID = UUID.randomUUID().toString();
        repairIssue.senderPeer = Serializer.serializeHost(localhost_);
        repairIssue.failedPeerPath = failed.getHostPath().toString();
        repairIssue.failedPeer = Serializer.serializeHost(failed);
        repairIssue.footpath = hostToContinue.getHostPath().toString();
        repairIssue.repairTimestamp = System.currentTimeMillis();
        repairIssue.timeoutTimestamp = System.currentTimeMillis() + 1000; // + 1 min

        remoteHandle.fixNode(repairIssue);
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

        for (Host conjugate : conjugateLevel) {
            if (conjugate.getHostPath().toString().compareTo(failed.getHostPath().toString()) != 0) {
                PGridPath localhostHostPath = localhost_.getHostPath();
                if (localhostHostPath.value(localhostHostPath.length()) == '1') {
                    // conjugate reduce path
                    // localhost path = failed path
                } else { // == '0'
                    // localhost reduce path
                    // conjugate path = failed path
                }
            } else { // conjugate is the failed peer
                // localhost path = failed path
            }
            break;
        }
        // propagate change to network
    }
}
