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

import java.util.Arrays;
import java.util.List;

/**
 * XXX: [REFACTOR] fixNode code is partial duplicated by fixSubtree, DRY violation!
 *
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

    // Threadify
    public void fixNode(String footpath, Host failedHost) {
        logger_.debug("Fixing node {}", failedHost.getHostPath());
        validateService();

        if (registry_.containsHost(failedHost.getUUID())) {
            logger_.info("The localhost has already seen the issue and run the algorithm.");
            return;
        }

        if (!routingTable_.contains(failedHost)) {
            logger_.debug("The failed peer is either " +
                    "(a) solved and the replacer is already in the routing table or " +
                    "(b) solved but the replacer is missing cause of refMax constant.");
            return;
        }

        logger_.info("It is not solved and the local peer must execute the algorithm");
        routingTable_.removeReference(failedHost);
        if (!registry_.containsHost(failedHost.getUUID())) {
            logger_.debug("Localhost is storing the issue in the registry");
            registry_.newIssue(failedHost);
        }

        List<Host> continuation = fix_.execute(routingTable_, new PGridPath(footpath));
        PGridPath failedHostPath = failedHost.getHostPath();
        if (continuation.size() == 0) {
            logger_.debug("The localhost doesn't know anyone that can fix the problem");
            // wait ????
            String pathToCheck = failedHostPath.subPath(0, failedHostPath.length() - 1);
            if (registry_.isCompleteSubtree(new PGridPath(pathToCheck))) {
                logger_.debug("Generalizing to path {}", pathToCheck);
                registry_.newSubtreeFailure(pathToCheck);
                List<Host> hosts = registry_.commonPrefixIssues(pathToCheck);
                fixSubtree(algorithmPathExecution(new PGridPath(pathToCheck)).toString(),
                        pathToCheck, hosts.toArray(new Host[hosts.size()]));
            } // else solve by fixNode (?)
        } else if (continuation.size() == 1) {
            Host host = continuation.get(0);
            if (routingTable_.getLocalhost().compareTo(host) == 0) { // or conjugate
                logger_.debug("The localhost prepares to fix the issue.");
                // the localhost is part of the solution
                if (!failedHostPath.isConjugateTo(routingTable_.getLocalhost().getHostPath())) {
                    logger_.debug("Informing the localhost's conjugate for the issue");
                    // the conjugate is not the failed peer
                    Host conjugate = routingTable_.getLevelArray(routingTable_.levelNumber() - 1)[0];
                    try {
                        RepairHandle repairHandle = getRemoteHandle(conjugate);
                        // TODO: rpc to host
                    } catch (CommunicationException e) {
                        logger_.debug("{}:{} cannot be reached.", conjugate, conjugate.getPort());
                        fixNode(algorithmPathExecution(conjugate.getHostPath()).toString(), conjugate);
                    }
                }
                replace_.execute(routingTable_, failedHostPath);
            } else {
                logger_.debug("The localhost must forward a request to {}:{}", host, host.getPort());
                try {
                    RepairHandle repairHandle = getRemoteHandle(host);
                    // TODO: rpc to host
                } catch (CommunicationException e) {
                    logger_.debug("{}:{} is not reachable.", host, host.getPort());
                    fixNode(algorithmPathExecution(host.getHostPath()).toString(), host);
                }
            }
        } else if (continuation.size() > 1) {
            logger_.debug("The localhost must forward a request for a remote algorithm execution.");
            Host selectedHost = continuation.get(0);
            int maxCommon = selectedHost.getHostPath().commonPrefix(failedHostPath).length();
            for (Host host : continuation) {
                int len = host.getHostPath().commonPrefix(failedHostPath).length();
                if (maxCommon < len) {
                    selectedHost = host;
                    maxCommon = len;
                }
            }
            logger_.debug("Selected {}:{} [{}] to continue", new Object[]{
                    selectedHost, selectedHost.getPort(), selectedHost.getHostPath()});
            try {
                RepairHandle repairHandle = getRemoteHandle(selectedHost);
                // TODO: rpc to host
            } catch (CommunicationException e) {
                if (selectedHost.getHostPath().isConjugateTo(failedHostPath)) {
                    String commonPrefix = failedHostPath.commonPrefix(selectedHost.getHostPath());
                    fixSubtree(algorithmPathExecution(
                            new PGridPath(commonPrefix)).toString(),
                            commonPrefix,
                            failedHost, selectedHost);
                } else {
                    fixNode(algorithmPathExecution(selectedHost.getHostPath()).toString(), selectedHost);
                }
            }
        }
    }


    public void fixSubtree(String footpath, String prefix, Host... failedHosts) {
        logger_.debug("Fixing subtree {}", prefix);

        for (Host failedHost : failedHosts) {
            routingTable_.removeReference(failedHost);
            if (registry_.containsHost(failedHost.getUUID())) {
                registry_.getIssue(failedHost.getHostPath().toString()).issueType = IssueType.SUBTREE;
            }
        }

        List<Host> continuation = fix_.execute(routingTable_, new PGridPath(footpath));
        PGridPath prefixPath = new PGridPath(prefix);

        if (continuation.size() == 0) {
            logger_.debug("The localhost doesn't know anyone that can fix the problem");
            // wait till timeout then keep generalizing till replacement
        } else {
            if (continuation.size() == 1) {
                Host host = continuation.get(0);
                if (routingTable_.getLocalhost().compareTo(host) == 0) {
                    logger_.debug("The localhost prepares to fix the subtree issue.");
                    // the localhost is part of the solution

                    logger_.debug("Informing the localhost's conjugate for the issue");
                    Host[] conjugateLevel = routingTable_.getLevelArray(routingTable_.levelNumber() - 1);
                    if (conjugateLevel.length > 0) {
                        Host conjugate = conjugateLevel[0];
                        try {
                            RepairHandle repairHandle = getRemoteHandle(conjugate);
                            // TODO: rpc to host
                        } catch (CommunicationException e) {
                            logger_.debug("{}:{} cannot be reached.", conjugate, conjugate.getPort());
                            fixNode(algorithmPathExecution(conjugate.getHostPath()).toString(), conjugate);
                        }
                    }
                    replace_.execute(routingTable_, prefixPath);
                } else {
                    logger_.debug("The localhost must forward a request to {}:{}", host, host.getPort());
                    try {
                        RepairHandle repairHandle = getRemoteHandle(host);
                        // TODO: rpc to host
                    } catch (CommunicationException e) {
                        logger_.debug("{}:{} is not reachable.", host, host.getPort());
                        if (host.getHostPath().isConjugateTo(prefixPath)) {
                            String commonPrefix = prefixPath.commonPrefix(host.getHostPath());
                            failedHosts = Arrays.copyOf(failedHosts, failedHosts.length + 1);
                            failedHosts[failedHosts.length - 1] = host;
                            fixSubtree(algorithmPathExecution(
                                    new PGridPath(commonPrefix)).toString(),
                                    commonPrefix,
                                    failedHosts);
                        } else {
                            fixNode(algorithmPathExecution(host.getHostPath()).toString(), host);
                        }

                    }
                }
            } else if (continuation.size() > 1) {
                logger_.debug("The localhost must forward a request for a remote algorithm execution.");
                Host selectedHost = continuation.get(0);
                int maxCommon = selectedHost.getHostPath().commonPrefix(prefixPath).length();
                for (Host host : continuation) {
                    int len = host.getHostPath().commonPrefix(prefixPath).length();
                    if (maxCommon < len) {
                        selectedHost = host;
                        maxCommon = len;
                    }
                }
                logger_.debug("Selected {}:{} [{}] to continue", new Object[]{
                        selectedHost, selectedHost.getPort(), selectedHost.getHostPath()});
                try {
                    RepairHandle repairHandle = getRemoteHandle(selectedHost);
                    // TODO: rpc to host
                } catch (CommunicationException e) {
                    if (selectedHost.getHostPath().isConjugateTo(prefixPath)) {
                        String commonPrefix = prefixPath.commonPrefix(selectedHost.getHostPath());
                        failedHosts = Arrays.copyOf(failedHosts, failedHosts.length + 1);
                        failedHosts[failedHosts.length - 1] = selectedHost;
                        fixSubtree(algorithmPathExecution(
                                new PGridPath(commonPrefix)).toString(),
                                commonPrefix,
                                failedHosts);
                    } else {
                        fixNode(algorithmPathExecution(selectedHost.getHostPath()).toString(), selectedHost);
                    }
                }
            } else {
                logger_.debug("The failed peer is either " +
                        "(a) solved and the replacer is already in the routing table or " +
                        "(b) solved but the replacer is missing cause of refMax constant.");
            }
        }
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

    public PGridPath algorithmPathExecution(PGridPath failedHostPath) {
        String root = failedHostPath.subPath(0, failedHostPath.length() - 1);
        char lastChar = failedHostPath.value(failedHostPath.length() - 1);
        PGridPath initialPath = new PGridPath(root);
        initialPath.revertAndAppend(lastChar);
        return initialPath;
    }
}
