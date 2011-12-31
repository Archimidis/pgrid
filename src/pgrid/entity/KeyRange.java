/*
 * This file (pgrid.entity.KeyRange) is part of the libpgrid project.
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
 * This is a part of the key space.
 *
 * @author Vourlakis Nikolas <nvourlakis@gmail.com>
 */
public interface KeyRange {

    /**
     * Returns the minimum value of this range.
     *
     * @return the minimum value.
     */
    public Key getMin();

    /**
     * Returns the maximum value of this range.
     *
     * @return the maximum value.
     */
    public Key getMax();

    /**
     * Checks if the given key belongs to this key range.
     *
     * @param key to be checked.
     * @return true if the key is in the range, else false.
     */
    public boolean contains(Key key);
}
