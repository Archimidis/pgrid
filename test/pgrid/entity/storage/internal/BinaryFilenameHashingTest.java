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

package pgrid.entity.storage.internal;

import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;
import pgrid.entity.Key;
import pgrid.entity.internal.PGridKey;
import pgrid.entity.storage.FilenameHashAlgorithm;

/**
 * @author Vourlakis Nikolas <nvourlakis@gmail.com>
 */
public class BinaryFilenameHashingTest {

    private static FilenameHashAlgorithm hashing_;

    @BeforeClass
    public static void beforeClass() {
        hashing_ = new BinaryFilenameHashing();
    }

    @Test(expected = NullPointerException.class)
    public void WhenHashingNullValue_ExpectNullPointerException() {
        hashing_.produceKey(null);
    }

    @Test
    public void WhenHashingValue_ExpectValidKey() {
        // The 64 first bits of the SHA-1 of the string "0" is:
        // "101101100101100010011111110001101010101100001101110010000010110"
        String value = "0";
        Key expectedKey = new PGridKey("101101100101100010011111110001101010101100001101110010000010110");
        Key key = hashing_.produceKey(value);
        Assert.assertTrue(key.size() > 0);
        Assert.assertTrue(key.equals(expectedKey));
    }
}
