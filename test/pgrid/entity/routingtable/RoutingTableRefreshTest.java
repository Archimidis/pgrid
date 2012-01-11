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

package pgrid.entity.routingtable;

import org.junit.Assert;
import org.junit.Test;
import pgrid.entity.Host;
import pgrid.entity.internal.PGridHost;

import java.net.UnknownHostException;

/**
 * @author Vourlakis Nikolas <nvourlakis@gmail.com>
 */
public class RoutingTableRefreshTest {

    @Test(expected = IllegalArgumentException.class)
    public void WhenRefMaxNegative_ExpectException() {
        RoutingTable routingTable = new RoutingTable();
        routingTable.refresh(-1);
    }

    @Test
    public void WhenHostsOnZeroLevel_ExpectHostsToTheirCorrespondingLevel() throws UnknownHostException {
        int refMax = Integer.MAX_VALUE;
        Host localhost = new PGridHost("127.0.0.1", 3000);
        localhost.setHostPath("0010");
        RoutingTable routingTable = new RoutingTable();
        routingTable.setLocalhost(localhost);

        Host host1 = new PGridHost("127.0.0.1", 1111);
        host1.setHostPath("1");
        Host host2 = new PGridHost("127.0.0.1", 2222);
        host2.setHostPath("01");
        Host host3 = new PGridHost("127.0.0.1", 3333);
        host3.setHostPath("0000");
        Host host4 = new PGridHost("127.0.0.1", 4444);
        host4.setHostPath("0001");
        Host host5 = new PGridHost("127.0.0.1", 5555);
        host5.setHostPath("00110");
        Host host6 = new PGridHost("127.0.0.1", 6666);
        host6.setHostPath("00111");

        routingTable.addReference(0, host1);
        routingTable.addReference(0, host2);
        routingTable.addReference(0, host3);
        routingTable.addReference(0, host4);
        routingTable.addReference(0, host5);
        routingTable.addReference(0, host6);

        routingTable.refresh(refMax);

        int expectedLevels = 4;
        int expected0levelSize = 1; // "1"
        int expected1levelSize = 1; // "01"
        int expected2levelSize = 2; // "000"
        int expected3levelSize = 2; // "0011"

        Assert.assertTrue(routingTable.levelNumber() == expectedLevels);
        Assert.assertTrue(routingTable.getLevel(0).size() == expected0levelSize);
        Assert.assertTrue(routingTable.getLevel(1).size() == expected1levelSize);
        Assert.assertTrue(routingTable.getLevel(2).size() == expected2levelSize);
        Assert.assertTrue(routingTable.getLevel(3).size() == expected3levelSize);

        Assert.assertTrue(routingTable.getLevel(0).contains(host1));
        Assert.assertTrue(routingTable.getLevel(1).contains(host2));
        Assert.assertTrue(routingTable.getLevel(2).contains(host3));
        Assert.assertTrue(routingTable.getLevel(2).contains(host4));
        Assert.assertTrue(routingTable.getLevel(3).contains(host5));
        Assert.assertTrue(routingTable.getLevel(3).contains(host6));

    }

    @Test
    public void WhenHostsShuffled_ExpectHostsToTheirCorrespondingLevel() throws UnknownHostException {
        int refMax = Integer.MAX_VALUE;
        Host localhost = new PGridHost("127.0.0.1", 3000);
        localhost.setHostPath("0010");
        RoutingTable routingTable = new RoutingTable();
        routingTable.setLocalhost(localhost);

        Host host1 = new PGridHost("127.0.0.1", 1111);
        host1.setHostPath("1");
        Host host2 = new PGridHost("127.0.0.1", 2222);
        host2.setHostPath("01");
        Host host3 = new PGridHost("127.0.0.1", 3333);
        host3.setHostPath("0000");
        Host host4 = new PGridHost("127.0.0.1", 4444);
        host4.setHostPath("0001");
        Host host5 = new PGridHost("127.0.0.1", 5555);
        host5.setHostPath("00110");
        Host host6 = new PGridHost("127.0.0.1", 6666);
        host6.setHostPath("00111");

        routingTable.addReference(3, host1);
        routingTable.addReference(3, host2);
        routingTable.addReference(3, host3);
        routingTable.addReference(2, host4);
        routingTable.addReference(1, host5);
        routingTable.addReference(3, host6);

        routingTable.refresh(refMax);

        int expectedLevels = 4;
        int expected0levelSize = 1; // "1"
        int expected1levelSize = 1; // "01"
        int expected2levelSize = 2; // "000"
        int expected3levelSize = 2; // "0011"

        Assert.assertTrue(routingTable.levelNumber() == expectedLevels);
        Assert.assertTrue(routingTable.getLevel(0).size() == expected0levelSize);
        Assert.assertTrue(routingTable.getLevel(1).size() == expected1levelSize);
        Assert.assertTrue(routingTable.getLevel(2).size() == expected2levelSize);
        Assert.assertTrue(routingTable.getLevel(3).size() == expected3levelSize);

        Assert.assertTrue(routingTable.getLevel(0).contains(host1));
        Assert.assertTrue(routingTable.getLevel(1).contains(host2));
        Assert.assertTrue(routingTable.getLevel(2).contains(host3));
        Assert.assertTrue(routingTable.getLevel(2).contains(host4));
        Assert.assertTrue(routingTable.getLevel(3).contains(host5));
        Assert.assertTrue(routingTable.getLevel(3).contains(host6));
    }

    @Test
    public void WhenHostWithPrefixFullPathOfLocalhost_ExpectToBeRemoved() throws UnknownHostException {
        int refMax = Integer.MAX_VALUE;
        String localhostPath = "0010";
        Host localhost = new PGridHost("127.0.0.1", 3000);
        localhost.setHostPath(localhostPath);
        RoutingTable routingTable = new RoutingTable();
        routingTable.setLocalhost(localhost);

        Host host1 = new PGridHost("127.0.0.1", 1111);
        host1.setHostPath("1");
        Host host2 = new PGridHost("127.0.0.1", 2222);
        host2.setHostPath("01");
        Host host3 = new PGridHost("127.0.0.1", 3333);
        host3.setHostPath("0000");
        Host host4 = new PGridHost("127.0.0.1", 4444);
        host4.setHostPath("0001");
        Host host5 = new PGridHost("127.0.0.1", 5555);
        host5.setHostPath("00110");
        Host host6 = new PGridHost("127.0.0.1", 6666);
        host6.setHostPath("00111");

        Host host7 = new PGridHost("127.0.0.1", 6666);
        host7.setHostPath(localhostPath + "11");

        routingTable.addReference(0, host1);
        routingTable.addReference(1, host2);
        routingTable.addReference(2, host3);
        routingTable.addReference(2, host4);
        routingTable.addReference(3, host5);
        routingTable.addReference(3, host6);
        routingTable.addReference(3, host7);

        routingTable.refresh(refMax);

        int expectedLevels = 4;
        int expected0levelSize = 1; // "1"
        int expected1levelSize = 1; // "01"
        int expected2levelSize = 2; // "000"
        int expected3levelSize = 2; // "0011"

        Assert.assertTrue(routingTable.levelNumber() == expectedLevels);
        Assert.assertTrue(routingTable.getLevel(0).size() == expected0levelSize);
        Assert.assertTrue(routingTable.getLevel(1).size() == expected1levelSize);
        Assert.assertTrue(routingTable.getLevel(2).size() == expected2levelSize);
        Assert.assertTrue(routingTable.getLevel(3).size() == expected3levelSize);

        Assert.assertTrue(routingTable.getLevel(0).contains(host1));
        Assert.assertTrue(routingTable.getLevel(1).contains(host2));
        Assert.assertTrue(routingTable.getLevel(2).contains(host3));
        Assert.assertTrue(routingTable.getLevel(2).contains(host4));
        Assert.assertTrue(routingTable.getLevel(3).contains(host5));
        Assert.assertTrue(routingTable.getLevel(3).contains(host6));
    }

    @Test
    public void WhenLevelBiggerThanRefMax_ExpectFinalSizeEqualToRefMax() throws UnknownHostException {
        int refMax = 1;
        Host localhost = new PGridHost("127.0.0.1", 3000);
        localhost.setHostPath("0010");
        RoutingTable routingTable = new RoutingTable();
        routingTable.setLocalhost(localhost);

        Host host1 = new PGridHost("127.0.0.1", 1111);
        host1.setHostPath("1");
        Host host2 = new PGridHost("127.0.0.1", 2222);
        host2.setHostPath("01");
        Host host3 = new PGridHost("127.0.0.1", 3333);
        host3.setHostPath("0000");
        Host host4 = new PGridHost("127.0.0.1", 4444);
        host4.setHostPath("0001");
        Host host5 = new PGridHost("127.0.0.1", 5555);
        host5.setHostPath("00110");
        Host host6 = new PGridHost("127.0.0.1", 6666);
        host6.setHostPath("00111");

        routingTable.addReference(0, host1);
        routingTable.addReference(1, host2);
        routingTable.addReference(2, host3);
        routingTable.addReference(2, host4);
        routingTable.addReference(3, host5);
        routingTable.addReference(3, host6);

        routingTable.refresh(refMax);

        int expectedLevels = 4;
        int expected0levelSize = 1; // "1"
        int expected1levelSize = 1; // "01"
        int expected2levelSize = 1; // "000"
        int expected3levelSize = 1; // "0011"

        Assert.assertTrue(routingTable.levelNumber() == expectedLevels);
        Assert.assertTrue(routingTable.getLevel(0).size() == expected0levelSize);
        Assert.assertTrue(routingTable.getLevel(1).size() == expected1levelSize);
        Assert.assertTrue(routingTable.getLevel(2).size() == expected2levelSize);
        Assert.assertTrue(routingTable.getLevel(3).size() == expected3levelSize);

        Assert.assertTrue(routingTable.getLevel(0).contains(host1));
        Assert.assertTrue(routingTable.getLevel(1).contains(host2));

        Assert.assertTrue(routingTable.getLevel(2).contains(host3) || routingTable.getLevel(2).contains(host4));
        Assert.assertFalse(routingTable.getLevel(2).contains(host3) && routingTable.getLevel(2).contains(host4));

        Assert.assertTrue(routingTable.getLevel(3).contains(host5) || routingTable.getLevel(3).contains(host6));
        Assert.assertFalse(routingTable.getLevel(3).contains(host5) && routingTable.getLevel(3).contains(host6));
    }

    @Test
    public void WhenLocalhostReducesPath_ExpectRemovedLevels() throws UnknownHostException {
        int refMax = Integer.MAX_VALUE;
        Host localhost = new PGridHost("127.0.0.1", 3000);
        localhost.setHostPath("0010");
        RoutingTable routingTable = new RoutingTable();
        routingTable.setLocalhost(localhost);

        Host host1 = new PGridHost("127.0.0.1", 1111);
        host1.setHostPath("1");
        Host host2 = new PGridHost("127.0.0.1", 2222);
        host2.setHostPath("01");
        Host host3 = new PGridHost("127.0.0.1", 3333);
        host3.setHostPath("0000");
        Host host4 = new PGridHost("127.0.0.1", 4444);
        host4.setHostPath("0001");
        Host host5 = new PGridHost("127.0.0.1", 5555);
        host5.setHostPath("00110");
        Host host6 = new PGridHost("127.0.0.1", 6666);
        host6.setHostPath("00111");

        routingTable.addReference(0, host1);
        routingTable.addReference(1, host2);
        routingTable.addReference(2, host3);
        routingTable.addReference(2, host4);
        routingTable.addReference(3, host5);
        routingTable.addReference(3, host6);

        localhost.setHostPath("001");
        routingTable.refresh(refMax);

        int expectedLevels = 3;
        int expected0levelSize = 1; // "1"
        int expected1levelSize = 1; // "01"
        int expected2levelSize = 2; // "000"

        Assert.assertTrue(routingTable.levelNumber() == expectedLevels);
        Assert.assertTrue(routingTable.getLevel(0).size() == expected0levelSize);
        Assert.assertTrue(routingTable.getLevel(1).size() == expected1levelSize);
        Assert.assertTrue(routingTable.getLevel(2).size() == expected2levelSize);

        Assert.assertTrue(routingTable.getLevel(0).contains(host1));
        Assert.assertTrue(routingTable.getLevel(1).contains(host2));

        Assert.assertTrue(routingTable.getLevel(2).contains(host3));
        Assert.assertTrue(routingTable.getLevel(2).contains(host4));

        Assert.assertFalse(routingTable.contains(host5) && routingTable.contains(host6));
    }

    @Test
    public void WhenLocalhostIncreasesPath_ExpectAddedLevels() throws UnknownHostException {
        int refMax = Integer.MAX_VALUE;
        String localhostPath = "0010";
        Host localhost = new PGridHost("127.0.0.1", 3000);
        localhost.setHostPath(localhostPath);
        RoutingTable routingTable = new RoutingTable();
        routingTable.setLocalhost(localhost);

        Host host1 = new PGridHost("127.0.0.1", 1111);
        host1.setHostPath("1");
        Host host2 = new PGridHost("127.0.0.1", 2222);
        host2.setHostPath("01");
        Host host3 = new PGridHost("127.0.0.1", 3333);
        host3.setHostPath("0000");
        Host host4 = new PGridHost("127.0.0.1", 4444);
        host4.setHostPath("0001");
        Host host5 = new PGridHost("127.0.0.1", 5555);
        host5.setHostPath("00110");
        Host host6 = new PGridHost("127.0.0.1", 6666);
        host6.setHostPath("00111");

        routingTable.addReference(0, host1);
        routingTable.addReference(1, host2);
        routingTable.addReference(2, host3);
        routingTable.addReference(2, host4);
        routingTable.addReference(3, host5);
        routingTable.addReference(3, host6);

        localhost.setHostPath(localhostPath + "11");
        routingTable.refresh(refMax);

        int expectedLevels = 6;
        int expected0levelSize = 1; // "1"
        int expected1levelSize = 1; // "01"
        int expected2levelSize = 2; // "000"
        int expected3levelSize = 2; // "0011"
        int expected4levelSize = 0; // "00100"
        int expected5levelSize = 0; // "001010"

        Assert.assertTrue(routingTable.levelNumber() == expectedLevels);
        Assert.assertTrue(routingTable.getLevel(0).size() == expected0levelSize);
        Assert.assertTrue(routingTable.getLevel(1).size() == expected1levelSize);
        Assert.assertTrue(routingTable.getLevel(2).size() == expected2levelSize);
        Assert.assertTrue(routingTable.getLevel(3).size() == expected3levelSize);
        Assert.assertTrue(routingTable.getLevel(4).size() == expected4levelSize);
        Assert.assertTrue(routingTable.getLevel(5).size() == expected5levelSize);


    }
}
