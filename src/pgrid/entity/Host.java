/*
 * This file (pgrid.entity.Host) is part of the libpgrid project.
 *
 * Copyright (c) 2011. Vourlakis Nikolas. All rights reserved.
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

package pgrid.entity;

import java.net.InetAddress;
import java.util.UUID;

/**
 * A peer is identified by an address, a port and a unique id. It is also
 * responsible for a certain range of keys.
 *
 * @author Vourlakis Nikolas <nvourlakis@gmail.com>
 */
public interface Host extends Comparable<Host> {

    /**
     * Updates the path of this host.
     *
     * @param path to be set.
     */
    public void setHostPath(String path);

    /**
     * Returns the path of this host.
     *
     * @return the path.
     */
    public PGridPath getHostPath();

    /**
     * The timestamp when compared to the clock of the local peer, reveals how
     * synchronized and updated the information stored here is. If it's old
     * then some steps must be followed to update it.
     *
     * @return a timestamp
     */
    public long getTimestamp();

    /**
     * Updates the timestamp of this host. This means that the local peer has
     * new information about this peer obtained through messaging.
     *
     * @param timestamp that will update the old one.
     */
    public void setTimestamp(long timestamp);

    /**
     * Resets the timestamp for this host.
     */
    public void resetTimestamp();

    /**
     * Returns the key range that the peer is responsible for.
     *
     * @return the key range.
     */
    public KeyRange getKeyRange();

    /**
     * Checks if the peer is responsible for the given key.
     *
     * @param key to be checked.
     * @return true if the peer is responsible, else false.
     */
    public boolean isResponsible(Key key);

    /**
     * Sets the UUID for this host.
     *
     * @param uuid the new UUID.
     */
    public void setUUID(UUID uuid);

    /**
     * Returns the UUID of the peer.
     *
     * @return the peer UUID.
     */
    public UUID getUUID();

    /**
     * Sets a new address for this host.
     *
     * @param address the new address of this host.
     */
    public void setAddress(InetAddress address);

    /**
     * Returns the ip address of the peer.
     *
     * @return the peer ip.
     */
    public InetAddress getAddress();

    /**
     * Updates the port of this host.
     *
     * @param port the new port of this host.
     */
    public void setPort(int port);

    /**
     * Returns the port that peer is running on.
     *
     * @return the peer port.
     */
    public int getPort();

    /**
     * Checks if this host must be exchanged.
     *
     * @return true if the time for an exchange has come, else false.
     */
    public boolean isExchangeTime();

    /**
     * This method is called when an exchange has initiated for this host to
     * mark this process.
     */
    public void exchangeInitiated();

    /**
     * Indicates that this host just finished exchanging.
     */
    public void exchanged();
}
