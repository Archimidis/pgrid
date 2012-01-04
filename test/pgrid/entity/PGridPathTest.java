/*
 * This file (pgrid.entity.PGridPathTest) is part of the libpgrid project.
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

import org.junit.Assert;
import org.junit.Test;

/**
 * @author Vourlakis Nikolas
 */
public class PGridPathTest {

    @Test
    public void testCommonPrefix() {
        String result = "00010";
        PGridPath path1 = new PGridPath("000100");
        PGridPath path2 = new PGridPath("0001010");
        Assert.assertTrue(0 == result.compareTo(path1.commonPrefix(path2)));
    }

    @Test
    public void testOthers() {
        String strPath = "000100";
        PGridPath path = new PGridPath(strPath);

        // revert
        Assert.assertTrue('1' == path.revert('0'));
        Assert.assertTrue('0' == path.revert('1'));

        // value
        Assert.assertTrue(strPath.charAt(0) == path.value(0));
        Assert.assertTrue(strPath.charAt(3) == path.value(3));

        // subPath
        int start = 0;
        int end = 3;
        String subResult = strPath.substring(start, end);
        Assert.assertTrue(0 == subResult.compareTo(path.subPath(start, end)));

        // append
        path.append('1');
        String appendResult = strPath + '1';
        Assert.assertTrue(0 == appendResult.compareTo(path.toString()));

        // constructor with null argument
        PGridPath path0 = new PGridPath(null);
        Assert.assertTrue(0 == "".compareTo(path0.toString()));
    }

    // public boolean hasPrefix(PGridPath path)
    @Test(expected = NullPointerException.class)
    public void WhenHasPrefixGivenANullPath_ExpectException() {
        PGridPath parent = new PGridPath("0000");
        parent.hasPrefix(null);
    }

    // public boolean hasPrefix(PGridPath path)
    @Test
    public void WhenPathIsPrefix_ExpectResult() {
        PGridPath parent = new PGridPath("0000");
        PGridPath child = new PGridPath("000");
        Assert.assertFalse(child.hasPrefix(parent));
        Assert.assertTrue(parent.hasPrefix(child));
    }
}

