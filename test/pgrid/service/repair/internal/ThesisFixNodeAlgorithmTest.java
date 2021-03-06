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

package pgrid.service.repair.internal;

import org.junit.Assert;
import org.junit.Test;
import pgrid.entity.Host;
import pgrid.entity.PGridPath;
import pgrid.entity.internal.PGridHost;
import pgrid.entity.routingtable.RoutingTable;
import pgrid.service.repair.spi.FindContinuationAlgorithm;

import java.net.UnknownHostException;
import java.util.List;

/**
 * @author Vourlakis Nikolas
 */
public class ThesisFixNodeAlgorithmTest {

    @Test(expected = NullPointerException.class)
    public void WhenExecutingWithNullRoutingTable_ExpectException() throws UnknownHostException {
        FindContinuationAlgorithm algorithm = new ThesisFindContinuationAlgorithm();
        algorithm.execute(null, new PGridPath(""));
    }

    @Test(expected = NullPointerException.class)
    public void WhenExecutingWithNullPath_ExpectException() throws UnknownHostException {
        RoutingTable rt = new RoutingTable();
        FindContinuationAlgorithm algorithm = new ThesisFindContinuationAlgorithm();
        algorithm.execute(rt, null);
    }

    @Test
    public void WhenFailedPath01_ExpectHostWithPrefix001() throws UnknownHostException {
        // localhost: "000"
        // failed: "01"
        // resulted prefix: "001"
        RoutingTable routingTable = new RoutingTable();
        Host localhost = new PGridHost("127.0.0.1", 3000);
        localhost.setHostPath("000");
        routingTable.setLocalhost(localhost);

        Host host1 = new PGridHost("127.0.0.1", 1111);
        host1.setHostPath("1");
        Host host2 = new PGridHost("127.0.0.1", 2222);
        host2.setHostPath("01");
        Host host3 = new PGridHost("127.0.0.1", 3333);
        host3.setHostPath("0010");
        Host host4 = new PGridHost("127.0.0.1", 4444);
        host4.setHostPath("0011");

        routingTable.addReference(0, host1);
        routingTable.addReference(1, host2);
        routingTable.addReference(2, host3);
        routingTable.addReference(2, host4);


        PGridPath failedHostPath = new PGridPath("01");

        String root = failedHostPath.subPath(0, failedHostPath.length() - 1);
        char lastChar = failedHostPath.value(failedHostPath.length() - 1);
        PGridPath initialPath = new PGridPath(root);
        initialPath.revertAndAppend(lastChar);

        FindContinuationAlgorithm algorithm = new ThesisFindContinuationAlgorithm();
        List<Host> toContinue = algorithm.execute(routingTable, initialPath);

        Assert.assertTrue(toContinue.size() == 2);
        for (Host host : toContinue) {
            Assert.assertTrue(host.getHostPath().hasPrefix(new PGridPath("001")));
        }
    }

    @Test
    public void WhenFailedPath0011_ExpectHostWithPrefix001() throws UnknownHostException {
        // localhost: "000"
        // failed: "0011"
        // resulted prefix: "001"
        RoutingTable routingTable = new RoutingTable();
        Host localhost = new PGridHost("127.0.0.1", 3000);
        localhost.setHostPath("000");
        routingTable.setLocalhost(localhost);

        Host host1 = new PGridHost("127.0.0.1", 1111);
        host1.setHostPath("1");
        Host host2 = new PGridHost("127.0.0.1", 2222);
        host2.setHostPath("01");
        Host host3 = new PGridHost("127.0.0.1", 3333);
        host3.setHostPath("0010");
        Host host4 = new PGridHost("127.0.0.1", 4444);
        host4.setHostPath("0011");

        routingTable.addReference(0, host1);
        routingTable.addReference(1, host2);
        routingTable.addReference(2, host3);
        routingTable.addReference(2, host4);

        Host failed = host4;
        PGridPath failedHostPath = failed.getHostPath();

        String root = failedHostPath.subPath(0, failedHostPath.length() - 1);
        char lastChar = failedHostPath.value(failedHostPath.length() - 1);
        PGridPath initialPath = new PGridPath(root);
        initialPath.revertAndAppend(lastChar);

        FindContinuationAlgorithm algorithm = new ThesisFindContinuationAlgorithm();
        List<Host> toContinue = algorithm.execute(routingTable, initialPath);

        Assert.assertTrue(toContinue.size() == 2);
        for (Host host : toContinue) {
            Assert.assertTrue(host.getHostPath().hasPrefix(new PGridPath("001")));
        }
    }

    @Test
    public void WhenFailedPath1_ExpectHostWithPrefix001() throws UnknownHostException {
        // localhost: "000"
        // failed: "1"
        // resulted prefix: "001"
        RoutingTable routingTable = new RoutingTable();
        Host localhost = new PGridHost("127.0.0.1", 3000);
        localhost.setHostPath("000");
        routingTable.setLocalhost(localhost);

        Host host1 = new PGridHost("127.0.0.1", 1111);
        host1.setHostPath("1");
        Host host2 = new PGridHost("127.0.0.1", 2222);
        host2.setHostPath("01");
        Host host3 = new PGridHost("127.0.0.1", 3333);
        host3.setHostPath("0010");
        Host host4 = new PGridHost("127.0.0.1", 4444);
        host4.setHostPath("0011");

        routingTable.addReference(0, host1);
        routingTable.addReference(1, host2);
        routingTable.addReference(2, host3);
        routingTable.addReference(2, host4);

        Host failed = host1;
        PGridPath failedHostPath = failed.getHostPath();

        String root = failedHostPath.subPath(0, failedHostPath.length() - 1);
        char lastChar = failedHostPath.value(failedHostPath.length() - 1);
        PGridPath initialPath = new PGridPath(root);
        initialPath.revertAndAppend(lastChar);

        FindContinuationAlgorithm algorithm = new ThesisFindContinuationAlgorithm();
        List<Host> toContinue = algorithm.execute(routingTable, initialPath);

        Assert.assertTrue(toContinue.size() == 2);
        for (Host host : toContinue) {
            Assert.assertTrue(host.getHostPath().hasPrefix(new PGridPath("001")));
        }
    }

    @Test
    public void WhenFailedPath000_ExpectHostWithPrefix001() throws UnknownHostException {
        // localhost: "0010"
        // failed: "000"
        // resulted prefix: "001"
        // selected host: "0010" = the localhost must solve it
        RoutingTable routingTable = new RoutingTable();
        Host localhost = new PGridHost("127.0.0.1", 3000);
        localhost.setHostPath("0010");
        routingTable.setLocalhost(localhost);

        Host host1 = new PGridHost("127.0.0.1", 1111);
        host1.setHostPath("1");
        Host host2 = new PGridHost("127.0.0.1", 2222);
        host2.setHostPath("01");
        Host host3 = new PGridHost("127.0.0.1", 3333);
        host3.setHostPath("000");
        Host host4 = new PGridHost("127.0.0.1", 4444);
        host4.setHostPath("0011");

        routingTable.addReference(0, host1);
        routingTable.addReference(1, host2);
        routingTable.addReference(2, host3);
        routingTable.addReference(3, host4);

        Host failed = host3;
        PGridPath failedHostPath = failed.getHostPath();

        String root = failedHostPath.subPath(0, failedHostPath.length() - 1);
        char lastChar = failedHostPath.value(failedHostPath.length() - 1);
        PGridPath initialPath = new PGridPath(root);
        initialPath.revertAndAppend(lastChar);

        FindContinuationAlgorithm algorithm = new ThesisFindContinuationAlgorithm();
        List<Host> toContinue = algorithm.execute(routingTable, initialPath);

        Assert.assertTrue(toContinue.size() == 1);
        for (Host host : toContinue) {
            Assert.assertTrue(host.getHostPath().hasPrefix(new PGridPath("001")));
        }
    }
}
