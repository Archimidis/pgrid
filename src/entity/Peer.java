/*
 * This file (entity.Peer) is part of the libpgrid project.
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

package entity;

import java.net.InetAddress;
import java.util.UUID;

/**
 * A peer is identified by an address, a port and a unique id. It is also
 * responsible for a certain range of keys.
 *
 * @author Vourlakis Nikolas <nvourlakis@gmail.com>
 */
public interface Peer {

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
     * Returns the UUID of the peer.
     *
     * @return the peer UUID.
     */
    public UUID getUUID();

    /**
     * Returns the ip address of the peer.
     *
     * @return the peer ip.
     */
    public InetAddress getAddress();

    /**
     * Returns the port that peer is running on.
     *
     * @return the peer port.
     */
    public int getPort();
}
