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
import org.junit.BeforeClass;
import org.junit.Test;
import pgrid.entity.Host;
import pgrid.entity.internal.PGridHost;

import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Collection;

/**
 * Test cases for method {@link RoutingTable#closestHosts(String)}.
 *
 * @author Nikolas Vourlakis <nvourlakis@gmail.com>
 */
public class RoutingTableClosestHostTest {

    private static Host localhost_;

    @BeforeClass
    public static void beforeClass() throws UnknownHostException {
        localhost_ = new PGridHost("127.0.0.1", 3000);
        localhost_.setHostPath("000");
    }

    @Test
    public void WhenSearchMatchLocalhost_ExpectLocalhost() throws UnknownHostException {
        String SEARCH = "000"; // == "000"

        Host host1 = new PGridHost("127.0.0.1", 1111);
        host1.setHostPath("1");
        Host host2 = new PGridHost("127.0.0.1", 2222);
        host2.setHostPath("01");
        Host host3 = new PGridHost("127.0.0.1", 3333);
        host3.setHostPath("0010");
        Host host4 = new PGridHost("127.0.0.1", 4444);
        host4.setHostPath("0011");

        RoutingTable routingTable = new RoutingTable();
        routingTable.setLocalhost(localhost_);

        routingTable.addReference(0, host1);
        routingTable.addReference(1, host2);
        routingTable.addReference(2, host3);
        routingTable.addReference(2, host4);

        Collection<Host> actual = routingTable.closestHosts(SEARCH);

        Assert.assertTrue(actual.size() == 1);
        Assert.assertTrue(actual.contains(localhost_));

        Assert.assertFalse(actual.contains(host1));
        Assert.assertFalse(actual.contains(host2));
        Assert.assertFalse(actual.contains(host3));
        Assert.assertFalse(actual.contains(host4));
    }

    @Test
    public void WhenSearchPrefixOfLocalhost_ExpectLocalhost() throws UnknownHostException {
        String SEARCH = "00"; // prefix of "000"

        Host host1 = new PGridHost("127.0.0.1", 1111);
        host1.setHostPath("1");
        Host host2 = new PGridHost("127.0.0.1", 2222);
        host2.setHostPath("01");
        Host host3 = new PGridHost("127.0.0.1", 3333);
        host3.setHostPath("0010");
        Host host4 = new PGridHost("127.0.0.1", 4444);
        host4.setHostPath("0011");

        RoutingTable routingTable = new RoutingTable();
        routingTable.setLocalhost(localhost_);

        routingTable.addReference(0, host1);
        routingTable.addReference(1, host2);
        routingTable.addReference(2, host3);
        routingTable.addReference(2, host4);

        Collection<Host> actual = routingTable.closestHosts(SEARCH);

        Assert.assertTrue(actual.size() == 1);
        Assert.assertTrue(actual.contains(localhost_));

        Assert.assertFalse(actual.contains(host1));
        Assert.assertFalse(actual.contains(host2));
        Assert.assertFalse(actual.contains(host3));
        Assert.assertFalse(actual.contains(host4));
    }

    @Test
    public void WhenSearchNotPrefixOfLocalhost_ExpectHostFromConjugateTree() throws UnknownHostException {
        String SEARCH = "1010101"; // no prefix relation to "000"

        Host host1 = new PGridHost("127.0.0.1", 1111);
        host1.setHostPath("1");

        RoutingTable routingTable = new RoutingTable();
        routingTable.setLocalhost(localhost_);

        routingTable.addReference(0, host1);

        Collection<Host> actual = routingTable.closestHosts(SEARCH);

        Assert.assertTrue(actual.size() == routingTable.getLevel(0).size());
        Assert.assertTrue(actual.contains(host1));

        Assert.assertFalse(actual.contains(localhost_));
    }

    @Test
    public void WhenSearchIsPrefixAndLevelsNotEmpty_ExpectHostCollection() throws UnknownHostException {
        String SEARCH = "00110"; // prefix relation "000" and level for path "001" not empty

        Host host1 = new PGridHost("127.0.0.1", 1111);
        host1.setHostPath("1");
        Host host2 = new PGridHost("127.0.0.1", 2222);
        host2.setHostPath("01");
        Host host3 = new PGridHost("127.0.0.1", 3333);
        host3.setHostPath("0010");
        Host host4 = new PGridHost("127.0.0.1", 4444);
        host4.setHostPath("0011");

        RoutingTable routingTable = new RoutingTable();
        routingTable.setLocalhost(localhost_);

        routingTable.addReference(0, host1);
        routingTable.addReference(1, host2);
        routingTable.addReference(2, host3);
        routingTable.addReference(2, host4);

        Collection<Host> actual = routingTable.closestHosts(SEARCH);

        Assert.assertTrue(actual.size() == routingTable.getLevel(2).size());
        Assert.assertTrue(actual.contains(host3));
        Assert.assertTrue(actual.contains(host4));

        Assert.assertFalse(actual.contains(localhost_));
        Assert.assertFalse(actual.contains(host1));
        Assert.assertFalse(actual.contains(host2));
    }

    @Test
    public void WhenSearchIsPrefixButLevelEmpty_ExpectHostCollectionInPrefixRelation() throws UnknownHostException {
        String SEARCH = "00110"; // prefix relation "000" but level for path "001" is empty

        Host host1 = new PGridHost("127.0.0.1", 1111);
        host1.setHostPath("1");
        Host host2 = new PGridHost("127.0.0.1", 2222);
        host2.setHostPath("01");

        RoutingTable routingTable = new RoutingTable();
        routingTable.setLocalhost(localhost_);

        routingTable.addReference(0, host1);
        routingTable.addReference(1, host2);

        Collection<Host> expected = new ArrayList<Host>(1);
        expected.add(localhost_);

        Collection<Host> actual = routingTable.closestHosts(SEARCH);

        Assert.assertTrue(actual.contains(localhost_));

        Assert.assertFalse(actual.contains(host1));
        Assert.assertFalse(actual.contains(host2));
    }
}
