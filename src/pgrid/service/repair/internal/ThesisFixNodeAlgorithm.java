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

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import pgrid.entity.Host;
import pgrid.entity.PGridPath;
import pgrid.entity.routingtable.RoutingTable;
import pgrid.service.repair.spi.FixNodeAlgorithm;

import java.util.Collection;

/**
 * <h1>The implementation of the Thesis fault tolerant protocol.</h1>
 * <p/>
 * The idea is to find a host that has zero or less than one host reference at
 * its conjugate tree according to its full path. To find this host, the
 * algorithm starting from the conjugate path of the failed host, will move to
 * longer paths. At a given path, it will choose populated subtrees if their
 * conjugate tree contains only one host. In case of two populated subtrees it
 * will take the right by default.
 * <p/>
 * <b><i>Example:</i></b> We have a failed peer associated with the path "01".
 * <br/>
 * In the <b>first iteration</b>, the algorithm will start searching from path
 * "00". The subtree for path "000" contains only one host and that for path
 * "001" contains two hosts. The algorithm must proceed with path "001". If the
 * local host knows a better host that can continue, then it will be returned.
 * It is now the responsibility of that host to continue the algorithm
 * execution. Else the local host will continue recursively. The algorithm in
 * both cases will finish to the host responsible for path "0011".
 * <p/>
 * In the <b>second iteration</b>, it will start searching from path "0011".
 * Obviously the two hosts are in paths "0010" and "0011", meaning that the
 * algorithm has reached a subtree in the pgrid structure containing only one
 * host and its conjugate. The host at path "0011" wil continue executing the
 * algorithm and will return himself as capable of fixing the issue.
 *
 * @author Vourlakis Nikolas
 */
public class ThesisFixNodeAlgorithm implements FixNodeAlgorithm {

    private static final Logger logger_ = LoggerFactory.getLogger(ThesisFixNodeAlgorithm.class);

    @Override
    public Host execute(RoutingTable routingTable, Host failed, PGridPath path) {
        if (routingTable == null) {
            throw new NullPointerException("Null routing table was given.");
        }
        if (path == null) {
            throw new NullPointerException("Null path was given");
        }

        logger_.info("Current path trace {}", path);
        Host localhost = routingTable.getLocalhost();
        PGridPath localhostPath = localhost.getHostPath();

        if (localhostPath.commonPrefix(path).length() <= 0) {
            logger_.info("[Pre] Not in prefix relation, return a host closest to the failed path");
            return closestHost(routingTable, failed, path);
        }

        if (localhostPath.toString().compareTo(path.toString()) == 0) {
            logger_.info("[Pre] The local host will be part of the solution");
            return localhost;
        }

        int nextLevel = path.length(); // routing table starts from index '0'
        if (localhostPath.length() < nextLevel + 1) { // PGridPath starts from index '1'
            logger_.info("[Pre] Smaller routing table, return a host closest to the failed path length");
            return closestHost(routingTable, failed, path);
        }

        char last = path.value(path.length() - 1);
        PGridPath case1 = new PGridPath(path.toString());
        case1.revertAndAppend(last);

        PGridPath case2 = new PGridPath(path.toString());
        case2.append(last);
        logger_.info("Checking {} and {}", case1, case2);
        int diff = routingTable.levelNumber() - nextLevel;

        if (localhostPath.hasPrefix(case1)) {
            logger_.info("[Case 1] Local host host in prefix relation with {}", case1);
            if ((routingTable.levelNumber() - case1.length()) <= 1) {
                if (localhostPath.toString().compareTo(case1.toString()) == 0) {
                    logger_.info("[Case 1] The local host will be part of the solution");
                    return localhost;
                } else {
                    logger_.info("[Case 1] Proceeding to path {}", case1);
                    return execute(routingTable, failed, case1);
                }
            } else {
                logger_.info("[Case 1] Return the host level in prefix relation to path {}", case2);
                return closestHost(routingTable, failed, case2);
            }
        } else if (localhostPath.hasPrefix(case2)) {
            logger_.info("[Case 2] Local host in prefix relation with {}", case2);
            if (localhostPath.toString().compareTo(case2.toString()) == 0) {
                if (diff == 1 && routingTable.getLevel(nextLevel).size() <= 1) {
                    logger_.info("[Case 2] The local host will be part of the solution");
                    return localhost;
                } else {
                    logger_.info("[Case 2] Return the host level in prefix relation to path {}", case1);
                    return closestHost(routingTable, failed, case1);
                }
            } else {
                logger_.info("[Case 2] Proceeding to path {}", case2);
                return execute(routingTable, failed, case2);
            }
        }
        return closestHost(routingTable, failed, path); // if everything fails
    }

    private Host closestHost(RoutingTable routingTable, Host failed, PGridPath path) {
        Collection<Host> hosts = routingTable.closestHosts(path.toString());
        int maxLen = 0;
        Host result = null;
        for (Host host : hosts) {
            if (host.compareTo(failed) == 0) {
                continue;
            }
            int currentLength = host.getHostPath().commonPrefix(path).length();
            if (maxLen < currentLength) {
                maxLen = currentLength;
                result = host;
            }
        }
        return result;
    }
}
