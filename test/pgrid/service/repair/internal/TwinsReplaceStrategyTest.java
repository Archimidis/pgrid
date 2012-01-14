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
import pgrid.service.repair.spi.ReplaceStrategy;

import java.net.UnknownHostException;

/**
 * @author Vourlakis Nikolas <nvourlakis@gmail.com>
 */
public class TwinsReplaceStrategyTest {

    private static final int MAX_REF = Integer.MAX_VALUE;

    @Test(expected = NullPointerException.class)
    public void WhenReplacingNullRoutingTable_ExpectException() throws UnknownHostException {
        ReplaceStrategy replace = new TwinsReplaceStrategy(MAX_REF);
        replace.execute(null, new PGridPath("00"));
    }

    @Test(expected = NullPointerException.class)
    public void WhenReplacingNullFailedPath_ExpectException() throws UnknownHostException {
        ReplaceStrategy replace = new TwinsReplaceStrategy(MAX_REF);
        replace.execute(new RoutingTable(), null);
    }

    @Test
    public void WhenReplacingWith2HostsInNetwork_ExpectLocalHoldingAllKeyspace() throws UnknownHostException {
        String expected = "";

        Host localhost = new PGridHost("127.0.0.1", 3000);
        localhost.setHostPath("0");
        RoutingTable localRT = new RoutingTable();
        localRT.setLocalhost(localhost);

        PGridPath failedPath = new PGridPath("1");

        ReplaceStrategy localReplace = new TwinsReplaceStrategy(MAX_REF);
        localReplace.execute(localRT, failedPath);

        Assert.assertTrue(localRT.getLocalhost().getHostPath().toString().compareTo(expected) == 0);
    }

    @Test
    public void WhenReplacing_ExpectLocalReducedPath() throws UnknownHostException {
        String expected = "0";

        Host localhost = new PGridHost("127.0.0.1", 3000);
        localhost.setHostPath("00");
        RoutingTable localRT = new RoutingTable();
        localRT.setLocalhost(localhost);

        PGridPath failedPath = new PGridPath("01");

        ReplaceStrategy localReplace = new TwinsReplaceStrategy(MAX_REF);
        localReplace.execute(localRT, failedPath);

        Assert.assertTrue(localRT.getLocalhost().getHostPath().toString().compareTo(expected) == 0);
    }

    @Test
    public void WhenReplacingHavingConjugate_ExpectResults() throws UnknownHostException {
        String expectedLocal = "0";
        String expectedRemote = "1";

        Host localhost = new PGridHost("127.0.0.1", 3000);
        localhost.setHostPath("00");
        RoutingTable localRT = new RoutingTable();
        localRT.setLocalhost(localhost);

        Host remoteHost = new PGridHost("127.0.0.1", 4000);
        remoteHost.setHostPath("01");
        RoutingTable remoteRT = new RoutingTable();
        remoteRT.setLocalhost(remoteHost);

        PGridPath failedPath = new PGridPath("1");

        ReplaceStrategy localReplace = new TwinsReplaceStrategy(MAX_REF);
        localReplace.execute(localRT, failedPath);

        ReplaceStrategy remoteReplace = new TwinsReplaceStrategy(MAX_REF);
        remoteReplace.execute(remoteRT, failedPath);

        Assert.assertTrue(localRT.getLocalhost().getHostPath().toString().compareTo(expectedLocal) == 0);
        Assert.assertTrue(remoteRT.getLocalhost().getHostPath().toString().compareTo(expectedRemote) == 0);
    }
}
