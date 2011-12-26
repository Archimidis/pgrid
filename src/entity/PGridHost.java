/*
 * This file (entity.PGridHost) is part of the libpgrid project.
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

import entity.clock.LamportClock;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.UUID;

/**
 * @author Vourlakis Nikolas <nvourlakis@gmail.com>
 */
public class PGridHost implements Peer, Comparable<PGridHost> {

    private InetAddress address_;
    private int port_;
    private LamportClock clock_;
    private UUID uuid_;
    private final PGridPath path_ = new PGridPath();

    private static final int MIN_EXCHANGE_DELAY = 1000 * 60;
    private long exchangeTime_ = 0;

    /**
     * Constructs a PGridHost.
     *
     * @param ip   The IP address of the host.
     * @param port The port of the host.
     * @throws UnknownHostException     if the given IP could not be determined.
     * @throws IllegalArgumentException in case the port argument is negative.
     */
    public PGridHost(String ip, int port) throws UnknownHostException {
        if (port <= 0) {
            throw new IllegalArgumentException(
                    "Not a valid port (port = " + port + ")");
        }

        address_ = InetAddress.getByName(ip);
        init(port);
    }

    /**
     * Constructs a PGridHost.
     *
     * @param ip   The IP address of the host encapsulated in an InetAddress.
     * @param port The port of the host.
     * @throws IllegalArgumentException in case the port argument is negative.
     */
    public PGridHost(InetAddress ip, int port) {
        if (port <= 0) {
            throw new IllegalArgumentException(
                    "Not a valid port (port = " + port + ")");
        }

        address_ = ip;
        init(port);
    }

    private void init(int port) {
        port_ = port;
        clock_ = new LamportClock();
        uuid_ = UUID.randomUUID();
    }

    /**
     * Convert this PGridHost to a String. It returns a string of the form
     * "IP:port".
     *
     * @return a string representation of this host.
     */
    @Override
    public String toString() {
        return address_.getHostName();//address_.getHostAddress() + ":" + port_;
    }

    // FIXME: setHostPath(String path) is redundant
    public void setHostPath(String path) {
        path_.setPath(path);
    }

    /**
     * @return the path of this host.
     */
    // FIXME: getHostPath() is redundant
    public PGridPath getHostPath() {
        return path_;
    }

    /**
     * The timestamp when compared to the entity.clock of the local peer, reveals how
     * synchronized and updated the information stored here is. If it's old
     * then some steps must be followed to update it.
     *
     * @return a timestamp
     */
    public long getTimestamp() {
        return clock_.getTimestamp();
    }

    /**
     * Updates the timestamp of this host. This means that the local peer has
     * new information about this peer obtained through messaging.
     *
     * @param timestamp that will update the old one.
     */
    public void setTimestamp(long timestamp) {
        clock_.clockedEvent(timestamp);
    }

    /**
     * Resets the timestamp for this host.
     *
     * @see LamportClock
     */
    public void resetTimestamp() {
        clock_.reset();
    }

    @Override
    public KeyRange getKeyRange() {
        // The host is responsible for a specific key. The following return
        // has the meaning of a range with min value the path of the host and
        // maximum value all the keys that have common prefix the min value.
        return new PGridKeyRange(
                new PGridKey(path_.toString()),
                new PGridKey(path_.toString()));
    }

    @Override
    public boolean isResponsible(Key key) {
        PGridKeyRange range = new PGridKeyRange(path_.toKey(), path_.toKey());
        return range.contains(key);
    }

    /**
     * Sets the UUID for this host.
     *
     * @param uuid the new UUID.
     */
    public void setUUID(UUID uuid) {
        uuid_ = uuid;
    }

    @Override
    public UUID getUUID() {
        return uuid_;
    }

    /**
     * Sets a new address for this host.
     *
     * @param address the new address of this host.
     */
    public void setAddress(InetAddress address) {
        address_ = address;
    }

    @Override
    public InetAddress getAddress() {
        return address_;
    }

    /**
     * Updates the port of this host.
     *
     * @param port the new port of this host.
     */
    public void setPort(int port) {
        port_ = port;
    }

    @Override
    public int getPort() {
        return port_;
    }

    /**
     * {@inheritDoc}
     * The comparison is done based on the UUIDs of the hosts.
     */
    @Override
    public int compareTo(PGridHost host) {
        return getUUID().compareTo(host.getUUID());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean equals(Object o) {
        if (o == null)
            return false;

        if (!o.getClass().equals(this.getClass()))
            return false;

        return getUUID().equals(((PGridHost) o).getUUID());
    }

    /**
     * {@inheritDoc}
     */
    public int hashCode() {
        assert false : "hashCode not designed";
        return 42; // the answer to all.
    }

    /**
     * Checks if this host must be exchanged.
     *
     * @return true if the time for an exchange has come, else false.
     */
    public boolean isExchangeTime() {
        return (exchangeTime_ < System.currentTimeMillis());
    }

    /**
     * This method is called when an exchange has initiated for this host to
     * mark this process.
     */
    public void exchangeInitiated() {
        exchangeTime_ = System.currentTimeMillis() + MIN_EXCHANGE_DELAY / 2;
    }

    /**
     * Indicates that this host just finished exchanging.
     */
    public void exchanged() {
        exchangeTime_ = System.currentTimeMillis() + MIN_EXCHANGE_DELAY;
    }
}
