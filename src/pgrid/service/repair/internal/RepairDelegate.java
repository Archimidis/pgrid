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
import pgrid.service.repair.spi.FixNodeAlgorithm;
import pgrid.service.repair.spi.ReplaceStrategy;
import pgrid.service.spi.corba.exchange.ExchangeHandleHelper;
import pgrid.service.spi.corba.repair.IssueType;
import pgrid.service.spi.corba.repair.RepairHandle;
import pgrid.service.spi.corba.repair.RepairHandleHelper;
import pgrid.service.spi.corba.repair.RepairIssue;
import pgrid.service.utilities.Serializer;

import java.util.List;

/**
 * @author Vourlakis Nikolas <nvourlakis@gmail.com>
 */
public class RepairDelegate {

    private static final Logger logger_ = LoggerFactory.getLogger(RepairDelegate.class);

    private final RepairIssueRegistry registry_;
    private final RoutingTable routingTable_;
    private final ORB orb_;
    private FixNodeAlgorithm fix_;
    private ReplaceStrategy replace_;
    private int maxRef_;

    public RepairDelegate(ORB orb, RoutingTable routingTable, RepairIssueRegistry registry) {
        orb_ = orb;
        routingTable_ = routingTable;
        registry_ = registry;
    }

    public void setMaxRef(int maxRef) {
        if (maxRef < 0) {
            throw new IllegalArgumentException("A negative maxRef was given.");
        }
        maxRef_ = maxRef;
    }

    public void setFixNodeAlgorithm(FixNodeAlgorithm algorithm) {
        if (algorithm == null) {
            throw new NullPointerException("A null object was given instead of a valid FixNodeAlgorithm.");
        }
        fix_ = algorithm;
    }

    public void setReplaceStrategy(ReplaceStrategy replace) {
        if (replace == null) {
            throw new NullPointerException("A null object was given instead of a valid ReplaceStrategy.");
        }
        replace_ = replace;
    }

    public void fixNode(String footpath, Host failedHost) {
        validateService();
        if (routingTable_.contains(failedHost)) {
            routingTable_.removeReference(failedHost);
        }

        List<Host> continuation = fix_.execute(routingTable_, new PGridPath(footpath));

        RepairIssue newIssue = new RepairIssue();
        newIssue.issueType = IssueType.SINGLE_NODE;
        newIssue.failedPeer = Serializer.serializeHost(failedHost);
        newIssue.failedPath = failedHost.getHostPath().toString();

        if (continuation.size() == 0) {
            // the localhost doesn't know anyone that can fix the problem
            // store RepairIssue and the wait
        } else if (continuation.size() == 1) {
            Host host = continuation.get(0);
            if (routingTable_.getLocalhost().compareTo(host) == 0) {
                logger_.debug("The localhost prepares to fix the issue.");
                // the localhost is part of the solution
                if (!failedHost.getHostPath().isConjugateTo(routingTable_.getLocalhost().getHostPath())) {
                    logger_.debug("Informing the localhost's conjugate for the issue");
                    // the conjugate is not the failed peer
                    // rpc to host
                }
                replace_.execute(routingTable_, failedHost.getHostPath());
            }
            // rpc to host
        } else if (continuation.size() > 1) {
            for (Host host : continuation) {
                // rpc to host
            }
        }
    }

    public void fixSubtree(String footpath, Host... failedHosts) {

    }

    private void validateService() {
        if (replace_ == null) {
            throw new NullPointerException("Repair service is not in a valid state. No ReplaceStrategy was found.");
        }
        if (fix_ == null) {
            throw new NullPointerException("Repair service is not in a valid state. No FixNodeAlgorithm was found.");
        }
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
}
