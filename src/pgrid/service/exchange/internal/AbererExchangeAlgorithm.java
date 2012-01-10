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

package pgrid.service.exchange.internal;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import pgrid.entity.Host;
import pgrid.entity.PGridPath;
import pgrid.entity.routingtable.RoutingTable;
import pgrid.service.exchange.spi.ExchangeAlgorithm;
import pgrid.service.exchange.spi.ExchangeContext;

/**
 * This implementation encapsulates the exchange algorithm exactly as
 * described in the initial paper of P-Grid by Karl Aberer. The only precondition
 * for this class to execute properly is an {@link ExchangeContext} with
 * information about local and remote peer correctly initialized. That is the
 * method {@link ExchangeContext#isReadyForExchange()} must return true.
 *
 * @author Vourlakis Nikolas
 */
public class AbererExchangeAlgorithm implements ExchangeAlgorithm {

    private static final Logger logger_ = LoggerFactory.getLogger(AbererExchangeAlgorithm.class);

    public void execute(ExchangeContext context) {
        if (context == null) {
            throw new NullPointerException("ExchangeContext cannot by null");
        }
        if (!context.isReadyForExchange()) {
            throw new IllegalStateException("The ExchangeContext object is not properly initialized.");
        }

        final RoutingTable localRoutingTable = context.getLocalRoutingTable();

        final Host remoteHost = context.getRemoteRoutingTable().getLocalhost();
        final Host localHost = localRoutingTable.getLocalhost();

        logger_.info("Exchange algorithm beginning between local peer {} and remote peer {}",
                localHost, remoteHost);
        logger_.info("Local peer path: {} && Remote peer path: {}",
                localHost.getHostPath(), remoteHost.getHostPath());

        String commonPath = localHost.getHostPath().commonPrefix(remoteHost.getHostPath());
        int commonLength = commonPath.length();

        logger_.debug("Common path is {} with length {}", commonPath, commonLength);

        commonLength--; // sub for convenience

        if (commonLength < 0) {
            context.setCommonPathLength(commonLength + 1);
        } else {
            context.setCommonPathLength(commonLength);
        }

        // Exchanging references at the level where the paths agree.
        // Does not executes for the peers that are bootstrapping.
        localRoutingTable.update(context.getRemoteRoutingTable(), commonLength, context.getRefMax());

        PGridPath localPath = localHost.getHostPath();
        PGridPath remotePath = remoteHost.getHostPath();
        int l1 = localPath.subPath(commonLength + 1, localPath.length()).length();
        int l2 = remotePath.subPath(commonLength + 1, remotePath.length()).length();

        if (l1 == 0 && l2 == 0) {
            // Case 1: if both remaining paths are empty introduce a new level.
            logger_.info("Case 1: (l1 == 0 && l2 == 0)");
            localPath.append(context.isInvited() ? '1' : '0');
            logger_.info("Appending {} to routing table.", remoteHost);
            localRoutingTable.addReference(commonLength + 1, remoteHost);
        } else if (l1 == 0 && l2 > 0) {
            // Case 2: if one remaining path is empty split the shorter path.
            // When one of the peers is in this case, the other is in CASE *3*.
            logger_.info("Case 2: (l1 == 0 && l2 > 0)");
            localPath.revertAndAppend(remotePath.value(commonLength + 1));
            logger_.info("Appending {} to routing table.", remoteHost);
            localRoutingTable.addReference(commonLength + 1, remoteHost);
        } else if (l1 > 0 && l2 == 0) {
            // Case 3: if one remaining path is empty split the shorter path.
            // When one of the peers is in this case, the other is in CASE *2*.
            logger_.info("Case 3: (l1 > 0 && l2 == 0)");
            localRoutingTable.updateLevelRandomly(commonLength + 1, remoteHost, context.getRefMax());
        } else if (l1 > 0 && l2 > 0) {
            // Case 4: recursively perform exchange with referenced peers.
            logger_.info("Case 4: (l1 > 0 && l2 > 0)");
            context.setRecursive();
        }

        logger_.info("[RESULT] Final path of local peer: {}", localPath);

    }
}
