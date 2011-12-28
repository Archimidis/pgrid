/*
 * This file (entity.EntityFactory) is part of the libpgrid project.
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

import com.google.inject.assistedinject.Assisted;

import java.net.InetAddress;
import java.net.UnknownHostException;

/**
 * Factory methods to create user assisted entity objects.
 *
 * @author Vourlakis Nikolas
 */
public interface EntityFactory {

    /**
     * Creates and returns a new {@link Host} object.
     *
     * @param ip the IP address of the host.
     * @param port the port of the host.
     * @return a fully constructed Host implementation object.
     * @throws UnknownHostException if the supplied ip cannot be resolved.
     * @throws IllegalArgumentException in case of an invalid port.
     */
    public Host newHost(String ip, int port) throws UnknownHostException, IllegalArgumentException;

    /**
     * Creates and returns a new {@link Host} object.
     *
     * @param ip the IP address of the host.
     * @param port the port of the host.
     * @return a fully constructed Host implementation object.
     * @throws IllegalArgumentException in case of an invalid port.
     */
    public Host newHost(InetAddress ip, int port) throws IllegalArgumentException;

    /**
     * Creates and returns a new {@link Key} object.
     *
     * @param key the key to be stored.
     * @return a fully constructed Key implementation object.
     * @throws NullPointerException if the supplied string is null.
     */
    public Key newKey(String key) throws NullPointerException;

    /**
     * Creates and returns a new {@link KeyRange} object.
     *
     * @param minKey the minimum key where the range starts from.
     * @param maxKey the maximum key where the range ends.
     * @return a fully constructed KeyRange implementation object.
     * @throws NullPointerException if the supplied strings are null.
     */
    public KeyRange newKeyRange(@Assisted("minKey") Key minKey, @Assisted("maxKey") Key maxKey)
            throws NullPointerException;
}
