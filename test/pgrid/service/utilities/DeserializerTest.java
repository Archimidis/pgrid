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

package pgrid.service.utilities;

import org.junit.Assert;
import org.junit.Test;
import pgrid.entity.Host;
import pgrid.entity.internal.PGridHost;
import pgrid.entity.routingtable.RoutingTable;
import pgrid.service.spi.corba.CorbaRoutingTable;
import pgrid.service.spi.corba.PeerReference;

import java.net.UnknownHostException;
import java.util.Collection;
import java.util.UUID;

/**
 * @author Vourlakis Nikolas
 */
public class DeserializerTest {

    // public static PGridHost deserializeHost(PeerReference ref)
    @Test(expected = NullPointerException.class)
    public void WhenDeserializingNullReference_ExpectException() {
        Deserializer.deserializeHost(null);
    }

    // public static PGridHost deserializeHost(PeerReference ref)
    @Test
    public void WhenDeserializingValidReference_ExpectResults() {
        String expectedIP = "127.0.0.1";
        int expectedPort = 3000;
        String expectedPath = "00";
        String expectedUUID = "12345678-1231-1234-1234-0123456789ad";
        long expectedTS = 0;

        PeerReference ref = new PeerReference(
                expectedIP, expectedPort, expectedPath, expectedTS, expectedUUID);

        Host host = Deserializer.deserializeHost(ref);
        Assert.assertTrue(host.getAddress().getHostAddress().compareTo(expectedIP) == 0);
        Assert.assertTrue(host.getPort() == expectedPort);
        Assert.assertTrue(host.getHostPath().toString().compareTo(expectedPath) == 0);
        Assert.assertTrue(host.getUUID().toString().compareTo(expectedUUID) == 0);
        Assert.assertTrue(host.getTimestamp() == expectedTS);
    }

    // public static RoutingTable deserializeRoutingTable(pgrid.service.spi.corba.RoutingTable corbaRoutingTable)
    @Test(expected = NullPointerException.class)
    public void WhenDeserializingNullRoutingTable_ExpectException() {
        Deserializer.deserializeRoutingTable(null);
    }

    // public static RoutingTable deserializeRoutingTable(pgrid.service.spi.corba.RoutingTable corbaRoutingTable)
    @Test
    public void WhenDeserializingValidRoutingTable_ExpectResults() throws UnknownHostException {
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

        RoutingTable expectedRT = new RoutingTable();
        expectedRT.setLocalhost(localhost);

        PeerReference[][] levelReferences = new PeerReference[levels][levelSize];

        int levelIndex = 0;
        for (int i = 0; i < levels; i++) {
            for (int j = 0; j < levelSize; j++) {
                Host host = new PGridHost("127.0.0.1", (j + 1) * 1000);
                levelReferences[i][j] = new PeerReference(
                        host.getAddress().getHostAddress(),
                        host.getPort(),
                        host.getHostPath().toString(),
                        host.getTimestamp(),
                        host.getUUID().toString());

                expectedRT.addReference(levelIndex, host);
            }
            levelIndex++;
        }
        PeerReference localRef = new PeerReference(
                expectedLocalIP, expectedLocalPort, expectedLocalPath, expectedLocalTS, expectedLocalUUID);
        CorbaRoutingTable corbaRT = new CorbaRoutingTable(levelReferences, localRef);

        RoutingTable actualRT = Deserializer.deserializeRoutingTable(corbaRT);


        Assert.assertTrue(expectedRT.levelNumber() == actualRT.levelNumber());

        levelIndex = 0;
        for (Collection<Host> actualLevel : actualRT.getAllHostsByLevels()) {
            Collection<Host> expectedLevel = expectedRT.getLevel(levelIndex);
            Assert.assertTrue(expectedLevel.size() == actualLevel.size());
            for (Host actualHost : actualLevel) {
                Assert.assertTrue(expectedRT.contains(actualHost));
                for (Host expectedHost : expectedLevel) {
                    if (expectedHost.compareTo(actualHost) == 0) {
                        Assert.assertTrue(expectedHost.getAddress().getHostAddress()
                                .compareTo(actualHost.getAddress().getHostAddress()) == 0);
                        Assert.assertTrue(expectedHost.getPort() == actualHost.getPort());
                        Assert.assertTrue(expectedHost.getHostPath().toString()
                                .compareTo(actualHost.getHostPath().toString()) == 0);
                        Assert.assertTrue(expectedHost.getUUID().compareTo(actualHost.getUUID()) == 0);
                        Assert.assertTrue(expectedHost.getTimestamp() == actualHost.getTimestamp());
                    }
                }
            }
            levelIndex++;
        }
    }
}
