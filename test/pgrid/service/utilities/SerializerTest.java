/*
 * This file (pgrid.service.utilities.SerializerTest) is part of the libpgrid project.
 *
 * Copyright (c) 2011. Vourlakis Nikolas. All rights reserved.
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

package pgrid.service.utilities;

import org.junit.Assert;
import org.junit.Test;
import pgrid.entity.Host;
import pgrid.entity.internal.PGridHost;
import pgrid.entity.routingtable.RoutingTable;
import pgrid.service.spi.corba.CorbaRoutingTable;
import pgrid.service.spi.corba.PeerReference;

import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * @author Vourlakis Nikolas
 */
public class SerializerTest {

    // public static PeerReference serializeHost(Host host)
    @Test(expected = NullPointerException.class)
    public void WhenSerializingNullHost_ExpectException() {
        Serializer.serializeHost(null);
    }

    // public static PeerReference serializeHost(Host host)
    @Test
    public void WhenSerializingValidHost_ExpectResults() throws UnknownHostException {
        String expectedIP = "127.0.0.1";
        int expectedPort = 3000;
        String expectedPath = "00";
        String expectedUUID = "12345678-1231-1234-1234-0123456789ad";
        long expectedTS = 0;

        Host host = new PGridHost(expectedIP, expectedPort);
        host.setHostPath(expectedPath);
        host.setUUID(UUID.fromString(expectedUUID));
        host.resetTimestamp();

        PeerReference ref = Serializer.serializeHost(host);
        Assert.assertTrue(ref.address.compareTo(expectedIP) == 0);
        Assert.assertTrue(ref.port == expectedPort);
        Assert.assertTrue(ref.path.compareTo(expectedPath) == 0);
        Assert.assertTrue(ref.uuid.compareTo(expectedUUID) == 0);
        Assert.assertTrue(ref.timestamp == expectedTS);

    }

    // public static pgrid.service.spi.corba.RoutingTable serializeRoutingTable(RoutingTable routingTable)
    @Test(expected = NullPointerException.class)
    public void WhenSerializingNullRoutingTable_ExpectException() {
        Serializer.serializeRoutingTable(null);
    }

    // public static pgrid.service.spi.corba.RoutingTable serializeRoutingTable(RoutingTable routingTable)
    @Test(expected = IllegalStateException.class)
    public void WhenSerializingRoutingTableWithUninitializedLocalhost_ExpectException() {
        RoutingTable routingTable = new RoutingTable();
        Serializer.serializeRoutingTable(routingTable);
    }

    // public static pgrid.service.spi.corba.RoutingTable serializeRoutingTable(RoutingTable routingTable)
    @Test
    public void WhenSerializingValidRoutingTable_ExpectResults() throws UnknownHostException {
        int levels = 2;
        int levelSize = 2;

        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < levels; i++) {
            builder.append("0");
        }

        String expectedLocalIP = "127.0.0.1";
        int expectedLocalPort = 3000;
        String expectedLocalPath = builder.toString();
        String expectedLocalUUID = "12345678-1231-1234-1234-0123456789ad";
        long expectedLocalTS = 0;

        Host localhost = new PGridHost(expectedLocalIP, expectedLocalPort);
        localhost.setHostPath(expectedLocalPath);
        localhost.setUUID(UUID.fromString(expectedLocalUUID));
        localhost.resetTimestamp();
        RoutingTable routingTable = new RoutingTable();

        routingTable.setLocalhost(localhost);

        List<List<PeerReference>> expectedRefs = new ArrayList<List<PeerReference>>(levels);
        for (int i = 0; i < levels; i++) {
            List<PeerReference> levelList = new ArrayList<PeerReference>(levelSize);
            for (int j = 0; j < levelSize; j++) {
                PGridHost host = new PGridHost("127.0.0.1", (j + 1) * 1000);
                routingTable.addReference(i, host);
                levelList.add(new PeerReference(
                        host.getAddress().getHostAddress(),
                        host.getPort(),
                        host.getHostPath().toString(),
                        host.getTimestamp(),
                        host.getUUID().toString()));
            }
            expectedRefs.add(levelList);
        }

        CorbaRoutingTable corbaRoutingTable =
                Serializer.serializeRoutingTable(routingTable);

        PeerReference localRef = corbaRoutingTable.peer;
        Assert.assertTrue(localRef.address.compareTo(expectedLocalIP) == 0);
        Assert.assertTrue(localRef.port == expectedLocalPort);
        Assert.assertTrue(localRef.path.compareTo(expectedLocalPath) == 0);
        Assert.assertTrue(localRef.uuid.compareTo(expectedLocalUUID) == 0);
        Assert.assertTrue(localRef.timestamp == expectedLocalTS);

        PeerReference[][] references = corbaRoutingTable.refs;

        Assert.assertTrue(references.length == levels);
        for (int i = 0; i < references.length; i++) {
            Assert.assertTrue(references[i].length == levelSize);
            for (int j = 0; j < references[i].length; j++) {
                List<PeerReference> expectedLevel = expectedRefs.get(i);
                Assert.assertTrue(references[i].length == expectedLevel.size());
                for (PeerReference peer : expectedLevel) {
                    if (peer.uuid.compareTo(references[i][j].uuid) == 0) {
                        Assert.assertTrue(peer.address.compareTo(references[i][j].address) == 0);
                        Assert.assertTrue(peer.port == references[i][j].port);
                        Assert.assertTrue(peer.path.compareTo(references[i][j].path) == 0);
                        Assert.assertTrue(peer.uuid.compareTo(references[i][j].uuid) == 0);
                        Assert.assertTrue(peer.timestamp == references[i][j].timestamp);
                    }
                }
            }
        }
    }
}
