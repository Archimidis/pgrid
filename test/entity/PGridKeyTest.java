/*
 * This file (entity.PGridKeyTest) is part of the libpgrid project.
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

import entity.internal.PGridKey;
import org.junit.Assert;
import org.junit.Test;

/**
 * @author Vourlakis Nikolas <nvourlakis@gmail.com>
 */
public class PGridKeyTest {
    @Test
    public void WhenComparingKeys_ExpectResults() {
        Key key1 = new PGridKey("");
        Key key2 = new PGridKey("");
        Assert.assertTrue(key1.compareTo(key2) == 0);

        key1 = new PGridKey("");
        key2 = new PGridKey("00");
        Assert.assertTrue(key1.compareTo(key2) == 1);

        key1 = new PGridKey("11");
        key2 = new PGridKey("");
        Assert.assertTrue(key1.compareTo(key2) == -1);

        key1 = new PGridKey("0000");
        key2 = new PGridKey("000011111");

        // in prefix relation
        Assert.assertTrue(key1.compareTo(key2) == 0);

        Key in = new PGridKey("000001");
        // key is in the range [key1, key2]
        Assert.assertTrue(in.compareTo(key1) >= 0 && in.compareTo(key2) <= 0);

    }

    @Test(expected = NullPointerException.class)
    public void WhenComparingNullKey_ExpectNullPointerException() {
        Key key1 = new PGridKey("");
        key1.compareTo(null);
    }

    @Test(expected = NullPointerException.class)
    public void WhenConstructingWithNullKeys_ExpectNullPointerException() {
        new PGridKey(null);
    }
}
