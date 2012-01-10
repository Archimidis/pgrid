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

package pgrid.service.exchange.spi;

import pgrid.entity.routingtable.RoutingTable;

/**
 * This is a simple class that stores information needed for exchange. Objects
 * of this class are used by {@link ExchangeAlgorithm}.
 *
 * @author Vourlakis Nikolas <nvourlakis@gmail.com>
 */
public class ExchangeContext {

    private final RoutingTable localRoutingTable_;
    private RoutingTable remoteRoutingTable_ = null;

    private boolean invited_;

    private int commonPathLength_;
    private boolean recursive_;

    private boolean readyForExchange_;

    private final int maxRef_;

    /**
     * Constructor.
     *
     * @param localRT a reference to the routing table of the local peer.
     * @param invited a flag that shows if the local peer was invited or not.
     * @param maxRef  maximum references a level in the routing table can hold.
     */
    public ExchangeContext(RoutingTable localRT, boolean invited, int maxRef) {
        if (localRT == null) {
            throw new NullPointerException("Null RoutingTable given.");
        }
        if (maxRef < 0) {
            throw new IllegalArgumentException("Negative maxRef given");
        }

        maxRef_ = maxRef;
        localRoutingTable_ = localRT;
        invited_ = invited;
        recursive_ = false;
        commonPathLength_ = 0;
        readyForExchange_ = false;
    }

    /**
     * Validates the context and sets the ready-for-exchange flag.
     */
    private void validateContext() {
        readyForExchange_ = localRoutingTable_.getLocalhost() != null
                && remoteRoutingTable_ != null
                && remoteRoutingTable_.getLocalhost() != null;
    }

    /**
     * Returns if the current context is ready for exchange.
     *
     * @return true if local and remote peer information are all set.
     */
    public boolean isReadyForExchange() {
        validateContext();
        return readyForExchange_;
    }

    /**
     * It sets information about the remote peer to be exchanged with.
     *
     * @param remoteRT the routing table of the remote peer.
     */
    public void setRemoteInfo(RoutingTable remoteRT) {
        if (remoteRT == null) {
            throw new NullPointerException("Null RoutingTable given.");
        }

        remoteRoutingTable_ = remoteRT;
        readyForExchange_ = true;
    }

    /**
     * Returns the routing table of the remote peer.
     *
     * @return the routing table.
     */
    public RoutingTable getRemoteRoutingTable() {
        return remoteRoutingTable_;
    }

    /**
     * Returns the routing table of the local peer.
     *
     * @return the routing table.
     */
    public RoutingTable getLocalRoutingTable() {
        return localRoutingTable_;
    }

    /**
     * Tells if the local peer was invited by the remote or not.
     *
     * @return true if the local peer was invited and false otherwise.
     */
    public boolean isInvited() {
        return invited_;
    }

    /**
     * It sets the common path length between the two peers. The common path
     * length starts from zero for convenience with RoutingTable schematics.
     *
     * @param length the common path length.
     */
    public void setCommonPathLength(int length) {
        commonPathLength_ = length;
    }

    /**
     * It will return the common path length between the two peers. The common
     * path length starts from zero for convenience with RoutingTable
     * schematics.
     *
     * @return the common path length.
     */
    public int getCommonPathLength() {
        return commonPathLength_;
    }

    /**
     * If the exchange algorithm ended with the case of recursion, it will
     * inform the exchange context.
     */
    public void setRecursive() {
        recursive_ = true;
    }

    /**
     * Tells if there was the recursion case during the algorithm execution.
     *
     * @return true in case of recursion.
     */
    public boolean isRecursive() {
        return recursive_;
    }

    /**
     * Returns the maximum references the routing table can store in a level.
     *
     * @return the refMax constant.
     */
    public int getRefMax() {
        return maxRef_;
    }
}
