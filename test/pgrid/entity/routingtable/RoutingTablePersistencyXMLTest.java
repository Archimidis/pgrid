/*
 * This file (pgrid.entity.routingtable.RoutingTablePersistencyXMLTest) is part of the libpgrid project.
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

import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;
import pgrid.entity.Host;
import pgrid.entity.internal.PGridHost;
import pgrid.entity.routingtable.internal.XMLPersistenceDelegate;

import java.io.*;
import java.util.*;

/**
 * @author Vourlakis Nikolas
 */
public class RoutingTablePersistencyXMLTest {
    private static final String VALID_FILE = "test/pgrid/entity/routingtable/validRoutingTable.xml";
    private static final String INVALID_ADDRESS_FILE = "test/pgrid/entity/routingtable/addrInvalidRoutingTable.xml";
    private static final String INVALID_PORT_FILE = "test/pgrid/entity/routingtable/portInvalidRoutingTable.xml";
    private static final String INVALID_UUID_FILE = "test/pgrid/entity/routingtable/uuidInvalidRoutingTable.xml";

    private static Map<Integer, List<Host>> hostList_;
    private static Host localpeer_;
    private static PersistenceDelegate persistenceDelegate_;

    @BeforeClass
    public static void beforeClass() throws IOException {
        persistenceDelegate_ = new XMLPersistenceDelegate();

        RoutingTable routingTable = new RoutingTable();

        localpeer_ = new PGridHost("localhost", 3000);
        localpeer_.setHostPath("000");
        localpeer_.setTimestamp(10000);
        localpeer_.setUUID(UUID.fromString("12345678-1231-1234-1234-0123456789ad"));

        routingTable.setLocalhost(localpeer_);

        hostList_ = new HashMap<Integer, List<Host>>(3);
        List<Host> levelList = new ArrayList<Host>();
        int level;

        //******* Level 0 *******//
        level = 0;
        Host host = new PGridHost("localhost", 3000);
        host.setHostPath("1");
        host.setTimestamp(0);
        host.setUUID(UUID.fromString("10d82159-3713-4b78-884e-4104720ae2d8"));
        levelList.add(host);

        host = new PGridHost("localhost", 3000);
        host.setHostPath("11");
        host.setTimestamp(0);
        host.setUUID(UUID.fromString("5a29fac1-bb15-4cdd-a071-f33f2b849875"));
        levelList.add(host);

        hostList_.put(level, levelList);
        routingTable.addReference(level, levelList);

        //******* Level 1 *******//
        levelList = new ArrayList<Host>();
        level = 1;

        host = new PGridHost("localhost", 3001);
        host.setHostPath("011");
        host.setTimestamp(1);
        host.setUUID(UUID.fromString("463dad77-627d-4a5d-930b-aab6a4523490"));
        levelList.add(host);

        host = new PGridHost("localhost", 3001);
        host.setHostPath("0111");
        host.setTimestamp(1);
        host.setUUID(UUID.fromString("491fad77-627d-4a5d-930b-aab6a1234567"));
        levelList.add(host);

        host = new PGridHost("localhost", 3001);
        host.setHostPath("01111");
        host.setTimestamp(1);
        host.setUUID(UUID.fromString("ac49991e-68a0-42ad-9c0e-ab328f193e4e"));
        levelList.add(host);

        hostList_.put(level, levelList);
        routingTable.addReference(level, levelList);

        //******* Level 2 *******//
        levelList = new ArrayList<Host>();
        level = 2;

        host = new PGridHost("localhost", 3002);
        host.setHostPath("0011");
        host.setTimestamp(2);
        host.setUUID(UUID.fromString("5a29fac1-bb15-4cdd-a071-f33f2b847349"));
        levelList.add(host);

        hostList_.put(level, levelList);
        routingTable.addReference(level, levelList);

        // Store
        PersistenceDelegate p = new XMLPersistenceDelegate();
        try {
            p.store(VALID_FILE, routingTable);
        } catch (FileNotFoundException e) {
        }

        //************ Invalid File Construction ************//

        OutputStream out = new FileOutputStream(INVALID_ADDRESS_FILE);
        String invalidAddress = new StringBuilder()
                .append("<?xml version=\"1.0\" encoding=\"UTF-8\"?>")
                .append("\n<RoutingTable>")
                .append("\n\t<localpeer>")
                .append("\n\t\t<address>-1</address>") // Error: Invalid ip address
                .append("\n\t\t<port>3000</port>")
                .append("\n\t\t<path>000</path>")
                .append("\n\t\t<timestamp>10001</timestamp>")
                .append("\n\t\t<uuid>12345678-1231-1234-1234-0123456789ad</uuid>")
                .append("\n\t</localpeer>")
                .append("\n</RoutingTable>").toString();
        out.write(invalidAddress.getBytes());
        out.close();

        out = new FileOutputStream(INVALID_PORT_FILE);
        String invalidPort = new StringBuilder()
                .append("<?xml version=\"1.0\" encoding=\"UTF-8\"?>")
                .append("\n<RoutingTable>")
                .append("\n\t<localpeer>")
                .append("\n\t\t<address>localhost</address>")
                .append("\n\t\t<port>-1</port>") // Error: Negative port
                .append("\n\t\t<path>000</path>")
                .append("\n\t\t<timestamp>10001</timestamp>")
                .append("\n\t\t<uuid>12345678-1231-1234-1234-0123456789ad</uuid>")
                .append("\n\t</localpeer>")
                .append("\n</RoutingTable>").toString();
        out.write(invalidPort.getBytes());
        out.close();

        out = new FileOutputStream(INVALID_UUID_FILE);
        String invalidUUID = new StringBuilder()
                .append("<?xml version=\"1.0\" encoding=\"UTF-8\"?>")
                .append("\n<RoutingTable>")
                .append("\n\t<localpeer>")
                .append("\n\t\t<address>localhost</address>")
                .append("\n\t\t<port>3000</port>")
                .append("\n\t\t<path>000</path>")
                .append("\n\t\t<timestamp>10001</timestamp>")
                .append("\n\t\t<uuid>sdfg457h*(</uuid>") // Error: invalid UUID format
                .append("\n\t</localpeer>")
                .append("\n</RoutingTable>").toString();
        out.write(invalidUUID.getBytes());
        out.close();
    }

    @AfterClass
    public static void afterClass() throws Exception {
        File file = new File(VALID_FILE);
        if (!file.delete()) {
            throw new Exception("Finished testing for routing table persistency" +
                    "and trying to cleanup resources used, " +
                    "but for some reason the file " + VALID_FILE + " could not be deleted.");
        }
        file = new File(INVALID_ADDRESS_FILE);
        if (!file.delete()) {
            throw new Exception("Finished testing for routing table persistency" +
                    "and trying to cleanup resources used, " +
                    "but for some reason the file " + INVALID_ADDRESS_FILE + " could not be deleted.");
        }
        file = new File(INVALID_PORT_FILE);
        if (!file.delete()) {
            throw new Exception("Finished testing for routing table persistency" +
                    "and trying to cleanup resources used, " +
                    "but for some reason the file " + INVALID_PORT_FILE + " could not be deleted.");
        }
        file = new File(INVALID_UUID_FILE);
        if (!file.delete()) {
            throw new Exception("Finished testing for routing table persistency" +
                    "and trying to cleanup resources used, " +
                    "but for some reason the file " + INVALID_UUID_FILE + " could not be deleted.");
        }
    }

    @Test
    public void WhenLoadingFromExistingFile_ExpectResultedRoutingTable() {
        RoutingTable routingTable = new RoutingTable();

        try {
            persistenceDelegate_.load(VALID_FILE, routingTable);
        } catch (FileNotFoundException e) {
        } catch (PersistencyException e) {
        }

        Host localpeer = routingTable.getLocalhost();

        Assert.assertTrue(localpeer_.compareTo(localpeer) == 0);
        Assert.assertTrue(hostList_.size() == routingTable.levelNumber());

        for (int levelIndex = 0; levelIndex < hostList_.size(); levelIndex++) {
            Collection<Host> actual = routingTable.getLevel(levelIndex);
            Collection<Host> expected = hostList_.get(levelIndex);
            Assert.assertTrue(expected.size() == actual.size());
            for (Host actualHost : actual) {
                Assert.assertTrue(expected.contains(actualHost));
            }
        }
    }

    @Test(expected = PersistencyException.class)
    public void WhenLoadingFromFileWithInvalidAddress_ExpectPersistencyException() throws PersistencyException {
        RoutingTable routingTable = new RoutingTable();
        try {
            persistenceDelegate_.load(INVALID_ADDRESS_FILE, routingTable);
        } catch (FileNotFoundException e) {
        }
    }

    @Test(expected = PersistencyException.class)
    public void WhenLoadingFromFileWithInvalidPort_ExpectIllegalArgumentException() throws PersistencyException {
        RoutingTable routingTable = new RoutingTable();
        try {
            persistenceDelegate_.load(INVALID_PORT_FILE, routingTable);
        } catch (FileNotFoundException e) {
        }
    }

    @Test(expected = PersistencyException.class)
    public void WhenLoadingFromFileWithInvalidUUID_ExpectIllegalArgumentException() throws PersistencyException {
        RoutingTable routingTable = new RoutingTable();
        try {
            persistenceDelegate_.load(INVALID_UUID_FILE, routingTable);
        } catch (FileNotFoundException e) {
        }
    }
}
