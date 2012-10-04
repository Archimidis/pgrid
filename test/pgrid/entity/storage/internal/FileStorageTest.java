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
import pgrid.entity.storage.FilenameHashAlgorithm;
import pgrid.entity.storage.Storage;
import pgrid.entity.storage.internal.BinaryFilenameHashing;
import pgrid.entity.storage.internal.FileStorage;

/**
 * @author Vourlakis Nikolas <nvourlakis@gmail.com>
 */
public class FileStorageTest {

    private static FilenameHashAlgorithm hashing_;

    @BeforeClass
    public static void beforeClass() {
        hashing_ = new BinaryFilenameHashing();
    }

    @Test(expected = NullPointerException.class)
    public void WhenPutNullFilename_ExpectNullPointerException() {
        Storage storage = new FileStorage(hashing_);
        storage.put(null);
    }

    @Test(expected = NullPointerException.class)
    public void WhenGetNullFileKey_ExpectNullPointerException() {
        Storage storage = new FileStorage(hashing_);
        Key key = null;
        storage.get(key);
    }

    @Test(expected = NullPointerException.class)
    public void WhenGetNullFileKeyStr_ExpectNullPointerException() {
        Storage storage = new FileStorage(hashing_);
        String key = null;
        storage.get(key);
    }

    @Test(expected = NullPointerException.class)
    public void WhenAskingWithNullFileKey_ExpectNullPointerException() {
        Storage storage = new FileStorage(hashing_);
        Key key = null;
        storage.containsFileKey(key);
    }

    @Test(expected = NullPointerException.class)
    public void WhenAskingWithNullFileKeyStr_ExpectNullPointerException() {
        Storage storage = new FileStorage(hashing_);
        String key = null;
        storage.containsFileKey(key);
    }

    @Test(expected = NullPointerException.class)
    public void WhenAskingWithNullFilename_ExpectNullPointerException() {
        Storage storage = new FileStorage(hashing_);
        storage.containsFile(null);
    }

    @Test
    public void WhenPutFilename_ExpectToBeContained() {
        FileStorage storage = new FileStorage(hashing_);
        String filename = "Here I come!";
        Key key = hashing_.produceKey(filename);
        storage.put(filename);

        Assert.assertFalse(storage.isEmpty());
        Assert.assertTrue(storage.size() == 1);
        Assert.assertTrue(storage.containsFile(filename));
        Assert.assertTrue(storage.containsFileKey(key));
        Assert.assertTrue(storage.containsFileKey(key.toString()));
        Assert.assertTrue(storage.get(key).compareTo(filename) == 0);
        Assert.assertTrue(storage.get(key.toString()).compareTo(filename) == 0);

        storage.clear();
        Assert.assertTrue(storage.isEmpty());
        Assert.assertTrue(storage.size() == 0);
        Assert.assertFalse(storage.containsFile(filename));
        Assert.assertFalse(storage.containsFileKey(key));
        Assert.assertFalse(storage.containsFileKey(key.toString()));

    }
}
