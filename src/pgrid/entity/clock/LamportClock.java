/*
 * This file (pgrid.entity.clock.LamportClock) is part of the libpgrid project.
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

package pgrid.entity.clock;

import java.util.concurrent.atomic.AtomicLong;

/**
 * LamportClock is an implementation of the Lamport clocks as described in his
 * paper.
 *
 * @author Vourlakis Nikolas <nvourlakis@gmail.com>
 */
public final class LamportClock implements PeerClock<Long> {

    private final AtomicLong timestamp_;

    public LamportClock() {
        timestamp_ = new AtomicLong(0);
    }

    /**
     * Resets the entity.clock to its initial value '0'.
     */
    @Override
    public void reset() {
        timestamp_.set(0);
    }

    /**
     * Returns the current timestamp.
     *
     * @return The current timestamp.
     */
    @Override
    public Long getTimestamp() {
        return timestamp_.get();
    }

    /**
     * When a message is received we must increase the local timestamp and then
     * compare it to the remote. Finally, choose the maximum one and set it to
     * local entity.clock.
     *
     * @param timestamp This is the timestamp of the remote peer the moment it
     *                  sent the message.
     */
    @Override
    public void clockedEvent(Long timestamp) {
        Long next = Math.max(timestamp, timestamp_.get());
        next++;
        timestamp_.set(next);
    }
}
