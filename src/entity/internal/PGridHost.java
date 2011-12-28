/*
 * This file (entity.internal.PGridHost) is part of the libpgrid project.
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

package entity.internal;

import com.google.inject.assistedinject.Assisted;
import com.google.inject.assistedinject.AssistedInject;
import entity.Host;
import entity.Key;
import entity.KeyRange;
import entity.PGridPath;
import entity.clock.LamportClock;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.UUID;

/**
 * @author Vourlakis Nikolas <nvourlakis@gmail.com>
 */
public class PGridHost implements Host {

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
     * @param ip   the IP address of the host.
     * @param port the port of the host.
     * @throws UnknownHostException     if the given IP could not be determined.
     * @throws IllegalArgumentException in case the port argument is negative.
     */
    @AssistedInject
    public PGridHost(@Assisted String ip, @Assisted int port) throws UnknownHostException {
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
     * @param ip   the IP address of the host encapsulated in an InetAddress.
     * @param port the port of the host.
     * @throws IllegalArgumentException in case the port argument is negative.
     */
    @AssistedInject
    public PGridHost(@Assisted InetAddress ip, @Assisted int port) {
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
    @Override
    public void setHostPath(String path) {
        path_.setPath(path);
    }

    /**
     * @return the path of this host.
     */
    // FIXME: getHostPath() is redundant
    @Override
    public PGridPath getHostPath() {
        return path_;
    }


    @Override
    public long getTimestamp() {
        return clock_.getTimestamp();
    }


    @Override
    public void setTimestamp(long timestamp) {
        clock_.clockedEvent(timestamp);
    }


    @Override
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
        KeyRange range = new PGridKeyRange(path_.toKey(), path_.toKey());
        return range.contains(key);
    }

    @Override
    public void setUUID(UUID uuid) {
        uuid_ = uuid;
    }

    @Override
    public UUID getUUID() {
        return uuid_;
    }


    @Override
    public void setAddress(InetAddress address) {
        address_ = address;
    }

    @Override
    public InetAddress getAddress() {
        return address_;
    }


    @Override
    public void setPort(int port) {
        port_ = port;
    }

    @Override
    public int getPort() {
        return port_;
    }

    /**
     * The comparison is done based on the UUIDs of the hosts.
     */
    @Override
    public int compareTo(PGridHost host) {
        return getUUID().compareTo(host.getUUID());
    }

    @Override
    public boolean equals(Object o) {
        if (o == null)
            return false;

        if (!o.getClass().equals(this.getClass()))
            return false;

        return getUUID().equals(((PGridHost) o).getUUID());
    }

    @Override
    public int hashCode() {
        assert false : "hashCode not designed";
        return 42; // the answer to all.
    }


    @Override
    public boolean isExchangeTime() {
        return (exchangeTime_ < System.currentTimeMillis());
    }


    @Override
    public void exchangeInitiated() {
        exchangeTime_ = System.currentTimeMillis() + MIN_EXCHANGE_DELAY / 2;
    }


    @Override
    public void exchanged() {
        exchangeTime_ = System.currentTimeMillis() + MIN_EXCHANGE_DELAY;
    }
}
