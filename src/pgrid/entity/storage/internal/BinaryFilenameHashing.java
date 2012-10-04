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

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * @author Vourlakis Nikolas <nvourlakis@gmail.com>
 */
public class BinaryFilenameHashing implements FilenameHashAlgorithm {

    private static final String ALGORITHM = "SHA-1";

    /**
     * A SHA-1 hash is produced from the given string. The hash is transformed
     * to binary and then a Key object is constructed by selecting the 64 first
     * bytes of the hash. This is done due to number type limit (long).
     *
     * @param value to be hashed.
     * @return a fully constructed {@link Key} object.
     */
    @Override
    public Key produceKey(String value) {
        if (value == null) {
            throw new NullPointerException("A null value was given");
        }

        MessageDigest instance = null;
        try {
            instance = MessageDigest.getInstance(ALGORITHM);
        } catch (NoSuchAlgorithmException ignored) {
        }

        instance.reset();
        instance.update(value.getBytes());
        byte[] digest = instance.digest();

        StringBuilder binary = new StringBuilder();

        for (byte b : digest) {
            int val = b;
            for (int i = 0; i < 8; i++) {
                binary.append((val & 128) == 0 ? 0 : 1);
                val <<= 1;
            }
        }
        return new PGridKey(binary.toString().substring(0, 63));
    }
}
