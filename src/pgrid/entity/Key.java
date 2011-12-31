/*
 * This file (pgrid.entity.Key) is part of the libpgrid project.
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

/**
 * A key is associated with peers or resources in the pgrid network.
 *
 * @author Vourlakis Nikolas <nvourlakis@gmail.com>
 */
public interface Key extends Comparable<Key> {

    /**
     * Returns a byte sequence of the key.
     *
     * @return a byte array.
     */
    public byte[] getBytes();

    /**
     * Get the size of this key.
     *
     * @return key size.
     */
    public int size();

    /**
     * Returns a string representation of the key.
     *
     * @return the stringified key.
     */
    public String toString();
}
