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
import pgrid.entity.internal.PGridHost;
import pgrid.entity.routingtable.RoutingTable;
import pgrid.service.repair.spi.ReplaceStrategy;

import java.net.UnknownHostException;

/**
 * @author Vourlakis Nikolas <nvourlakis@gmail.com>
 */
public class TwinsReplaceStrategyTest {

    @Test(expected = NullPointerException.class)
    public void WhenReplacingLocallyNullRoutingTable_ExpectException() throws UnknownHostException {
        ReplaceStrategy replace = new TwinsReplaceStrategy();
        replace.execute(null, new PGridHost("127.0.0.1", 1111));
    }

    @Test(expected = NullPointerException.class)
    public void WhenReplacingLocallyNullFailedHost_ExpectException() throws UnknownHostException {
        ReplaceStrategy replace = new TwinsReplaceStrategy();
        replace.execute(new RoutingTable(), null);
    }

    @Test
    public void WhenReplacingLocallyWith2HostsInNetwork_ExpectLocalHoldingAllKeyspace() throws UnknownHostException {
        String expected = "";

        Host localhost = new PGridHost("127.0.0.1", 3000);
        localhost.setHostPath("0");
        RoutingTable localRT = new RoutingTable();
        localRT.setLocalhost(localhost);

        Host failed = new PGridHost("127.0.0.1", 1111);
        failed.setHostPath("1");

        ReplaceStrategy localReplace = new TwinsReplaceStrategy();
        localReplace.execute(localRT, failed);

        Assert.assertTrue(localRT.getLocalhost().getHostPath().toString().compareTo(expected) == 0);
    }

    @Test
    public void WhenReplacingLocally_ExpectLocalReducedPath() throws UnknownHostException {
        String expected = "0";

        Host localhost = new PGridHost("127.0.0.1", 3000);
        localhost.setHostPath("00");
        RoutingTable localRT = new RoutingTable();
        localRT.setLocalhost(localhost);

        Host failed = new PGridHost("127.0.0.1", 1111);
        failed.setHostPath("01");

        ReplaceStrategy localReplace = new TwinsReplaceStrategy();
        localReplace.execute(localRT, failed);

        Assert.assertTrue(localRT.getLocalhost().getHostPath().toString().compareTo(expected) == 0);
    }

    @Test(expected = NullPointerException.class)
    public void WhenReplacingWithConjugateNullRoutingTable_ExpectException() throws UnknownHostException {
        ReplaceStrategy replace = new TwinsReplaceStrategy();
        replace.execute(null, new PGridHost("127.0.0.1", 3000), new PGridHost("127.0.0.1", 1111));
    }

    @Test(expected = NullPointerException.class)
    public void WhenReplacingWithConjugateNullConjugateHost_ExpectException() throws UnknownHostException {
        ReplaceStrategy replace = new TwinsReplaceStrategy();
        replace.execute(new RoutingTable(), null, new PGridHost("127.0.0.1", 1111));
    }

    @Test(expected = NullPointerException.class)
    public void WhenReplacingWithConjugateNullFailedHost_ExpectException() throws UnknownHostException {
        ReplaceStrategy replace = new TwinsReplaceStrategy();
        replace.execute(new RoutingTable(), new PGridHost("127.0.0.1", 3000), null);
    }

    @Test
    public void WhenReplacingWithConjugate_ExpectResults() throws UnknownHostException {
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

        Host failed = new PGridHost("127.0.0.1", 1111);
        failed.setHostPath("1");

        ReplaceStrategy localReplace = new TwinsReplaceStrategy();
        localReplace.execute(localRT, remoteHost, failed);

        ReplaceStrategy remoteReplace = new TwinsReplaceStrategy();
        remoteReplace.execute(remoteRT, localhost, failed);

        Assert.assertTrue(localRT.getLocalhost().getHostPath().toString().compareTo(expectedLocal) == 0);
        Assert.assertTrue(remoteRT.getLocalhost().getHostPath().toString().compareTo(expectedRemote) == 0);
    }
}
