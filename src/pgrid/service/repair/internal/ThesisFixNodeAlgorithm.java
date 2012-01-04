/*
 * This file (pgrid.service.repair.internal.DefaultFixNodeAlgorithm) is part of the libpgrid project.
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

/**
 * @author Vourlakis Nikolas
 */
public class ThesisFixNodeAlgorithm implements FixNodeAlgorithm {

    private static final Logger logger_ = LoggerFactory.getLogger(ThesisFixNodeAlgorithm.class);

    private final RoutingTable localRT_;

    public ThesisFixNodeAlgorithm(RoutingTable localRT) {
        localRT_ = localRT;
    }

    @Override
    public void execute(Host failed, PGridPath path) {
        if (failed == null) {
            throw new NullPointerException("Null host was given.");
        }
        if (path == null) {
            throw new NullPointerException("Null path was given");
        }

        Host localhost = localRT_.getLocalhost();
        PGridPath localhostPath = localhost.getHostPath();

        if (localhostPath.commonPrefix(path).length() <= 0) {
            logger_.info("[Pre] Not in prefix relation, return a host closest to the failed path");
            return;
        }

        if (localhostPath.toString().compareTo(path.toString()) == 0) {
            logger_.info("[Pre] The local host will be part of the solution");
            // [Replace] return the localhost
            return;
        }

        int nextLevel = path.length(); // routing table starts from index '0'
        if (localhostPath.length() < nextLevel + 1) { // PGridPath starts from index '1'
            logger_.info("[Pre] Smaller routing table, return a host closest to the failed path length");
            return;
        }

        char last = path.value(path.length() - 1);
        PGridPath case1 = new PGridPath(path.toString());
        case1.revertAndAppend(last);

        PGridPath case2 = new PGridPath(path.toString());
        case2.append(last);

        int diff = localRT_.levelNumber() - nextLevel;
        if (localhostPath.hasPrefix(case1)) {
            if (diff == 1) {
                if (localhostPath.toString().compareTo(case1.toString()) == 0) {
                    logger_.info("[Case 1] The local host will be part of the solution");
                } else {
                    logger_.info("[Case 1] Proceeding to path {}", case1);
                    execute(failed, case1); // XXX: alternative to recursion?
                }
            } else {
                logger_.info("[Case 1] Return the host level in prefix relation to path");
            }
        } else if (localhostPath.hasPrefix(case2)) {
            if (localhostPath.toString().compareTo(case2.toString()) == 0) {
                if (diff == 1) {
                    logger_.info("[Case 2] The local host will be part of the solution");
                } else {
                    logger_.info("[Case 2] Return the host level in prefix relation to path");
                }
            } else {
                logger_.info("[Case 2] Proceeding to path {}", case2);
                execute(failed, case2); // XXX: alternative to recursion?
            }
        }
    }
}
