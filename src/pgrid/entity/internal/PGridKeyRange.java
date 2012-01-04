/*
 * This file (pgrid.entity.internal.PGridKeyRange) is part of the libpgrid project.
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

package pgrid.entity.internal;

import com.google.inject.assistedinject.Assisted;
import com.google.inject.assistedinject.AssistedInject;
import pgrid.entity.Key;
import pgrid.entity.KeyRange;

/**
 * @author Vourlakis Nikolas <nvourlakis@gmail.com>
 */
public class PGridKeyRange implements KeyRange {

    private Key minKey_;
    private Key maxKey_;

    @AssistedInject
    public PGridKeyRange(@Assisted("minKey") Key minKey, @Assisted("maxKey") Key maxKey) {
        if (minKey == null || maxKey == null) {
            throw new NullPointerException(
                    "PGridKeyRange needs not null values to instantiate");
        }

        minKey_ = minKey;
        maxKey_ = maxKey;
    }

    @Override
    public Key getMin() {
        return minKey_;
    }

    @Override
    public Key getMax() {
        return maxKey_;
    }

    @Override
    public boolean contains(Key key) {
        if (key == null) {
            throw new NullPointerException();
        }

        return ((minKey_.compareTo(key) <= 0) && (maxKey_.compareTo(key) >= 0));
    }
}
