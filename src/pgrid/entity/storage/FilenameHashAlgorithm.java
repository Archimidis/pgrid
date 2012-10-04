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

package pgrid.entity.storage;

import pgrid.entity.Key;

/**
 * @author Vourlakis Nikolas <nvourlakis@gmail.com>
 */
public interface FilenameHashAlgorithm {

    /**
     * Given a string value it produces a binary hash and then constructs a
     * {@link Key} object and returns it.
     *
     * @param value to be hashed.
     * @return a fully constructed {@link Key} object.
     */
    public Key produceKey(String value);
}
