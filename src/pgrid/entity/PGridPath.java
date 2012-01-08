/*
 * This file (pgrid.entity.PGridPath) is part of the libpgrid project.
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

package pgrid.entity;


import pgrid.entity.internal.PGridKey;

/**
 * This class stores information about the path of a PGridHost. It assumes a
 * binary path representation as described in the original paper of P-Grid.
 *
 * @author Vourlakis Nikolas <nvourlakis@gmail.com>
 */
public class PGridPath {
    private String path_ = INITIAL_PATH;

    private final static String INITIAL_PATH = "";

    /**
     * Default constructor.
     */
    public PGridPath() {

    }

    /**
     * Constructs a PGridPath with a given initial path. if the argument is
     * null it will assign the default empty path.
     *
     * @param path initial value of the PGridPath.
     */
    public PGridPath(String path) {
        path_ = (path == null) ? INITIAL_PATH : path;
    }

    /**
     * Returns the current length of the sorted path.
     *
     * @return the path length.
     */
    public int length() {
        return path_.length();
    }

    /**
     * Transforms this path to a key.
     *
     * @return a Key object.
     */
    public Key toKey() {
        return new PGridKey(path_);
    }

    @Override
    public String toString() {
        return path_;
    }

    /**
     * Resets the current path with the given new path.
     *
     * @param newPath the new path to be swapped with.
     */
    public void setPath(String newPath) {
        path_ = newPath;
    }

    /**
     * Returns a sub-sequence of the path starting and ending at the specified
     * indexes.
     *
     * @param start starting index.
     * @param end   ending index.
     * @return the sub-path.
     * @throws IndexOutOfBoundsException when start is negative, or end is
     *                                   smaller than start, or end is greater
     *                                   than the current path length.
     */
    public String subPath(int start, int end) {
        if (start < 0 || end < start || end > path_.length()) {
            throw new IndexOutOfBoundsException();
        }

        return path_.substring(start, end);
    }

    /**
     * Returns the character in the path at the specified level.
     *
     * @param level where to search for.
     * @return the character.
     * @throws IndexOutOfBoundsException if the given level exceeds the
     *                                   current path length.
     */
    public char value(int level) {
        if (path_.length() < level) {
            throw new IndexOutOfBoundsException();
        }

        return path_.charAt(level);
    }

    /**
     * Appends a given character at the end of the current path.
     *
     * @param c a character representation of '1' or '0'.
     * @throws IllegalArgumentException in case of a character other than '1'
     *                                  or '0'.
     */
    public void append(char c) {
        if (c != '0' && c != '1') {
            throw new IllegalArgumentException("Argument must be '0' or '1'");
        }

        path_ = path_ + c;
    }

    /**
     * Revert the given character.
     *
     * @param c a character representation of '1' or '0'.
     * @return the reverted character.
     * @throws IllegalArgumentException in case of a character other than '1'
     *                                  or '0'.
     */
    public char revert(char c) {
        if (c != '0' && c != '1') {
            throw new IllegalArgumentException("Argument must be '0' or '1'");
        }

        return (c == '1') ? '0' : '1';
    }

    /**
     * Reverts and appends the given character to the current path.
     *
     * @param c a character representation of '1' or '0'.
     * @throws IllegalArgumentException in case of a character other than '1'
     *                                  or '0'.
     */
    public void revertAndAppend(char c) {
        if (c != '0' && c != '1') {
            throw new IllegalArgumentException("Argument must be '0' or '1'");
        }

        append(revert(c));
    }

    /**
     * It returns the common prefix between this and another PGridPath.
     *
     * @param path another binary path.
     * @return the common prefix of the two paths.
     * @throws NullPointerException in case of a null path argument.
     */
    public String commonPrefix(PGridPath path) {
        if (path == null) {
            throw new NullPointerException();
        }

        int index = 1;
        String oPath = path.toString();

        while (path_.regionMatches(0, oPath, 0, index)) {
            index++;
        }
        index--;

        return path_.substring(0, index);
    }

    /**
     * Given a path it decides if it's a prefix of this path. For example,
     * "000" is prefix to "0001" but not the other way around.
     *
     * @param path the path to check.
     * @return true if the given  path is a prefix.
     * @throws NullPointerException in case of a null path argument.
     */
    public boolean hasPrefix(PGridPath path) {
        if (path == null) {
            throw new NullPointerException();
        }
        return commonPrefix(path).length() == path.length();
    }
}
