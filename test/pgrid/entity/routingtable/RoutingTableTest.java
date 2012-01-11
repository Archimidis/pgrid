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
 * @author Vourlakis Nikolas <nvourlakis@gmail.com>
 */
public class RoutingTableTest {

    private static Host localhost_;

    @BeforeClass
    public static void beforeClass() throws UnknownHostException {
        localhost_ = new PGridHost("127.0.0.1", 10000);
        localhost_.setHostPath("0000");
    }

    // void addReference(int level, PGridHost host)
    @Test(expected = NullPointerException.class)
    public void WhenAddingNullReference_ExpectException() {
        RoutingTable table = new RoutingTable();
        table.setLocalhost(localhost_);

        Host host = null;
        table.addReference(0, host);
    }

    // void addReference(int level, PGridHost host)
    @Test(expected = IllegalArgumentException.class)
    public void WhenAddingReferenceToNegativeLevel_ExpectException()
            throws UnknownHostException {
        RoutingTable table = new RoutingTable();
        table.setLocalhost(localhost_);

        Host host = new PGridHost("127.0.0.1", 3000);
        table.addReference(-2, host);
    }

    // void addReference(int level, PGridHost host)
    @Test(expected = IllegalArgumentException.class)
    public void WhenAddingReferenceToLevelGreaterPath_ExpectException()
            throws UnknownHostException {
        RoutingTable table = new RoutingTable();
        table.setLocalhost(localhost_);

        Host host = new PGridHost("127.0.0.1", 3000);
        table.addReference(100, host);
    }

    // void addReference(int level, PGridHost host)
    @Test
    public void WhenAddingReference_ExpectContainedSizeChange()
            throws UnknownHostException {
        RoutingTable table = new RoutingTable();
        table.setLocalhost(localhost_);
        Assert.assertTrue(table.levelNumber() == localhost_.getHostPath().length());
        Assert.assertTrue(table.uniqueHostsNumber() == 0);

        int level = localhost_.getHostPath().length();
        Host host = new PGridHost("127.0.0.1", 3000);
        table.addReference(level - 1, host);
        Assert.assertTrue(table.levelNumber() == level);
        Assert.assertTrue(table.contains(host));
        Assert.assertTrue(table.uniqueHostsNumber() == 1);
    }

    // void addReference(int level, Collection<PGridHost> hosts)
    @Test(expected = NullPointerException.class)
    public void WhenAddingNullCollection_ExpectException() {
        RoutingTable table = new RoutingTable();
        table.setLocalhost(localhost_);

        ArrayList<Host> host = null;
        table.addReference(0, host);
    }

    // void addReference(int level, Collection<PGridHost> hosts)
    @Test(expected = IllegalArgumentException.class)
    public void WhenAddingCollectionToNegativeLevel_ExpectException()
            throws UnknownHostException {
        RoutingTable table = new RoutingTable();
        table.setLocalhost(localhost_);

        Host host0 = new PGridHost("127.0.0.1", 3000);
        Host host1 = new PGridHost("127.0.0.1", 3001);
        Collection<Host> list = new ArrayList<Host>(2);
        list.add(host0);
        list.add(host1);
        table.addReference(-1, list);
    }

    // void addReference(int level, Collection<PGridHost> hosts)
    @Test(expected = IllegalArgumentException.class)
    public void WhenAddingCollectionToLevelGreaterPath_ExpectException()
            throws UnknownHostException {
        RoutingTable table = new RoutingTable();
        table.setLocalhost(localhost_);

        Host host0 = new PGridHost("127.0.0.1", 3000);
        Host host1 = new PGridHost("127.0.0.1", 3001);
        Collection<Host> list = new ArrayList<Host>(2);
        list.add(host0);
        list.add(host1);
        table.addReference(100, list);
    }

    // void addReference(int level, Collection<PGridHost> hosts)
    @Test
    public void WhenAddingCollection_ExpectContainedSizeChange()
            throws UnknownHostException {
        int level = localhost_.getHostPath().length();
        RoutingTable table = new RoutingTable();
        table.setLocalhost(localhost_);
        Assert.assertTrue(table.levelNumber() == level);
        Assert.assertTrue(table.uniqueHostsNumber() == 0);

        Host host0 = new PGridHost("127.0.0.1", 3000);
        Host host1 = new PGridHost("127.0.0.1", 3001);
        Collection<Host> list = new ArrayList<Host>(2);
        list.add(host0);
        list.add(host1);

        table.addReference(level - 1, list);
        Assert.assertTrue(table.levelNumber() == level);
        Assert.assertTrue(table.contains(host0) && table.contains(host1));
        Assert.assertTrue(table.uniqueHostsNumber() == 2);
    }

    // void updateLevel(int level, Collection<PGridHost> hosts)
    @Test
    public void WhenUpdatingLevelWithNullCollection_ExpectNothing() {
        int level = localhost_.getHostPath().length();
        RoutingTable table = new RoutingTable();
        table.setLocalhost(localhost_);
        Assert.assertTrue(table.levelNumber() == level);
        Assert.assertTrue(table.uniqueHostsNumber() == 0);

        ArrayList<Host> host = null;
        table.updateLevel(0, host);
        Assert.assertTrue(table.levelNumber() == level);
        Assert.assertTrue(table.uniqueHostsNumber() == 0);
    }

    // void updateLevel(int level, Collection<PGridHost> hosts)
    @Test(expected = IllegalArgumentException.class)
    public void WhenUpdatingInvalidLevel_ExpectException()
            throws UnknownHostException {
        RoutingTable table = new RoutingTable();
        table.setLocalhost(localhost_);
        Assert.assertTrue(table.uniqueHostsNumber() == 0);

        Host host0 = new PGridHost("127.0.0.1", 3000);
        Host host1 = new PGridHost("127.0.0.1", 3001);
        Collection<Host> list = new ArrayList<Host>(2);
        list.add(host0);
        list.add(host1);
        table.updateLevel(100, list);
    }


    // void updateLevel(int level, Collection<PGridHost> hosts)
    @Test
    public void WhenUpdatingCollection_ExpectContainedSizeChange()
            throws UnknownHostException {
        int tableLevel = localhost_.getHostPath().length();
        RoutingTable table = new RoutingTable();
        table.setLocalhost(localhost_);
        Assert.assertTrue(table.levelNumber() == tableLevel);
        Assert.assertTrue(table.uniqueHostsNumber() == 0);

        Host oldHost0 = new PGridHost("127.0.0.1", 1000);
        Host oldHost1 = new PGridHost("127.0.0.1", 1001);
        Collection<Host> oldList = new ArrayList<Host>(2);
        oldList.add(oldHost0);
        oldList.add(oldHost1);
        table.addReference(0, oldList);

        Host newHost0 = new PGridHost("127.0.0.1", 3000);
        Host newHost1 = new PGridHost("127.0.0.1", 3001);
        Collection<Host> newList = new ArrayList<Host>(2);
        newList.add(newHost0);
        newList.add(newHost1);

        table.updateLevel(0, newList);

        Assert.assertTrue(table.levelNumber() == tableLevel);
        Collection<Host> zeroLevel = table.getLevel(0);
        Assert.assertTrue(zeroLevel.contains(oldHost0) &&
                zeroLevel.contains(oldHost1) &&
                zeroLevel.contains(newHost0) &&
                zeroLevel.contains(newHost1));
        Assert.assertTrue(table.uniqueHostsNumber() == 4);
    }

    // void updateReference(PGridHost host)
    @Test
    public void WhenUpdatingWithNullHost_ExpectNothing() {
        int level = localhost_.getHostPath().length();
        RoutingTable table = new RoutingTable();
        table.setLocalhost(localhost_);
        Assert.assertTrue(table.levelNumber() == level);
        Assert.assertTrue(table.uniqueHostsNumber() == 0);

        ArrayList<Host> host = null;
        table.updateLevel(0, host);
        Assert.assertTrue(table.levelNumber() == level);
        Assert.assertTrue(table.uniqueHostsNumber() == 0);
    }

    // void updateReference(PGridHost host)
    @Test
    public void WhenUpdatingContainedHost_ExpectChange()
            throws UnknownHostException {
        int level = localhost_.getHostPath().length();
        RoutingTable table = new RoutingTable();
        table.setLocalhost(localhost_);
        Assert.assertTrue(table.levelNumber() == level);
        Assert.assertTrue(table.uniqueHostsNumber() == 0);

        Host host = new PGridHost("127.0.0.1", 3333);
        table.addReference(0, host);
        Assert.assertTrue(table.uniqueHostsNumber() == 1);

        Host hostToUpdate = new PGridHost("127.0.0.1", 1111);
        hostToUpdate.setUUID(host.getUUID());

        table.updateReference(hostToUpdate);
        Collection<Host> zeroLevel = table.getLevel(0);
        Assert.assertTrue(zeroLevel.contains(hostToUpdate));

        Assert.assertTrue(table.selectUUIDHost(host.getUUID()).getPort() == hostToUpdate.getPort());
        Assert.assertTrue(table.levelNumber() == level);
        Assert.assertTrue(table.contains(host));
        Assert.assertTrue(table.uniqueHostsNumber() == 1);
    }

    // void updateReference(PGridHost host)
    @Test
    public void WhenUpdatingUnknownHost_ExpectNothing()
            throws UnknownHostException {
        int level = localhost_.getHostPath().length();

        RoutingTable table = new RoutingTable();
        table.setLocalhost(localhost_);
        Assert.assertTrue(table.uniqueHostsNumber() == 0);

        Host host = new PGridHost("127.0.0.1", 3333);
        table.addReference(0, host);
        Assert.assertTrue(table.uniqueHostsNumber() == 1);

        Host hostToUpdate = new PGridHost("127.0.0.1", 1111);
        table.updateReference(hostToUpdate);

        Assert.assertTrue(table.levelNumber() == level);
        Assert.assertTrue(table.contains(host) && !table.contains(hostToUpdate));
        Assert.assertTrue(table.uniqueHostsNumber() == 1);
    }

    // void unionLevel(int level, RoutingTable routingTable)
    @Test(expected = NullPointerException.class)
    public void WhenUnionWithNullRoutingTable_ExpectException() {
        RoutingTable table = new RoutingTable();
        table.setLocalhost(localhost_);

        RoutingTable other = null;
        table.unionLevel(0, other);
    }

    // void unionLevel(int level, RoutingTable routingTable)
    @Test(expected = IllegalArgumentException.class)
    public void WhenUnionAtInvalidLevel_ExpectException()
            throws UnknownHostException {
        RoutingTable table = new RoutingTable();
        table.setLocalhost(localhost_);

        RoutingTable other = new RoutingTable();
        table.unionLevel(100, other);
    }

    // void unionLevel(int level, RoutingTable routingTable)
    @Test
    public void WhenUnionBothHavingLevels_ExpectChange()
            throws UnknownHostException {
        int level = localhost_.getHostPath().length();
        RoutingTable table = new RoutingTable();
        table.setLocalhost(localhost_);
        Host host = new PGridHost("127.0.0.1", 3333);

        table.addReference(0, host);
        Assert.assertTrue(table.levelNumber() == level);
        Assert.assertTrue(table.uniqueHostsNumber() == 1);

        RoutingTable other = new RoutingTable();
        Host otherHost = new PGridHost("127.0.0.1", 1234);
        otherHost.setHostPath("0000");
        other.setLocalhost(otherHost);
        int otherLevel = otherHost.getHostPath().length();

        Host host1 = new PGridHost("127.0.0.1", 1111);
        other.addReference(0, host1);
        Assert.assertTrue(other.levelNumber() == otherLevel);
        Assert.assertTrue(other.uniqueHostsNumber() == 1);

        table.unionLevel(0, other);
        Assert.assertTrue(table.levelNumber() == level);
        Assert.assertTrue(table.contains(host) && table.contains(host1));
        Assert.assertTrue(table.uniqueHostsNumber() == 2);
    }

    // void unionLevel(int level, RoutingTable routingTable)
    @Test
    public void WhenUnionOtherNotHavingLevelSpecified_ExpectNothing()
            throws UnknownHostException {
        localhost_.setHostPath("00");
        int level = localhost_.getHostPath().length();
        RoutingTable table = new RoutingTable();
        table.setLocalhost(localhost_);

        Host host0 = new PGridHost("127.0.0.1", 3333);
        Host host1 = new PGridHost("127.0.0.1", 3333);
        table.addReference(0, host0);
        table.addReference(1, host1);
        Assert.assertTrue(table.levelNumber() == level);
        Assert.assertTrue(table.uniqueHostsNumber() == 2);

        RoutingTable other = new RoutingTable();
        Host otherHost = new PGridHost("127.0.0.1", 1234);
        otherHost.setHostPath("0");
        other.setLocalhost(otherHost);
        int otherLevel = otherHost.getHostPath().length();

        Host host3 = new PGridHost("127.0.0.1", 1111);
        other.addReference(0, host3);
        Assert.assertTrue(other.levelNumber() == otherLevel);
        Assert.assertTrue(other.uniqueHostsNumber() == 1);

        table.unionLevel(1, other);
        Assert.assertTrue(table.levelNumber() == level);
        Assert.assertTrue(table.contains(host0) &&
                table.contains(host1) &&
                !table.contains(otherHost));
        Assert.assertTrue(table.uniqueHostsNumber() == 2);
    }

    // Collection<PGridHost> getLevel(int level)
    @Test(expected = IllegalArgumentException.class)
    public void WhenGetLevelNotExisting_ExpectException()
            throws UnknownHostException {
        RoutingTable table = new RoutingTable();
        table.setLocalhost(localhost_);

        Host host = new PGridHost("127.0.0.1", 3333);
        table.addReference(0, host);
        table.getLevel(1000);
    }

    // void removeReference(PGridHost host)
    @Test(expected = NullPointerException.class)
    public void WhenRemovingNullReference_ExpectException()
            throws UnknownHostException {
        RoutingTable table = new RoutingTable();
        table.setLocalhost(localhost_);

        Host host = new PGridHost("127.0.0.1", 3000);
        Host toRemove = null;
        table.addReference(0, host);
        table.removeReference(toRemove);
    }

    // void removeReference(PGridHost host)
    @Test
    public void WhenRemovingReference_ExpectRemoved()
            throws UnknownHostException {
        localhost_.setHostPath("0000");
        int level = localhost_.getHostPath().length();
        RoutingTable table = new RoutingTable();
        table.setLocalhost(localhost_);

        Host host = new PGridHost("127.0.0.1", 3000);
        for (int i = 0; i < level - 1; i++) {
            table.addReference(i, host);
        }
        Assert.assertTrue(table.levelNumber() == level);
        Assert.assertTrue(table.uniqueHostsNumber() == 1);

        table.removeReference(host);
        Assert.assertTrue(table.levelNumber() == level);
        Assert.assertFalse(table.contains(host));
        Assert.assertTrue(table.uniqueHostsNumber() == 0);
    }

    // static Collection<PGridHost> union(Collection<PGridHost> refs1, Collection<PGridHost> refs2)
    @Test(expected = NullPointerException.class)
    public void WhenStaticUnionNullCollections_ExpectException() {
        ArrayList<Host> list1 = null;
        ArrayList<Host> list2 = null;
        RoutingTable.union(list1, list2);
    }

    @Test
    public void WhenStaticUnionCollections_ExpectResultUnion()
            throws UnknownHostException {
        ArrayList<Host> list0 = new ArrayList<Host>(1);
        Host host0 = new PGridHost("127.0.0.1", 3333);
        list0.add(host0);

        ArrayList<Host> list1 = new ArrayList<Host>(1);
        Host host1 = new PGridHost("127.0.0.1", 1111);
        list1.add(host1);

        Collection<Host> result = RoutingTable.union(list0, list1);
        Assert.assertTrue(result.size() == 2);
        Assert.assertTrue(result.contains(host0) && result.contains(host1));
    }

    // static Collection<PGridHost> randomSelect(int refMax, Collection<PGridHost> commonRefs)
    @Test(expected = NullPointerException.class)
    public void WhenStaticRandomSelectFromNullCollection_ExpectException() {
        ArrayList<Host> list = null;
        RoutingTable.randomSelect(0, list);
    }

    // static Collection<PGridHost> randomSelect(int refMax, Collection<PGridHost> commonRefs)
    @Test(expected = IllegalArgumentException.class)
    public void WhenStaticRandomSelectNegativeRefMax_ExpectException() {
        ArrayList<Host> list = new ArrayList<Host>();
        RoutingTable.randomSelect(-11, list);
    }

    // static Collection<PGridHost> randomSelect(int refMax, Collection<PGridHost> commonRefs)
    @Test
    public void WhenStaticRandomSelect_ExpectRefMaxHostsInResult()
            throws UnknownHostException {
        ArrayList<Host> list = new ArrayList<Host>(1);
        Host host0 = new PGridHost("127.0.0.1", 3333);
        Host host1 = new PGridHost("127.0.0.1", 1111);
        list.add(host0);
        list.add(host1);

        int refMax = 100;
        Collection<Host> result = RoutingTable.randomSelect(refMax, list);
        Assert.assertTrue(result.size() <= refMax);
        Assert.assertTrue(result.size() == 2);
        Assert.assertTrue(result.contains(host0) && result.contains(host1));

        refMax = 1;
        result = RoutingTable.randomSelect(refMax, list);
        Assert.assertTrue(result.size() <= refMax);
        Assert.assertTrue(result.size() == 1);
        Assert.assertFalse(result.contains(host0) && result.contains(host1));
        Assert.assertTrue(result.contains(host0) || result.contains(host1));
    }

    // public void updateLevelRandomly(int level, Host host, int refMax)
    @Test(expected = IllegalArgumentException.class)
    public void WhenUpdatingLevelGreaterPath_ExpectException() throws UnknownHostException {
        RoutingTable routingTable = new RoutingTable();
        routingTable.setLocalhost(localhost_);
        PGridHost aHost = new PGridHost("127.0.0.1", 3000);
        routingTable.updateLevelRandomly(routingTable.levelNumber() + 10000, aHost, 2);
    }

    // public void updateLevelRandomly(int level, Host host, int refMax)
    @Test(expected = IllegalArgumentException.class)
    public void WhenUpdatingNegativeLevel_ExpectException() throws UnknownHostException {
        RoutingTable routingTable = new RoutingTable();
        routingTable.setLocalhost(localhost_);
        PGridHost aHost = new PGridHost("127.0.0.1", 3000);
        routingTable.updateLevelRandomly(-1, aHost, 2);
    }

    // public void updateLevelRandomly(int level, Host host, int refMax)
    @Test
    public void WhenUpdating_ExpectResults() throws UnknownHostException {
        RoutingTable routingTable = new RoutingTable();
        localhost_.setHostPath("0");
        routingTable.setLocalhost(localhost_);
        PGridHost aHost = new PGridHost("127.0.0.1", 3333);
        PGridHost anotherHost = new PGridHost("127.0.0.1", 4444);
        routingTable.addReference(0, aHost);
        routingTable.updateLevelRandomly(0, anotherHost, 2);

        Assert.assertTrue(routingTable.levelNumber() == 1);
        Assert.assertTrue(routingTable.getLevel(0).size() == 2);
        Assert.assertTrue(routingTable.getLevel(0).contains(aHost));
        Assert.assertTrue(routingTable.getLevel(0).contains(anotherHost));
    }

    // public void updateLevelRandomly(int level, Host host, int refMax)
    @Test
    public void WhenUpdatingWithNullHostAndRefmaxLesserThanLevelSize_ExpectReducedLevelSize() throws UnknownHostException {
        RoutingTable routingTable = new RoutingTable();
        localhost_.setHostPath("0");
        routingTable.setLocalhost(localhost_);
        PGridHost aHost = new PGridHost("127.0.0.1", 3333);
        PGridHost anotherHost = new PGridHost("127.0.0.1", 4444);
        routingTable.addReference(0, aHost);
        routingTable.addReference(0, anotherHost);
        int refMax = routingTable.getLevel(0).size() - 1;
        routingTable.updateLevelRandomly(0, null, refMax);

        Assert.assertTrue(routingTable.levelNumber() == 1);
        Assert.assertTrue(routingTable.getLevel(0).size() == refMax);
        Assert.assertTrue(routingTable.getLevel(0).contains(aHost) || routingTable.getLevel(0).contains(anotherHost));
        Assert.assertFalse(routingTable.getLevel(0).contains(aHost) && routingTable.getLevel(0).contains(anotherHost));
    }

    // public void updateLevelRandomly(int level, Host host, int refMax)
    @Test
    public void WhenUpdatingWithNullHostAndRefmaxEqualToLevelSize_ExpectNoChange() throws UnknownHostException {
        RoutingTable routingTable = new RoutingTable();
        localhost_.setHostPath("0");
        routingTable.setLocalhost(localhost_);
        PGridHost aHost = new PGridHost("127.0.0.1", 3333);
        PGridHost anotherHost = new PGridHost("127.0.0.1", 4444);
        routingTable.addReference(0, aHost);
        routingTable.addReference(0, anotherHost);
        int previousLevelSize = routingTable.getLevel(0).size();

        routingTable.updateLevelRandomly(0, null, 2);

        Assert.assertTrue(routingTable.levelNumber() == 1);
        Assert.assertTrue(routingTable.getLevel(0).size() == previousLevelSize);
        Assert.assertTrue(routingTable.getLevel(0).contains(aHost) && routingTable.getLevel(0).contains(anotherHost));
    }

    // public void update(RoutingTable routingTable, int level)
    @Test
    public void WhenUpdatingWithInfiniteRefMax_ExpectAdditionalHostsPerLevel() throws UnknownHostException {
        int refMax = Integer.MAX_VALUE;

        RoutingTable localRT = new RoutingTable();
        localhost_.setHostPath("0000");
        localRT.setLocalhost(localhost_);
        Host localHost0 = new PGridHost("127.0.0.1", 1110);
        localHost0.setHostPath("10");
        Host localHost1 = new PGridHost("127.0.0.1", 1111);
        localHost1.setHostPath("010");
        Host localHost2 = new PGridHost("127.0.0.1", 1112);
        localHost2.setHostPath("0010");
        localRT.addReference(0, localHost0);
        localRT.addReference(1, localHost1);
        localRT.addReference(2, localHost2);

        RoutingTable remoteRT = new RoutingTable();
        Host remoteHost = new PGridHost("127.0.0.1", 1234);
        remoteHost.setHostPath("0001");
        remoteRT.setLocalhost(remoteHost);
        Host remoteHost0 = new PGridHost("127.0.0.1", 2220);
        remoteHost0.setHostPath("11"); // conjugate of localHost0
        Host remoteHost1 = new PGridHost("127.0.0.1", 2221);
        remoteHost1.setHostPath("011"); // conjugate of localHost1
        Host remoteHost2 = new PGridHost("127.0.0.1", 2222);
        remoteHost2.setHostPath("0011"); // conjugate of localHost2
        remoteRT.addReference(0, remoteHost0);
        remoteRT.addReference(1, remoteHost1);
        remoteRT.addReference(2, remoteHost2);

        int commonLength = localhost_.getHostPath().commonPrefix(remoteHost.getHostPath()).length();

        localRT.update(remoteRT, commonLength, refMax);

        int expectedLevels = 4;
        int expected0levelSize = 2; // "1"
        int expected1levelSize = 2; // "01"
        int expected2levelSize = 2; // "001"
        int expected3levelSize = 1; // "0001"

        Assert.assertTrue(localRT.levelNumber() == expectedLevels);
        Assert.assertTrue(localRT.getLevel(0).size() == expected0levelSize);
        Assert.assertTrue(localRT.getLevel(1).size() == expected1levelSize);
        Assert.assertTrue(localRT.getLevel(2).size() == expected2levelSize);
        Assert.assertTrue(localRT.getLevel(3).size() == expected3levelSize);

        Assert.assertTrue(localRT.getLevel(0).contains(localHost0) && localRT.getLevel(0).contains(remoteHost0));
        Assert.assertTrue(localRT.getLevel(1).contains(localHost1) && localRT.getLevel(1).contains(remoteHost1));
        Assert.assertTrue(localRT.getLevel(2).contains(localHost2) && localRT.getLevel(2).contains(remoteHost2));
        Assert.assertTrue(localRT.getLevel(3).contains(remoteHost));
    }

    // public void update(RoutingTable routingTable, int level)
    @Test
    public void WhenUpdatingAndLocalhostIncreasedHisPath_ExpectLevelsAdded() throws UnknownHostException {
        int refMax = Integer.MAX_VALUE;

        RoutingTable localRT = new RoutingTable();
        String localhostPath = "0000";
        localhost_.setHostPath(localhostPath);
        localRT.setLocalhost(localhost_);
        Host localHost0 = new PGridHost("127.0.0.1", 1110);
        localHost0.setHostPath("10");
        Host localHost1 = new PGridHost("127.0.0.1", 1111);
        localHost1.setHostPath("010");
        Host localHost2 = new PGridHost("127.0.0.1", 1112);
        localHost2.setHostPath("0010");
        localRT.addReference(0, localHost0);
        localRT.addReference(1, localHost1);
        localRT.addReference(2, localHost2);

        RoutingTable remoteRT = new RoutingTable();
        Host remoteHost = new PGridHost("127.0.0.1", 1234);
        remoteHost.setHostPath("0001");
        remoteRT.setLocalhost(remoteHost);
        Host remoteHost0 = new PGridHost("127.0.0.1", 2220);
        remoteHost0.setHostPath("11"); // conjugate of localHost0
        Host remoteHost1 = new PGridHost("127.0.0.1", 2221);
        remoteHost1.setHostPath("011"); // conjugate of localHost1
        Host remoteHost2 = new PGridHost("127.0.0.1", 2222);
        remoteHost2.setHostPath("0011"); // conjugate of localHost2
        remoteRT.addReference(0, remoteHost0);
        remoteRT.addReference(1, remoteHost1);
        remoteRT.addReference(2, remoteHost2);

        int commonLength = localhost_.getHostPath().commonPrefix(remoteHost.getHostPath()).length();

        localhost_.setHostPath(localhostPath + "11");
        localRT.update(remoteRT, commonLength, refMax);

        int expectedLevels = 6;
        int expected0levelSize = 2; // "1"
        int expected1levelSize = 2; // "01"
        int expected2levelSize = 2; // "001"
        int expected3levelSize = 1; // "0001"
        int expected4levelSize = 0; // "0001"
        int expected5levelSize = 0; // "0001"

        Assert.assertTrue(localRT.levelNumber() == expectedLevels);
        Assert.assertTrue(localRT.getLevel(0).size() == expected0levelSize);
        Assert.assertTrue(localRT.getLevel(1).size() == expected1levelSize);
        Assert.assertTrue(localRT.getLevel(2).size() == expected2levelSize);
        Assert.assertTrue(localRT.getLevel(3).size() == expected3levelSize);
        Assert.assertTrue(localRT.getLevel(4).size() == expected4levelSize);
        Assert.assertTrue(localRT.getLevel(5).size() == expected5levelSize);

        Assert.assertTrue(localRT.getLevel(0).contains(localHost0) && localRT.getLevel(0).contains(remoteHost0));
        Assert.assertTrue(localRT.getLevel(1).contains(localHost1) && localRT.getLevel(1).contains(remoteHost1));
        Assert.assertTrue(localRT.getLevel(2).contains(localHost2) && localRT.getLevel(2).contains(remoteHost2));
        Assert.assertTrue(localRT.getLevel(3).contains(remoteHost));
    }

    // public void update(RoutingTable routingTable, int level)
    @Test
    public void WhenUpdatingAndLocalhostDecreasedHisPath_ExpectLevelsAdded() throws UnknownHostException {
        int refMax = Integer.MAX_VALUE;

        RoutingTable localRT = new RoutingTable();
        localhost_.setHostPath("000000");
        localRT.setLocalhost(localhost_);
        Host localHost0 = new PGridHost("127.0.0.1", 1110);
        localHost0.setHostPath("10");
        Host localHost1 = new PGridHost("127.0.0.1", 1111);
        localHost1.setHostPath("010");
        Host localHost2 = new PGridHost("127.0.0.1", 1112);
        localHost2.setHostPath("0010");
        localRT.addReference(0, localHost0);
        localRT.addReference(1, localHost1);
        localRT.addReference(2, localHost2);

        RoutingTable remoteRT = new RoutingTable();
        Host remoteHost = new PGridHost("127.0.0.1", 1234);
        remoteHost.setHostPath("0001");
        remoteRT.setLocalhost(remoteHost);
        Host remoteHost0 = new PGridHost("127.0.0.1", 2220);
        remoteHost0.setHostPath("11"); // conjugate of localHost0
        Host remoteHost1 = new PGridHost("127.0.0.1", 2221);
        remoteHost1.setHostPath("011"); // conjugate of localHost1
        Host remoteHost2 = new PGridHost("127.0.0.1", 2222);
        remoteHost2.setHostPath("0011"); // conjugate of localHost2
        remoteRT.addReference(0, remoteHost0);
        remoteRT.addReference(1, remoteHost1);
        remoteRT.addReference(2, remoteHost2);

        int commonLength = localhost_.getHostPath().commonPrefix(remoteHost.getHostPath()).length();

        localhost_.setHostPath("000");
        localRT.update(remoteRT, commonLength, refMax);

        int expectedLevels = 3;
        int expected0levelSize = 2; // "1"
        int expected1levelSize = 2; // "01"
        int expected2levelSize = 2; // "001"

        Assert.assertTrue(localRT.levelNumber() == expectedLevels);
        Assert.assertTrue(localRT.getLevel(0).size() == expected0levelSize);
        Assert.assertTrue(localRT.getLevel(1).size() == expected1levelSize);
        Assert.assertTrue(localRT.getLevel(2).size() == expected2levelSize);

        Assert.assertTrue(localRT.getLevel(0).contains(localHost0) && localRT.getLevel(0).contains(remoteHost0));
        Assert.assertTrue(localRT.getLevel(1).contains(localHost1) && localRT.getLevel(1).contains(remoteHost1));
        Assert.assertTrue(localRT.getLevel(2).contains(localHost2) && localRT.getLevel(2).contains(remoteHost2));
        Assert.assertFalse(localRT.contains(remoteHost));
    }

    // public void update(RoutingTable routingTable, int level)
    @Test
    public void WhenUpdatingWithSmallRefMax_ExpectLevelSizeLessEqualToRefMax() throws UnknownHostException {
        int refMax = 1;

        RoutingTable localRT = new RoutingTable();
        localhost_.setHostPath("0000");
        localRT.setLocalhost(localhost_);
        Host localHost0 = new PGridHost("127.0.0.1", 1110);
        localHost0.setHostPath("10");
        Host localHost1 = new PGridHost("127.0.0.1", 1111);
        localHost1.setHostPath("010");
        Host localHost2 = new PGridHost("127.0.0.1", 1112);
        localHost2.setHostPath("0010");
        localRT.addReference(0, localHost0);
        localRT.addReference(1, localHost1);
        localRT.addReference(2, localHost2);

        RoutingTable remoteRT = new RoutingTable();
        Host remoteHost = new PGridHost("127.0.0.1", 1234);
        remoteHost.setHostPath("0001");
        remoteRT.setLocalhost(remoteHost);
        Host remoteHost0 = new PGridHost("127.0.0.1", 2220);
        remoteHost0.setHostPath("11"); // conjugate of localHost0
        Host remoteHost1 = new PGridHost("127.0.0.1", 2221);
        remoteHost1.setHostPath("011"); // conjugate of localHost1
        Host remoteHost2 = new PGridHost("127.0.0.1", 2222);
        remoteHost2.setHostPath("0011"); // conjugate of localHost2
        remoteRT.addReference(0, remoteHost0);
        remoteRT.addReference(1, remoteHost1);
        remoteRT.addReference(2, remoteHost2);

        int commonLength = localhost_.getHostPath().commonPrefix(remoteHost.getHostPath()).length();

        localRT.update(remoteRT, commonLength, refMax);

        int expectedLevels = 4;

        Assert.assertTrue(localRT.levelNumber() == expectedLevels);
        Assert.assertTrue(localRT.getLevel(0).size() == refMax); // "1"
        Assert.assertTrue(localRT.getLevel(1).size() == refMax); // "01"
        Assert.assertTrue(localRT.getLevel(2).size() == refMax); // "001"
        Assert.assertTrue(localRT.getLevel(3).size() == refMax); // "0001"

        Assert.assertTrue(localRT.getLevel(0).contains(localHost0) || localRT.getLevel(0).contains(remoteHost0));
        Assert.assertFalse(localRT.getLevel(0).contains(localHost0) && localRT.getLevel(0).contains(remoteHost0));

        Assert.assertTrue(localRT.getLevel(1).contains(localHost1) || localRT.getLevel(1).contains(remoteHost1));
        Assert.assertFalse(localRT.getLevel(1).contains(localHost1) && localRT.getLevel(1).contains(remoteHost1));

        Assert.assertTrue(localRT.getLevel(2).contains(localHost2) || localRT.getLevel(2).contains(remoteHost2));
        Assert.assertFalse(localRT.getLevel(2).contains(localHost2) && localRT.getLevel(2).contains(remoteHost2));

        Assert.assertTrue(localRT.getLevel(3).contains(remoteHost));
    }
}
