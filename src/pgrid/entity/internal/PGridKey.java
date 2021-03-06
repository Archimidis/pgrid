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

package pgrid.entity.internal;

import com.google.inject.assistedinject.Assisted;
import com.google.inject.assistedinject.AssistedInject;
import pgrid.entity.Key;

/**
 * @author Vourlakis Nikolas <nvourlakis@gmail.com>
 */
public class PGridKey implements Key {

    private String key_;

    @AssistedInject
    public PGridKey(@Assisted String key) {
        if (key == null) {
            throw new NullPointerException(
                    "PGridKey needs not null values to instantiate");
        }

        key_ = key;
    }

    @Override
    public byte[] getBytes() {
        return key_.getBytes();
    }

    @Override
    public int size() {
        return key_.length();
    }

    @Override
    public String toString() {
        return key_;
    }

    @Override
    public int compareTo(Key key) {
        if (key == null) {
            throw new NullPointerException();
        }

        String otherKey = key.toString();
        if (key_.equals("") && otherKey.equals("")) {
            return 0;
        } else if (key_.equals("") && !otherKey.equals("")) {
            return 1;
        } else if (!key_.equals("") && otherKey.equals("")) {
            return -1;
        }

        int length = Math.min(key_.length(), otherKey.length());
        return Long.valueOf(key_.substring(0, length), 2)
                .compareTo(Long.valueOf(otherKey.substring(0, length), 2));
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (!(obj instanceof Key)) {
            return false;
        }

        return this.compareTo((Key) obj) == 0;
    }

    @Override
    public int hashCode() {
        assert false : "hashCode not implemented yet";
        return 42;
    }
}
