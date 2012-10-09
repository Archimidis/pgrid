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

import pgrid.entity.Key;
import pgrid.entity.internal.PGridKey;
import pgrid.entity.storage.FilenameHashAlgorithm;
import pgrid.entity.storage.StorageSpace;
import pgrid.utilities.ArgumentCheck;

import javax.inject.Inject;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * A simple map-like data type that stores <{@link Key}, {@link String}> pairs.
 * The string is a filename. The filename will be hashed to a Key.
 *
 * @author Vourlakis Nikolas <nvourlakis@gmail.com>
 */
public class FileStorageSpace implements StorageSpace {

    private final Map<Key, String> storage_ =
            Collections.synchronizedMap(new HashMap<Key, String>());
    private static FilenameHashAlgorithm hashing_;

    @Inject
    public FileStorageSpace(FilenameHashAlgorithm hashing) {
        ArgumentCheck.checkNotNull(hashing, "Cannot initialize a FileStorageSpace object with a null FilenameHashAlgorithm.");

        hashing_ = hashing;
    }

    @Override
    public void put(String filename) {
        ArgumentCheck.checkNotNull(filename, "A null filename was given");

        storage_.put(hashing_.produceKey(filename), filename);
    }

    @Override
    public String get(Key fileKey) {
        ArgumentCheck.checkNotNull(fileKey, "A null file key was given");

        return storage_.get(fileKey);
    }

    @Override
    public String get(String fileKeyStr) {
        ArgumentCheck.checkNotNull(fileKeyStr, "A null file key was given");

        return storage_.get(new PGridKey(fileKeyStr));
    }

    @Override
    public boolean containsFileKey(Key fileKey) {
        ArgumentCheck.checkNotNull(fileKey, "A null file key was given");

        return storage_.containsKey(fileKey);
    }

    @Override
    public boolean containsFileKey(String fileKeyStr) {
        ArgumentCheck.checkNotNull(fileKeyStr, "A null file key was given");

        return storage_.containsKey(new PGridKey(fileKeyStr));
    }

    @Override
    public boolean containsFile(String filename) {
        ArgumentCheck.checkNotNull(filename, "A null filename was given");

        return storage_.containsValue(filename);
    }

    @Override
    public boolean isEmpty() {
        return storage_.isEmpty();
    }

    @Override
    public int size() {
        return storage_.size();
    }

    @Override
    public void clear() {
        storage_.clear();
    }
}
