/*
 * This file (pgrid.entity.clock.PeerClock) is part of the libpgrid project.
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

package pgrid.entity.clock;

/**
 * Every peer has a local PeerClock. When he is about to send a message to a
 * remote peer he must include the current timestamp contained in the PeerClock.
 * This is done to compare and update the references in his routing table.
 *
 * @author Vourlakis Nikolas <nvourlakis@gmail.com>
 */
public interface PeerClock<T extends Number> {

    /**
     * Resets the entity.clock to its initial value.
     */
    public void reset();

    /**
     * Returns the current timestamp.
     *
     * @return The current timestamp.
     */
    public T getTimestamp();

    /**
     * This method sets a new timestamp when a distributed event was triggered.
     *
     * @param timestamp This is the timestamp of the remote peer the moment it
     *                  sent the message.
     */
    public void clockedEvent(T timestamp);
}
