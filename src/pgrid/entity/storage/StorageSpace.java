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
public interface StorageSpace {
    /**
     * Stores the given filename.
     *
     * @param filename to be stored.
     * @throws NullPointerException if a null filename is given.
     */
    public void put(String filename);

    /**
     * Returns the filename associated with the given key if it exists.
     *
     * @param fileKey the key to be searched for.
     * @return the filename.
     * @throws NullPointerException if a null fileKey is given.
     */
    public String get(Key fileKey);

    /**
     * Returns the filename associated with the given key if it exists.
     *
     * @param fileKeyStr the key in string format to be searched for.
     * @return the filename.
     * @throws NullPointerException if a null fileKeyStr is given.
     */
    public String get(String fileKeyStr);

    /**
     * Returns true if a filename is associated with the given key, else it
     * returns false.
     *
     * @param fileKey the key to be searched for.
     * @return true if the storage contains the given key.
     * @throws NullPointerException if a null fileKey is given.
     */
    public boolean containsFileKey(Key fileKey);

    /**
     * Returns true if a filename is associated with the given key, else it
     * returns false.
     *
     * @param fileKeyStr the key in string format to be searched for.
     * @return true if the storage contains the given key.
     * @throws NullPointerException if a null fileKeyStr is given.
     */
    public boolean containsFileKey(String fileKeyStr);

    /**
     * Returns true if the given filename exists in the storage, else it
     * returns false.
     *
     * @param filename to be searched for.
     * @return true if the storage contains the given filename.
     * @throws NullPointerException if a null filename is given.
     */
    public boolean containsFile(String filename);

    /**
     * Returns true if the storage does not contains any key-filename
     * association.
     *
     * @return true if empty, else false.
     */
    public boolean isEmpty();

    /**
     * Returns the numbers of key-filename associations in this storage.
     *
     * @return the number of key-filename.
     */
    public int size();

    /**
     * Removes all of the associations from this storage. The file storage will
     * be empty after this call returns.
     */
    public void clear();
}
