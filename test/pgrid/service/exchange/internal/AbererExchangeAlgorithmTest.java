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

package pgrid.service.exchange.internal;

import org.junit.Assert;
import org.junit.Test;
import pgrid.entity.Host;
import pgrid.entity.internal.PGridHost;
import pgrid.entity.routingtable.RoutingTable;
import pgrid.service.exchange.spi.ExchangeAlgorithm;
import pgrid.service.exchange.spi.ExchangeContext;

import java.net.UnknownHostException;

/**
 * @author Vourlakis Nikolas
 */
public class AbererExchangeAlgorithmTest {
    private final ExchangeAlgorithm localExchange_ = new AbererExchangeAlgorithm();
    private final ExchangeAlgorithm remoteExchange_ = new AbererExchangeAlgorithm();
    private static int REF_MAX = 1;

    private void executeExchange(ExchangeContext local, ExchangeContext remote) {
        localExchange_.execute(local);
        remoteExchange_.execute(remote);
    }

    private boolean validatePath(ExchangeTestbed.Case CASE, String path, boolean isLocal) {
        String result;
        if (isLocal) {
            result = ExchangeTestbed.expectedLocalPath(CASE);
        } else {
            result = ExchangeTestbed.expectedRemotePath(CASE);
        }
        return path.compareTo(result) == 0;
    }

    // public void execute(ExchangeContext context)
    @Test(expected = NullPointerException.class)
    public void WhenExecutingWithNullContext_ExpectException() {
        localExchange_.execute(null);
    }

    // public void execute(ExchangeContext context)
    @Test(expected = IllegalStateException.class)
    public void WhenExecutingWithInvalidLocalValidRemoteInfo_ExpectException() throws UnknownHostException {
        RoutingTable localRT = new RoutingTable();
        // localhost is not initialized
        ExchangeContext context = new ExchangeContext(localRT, false, REF_MAX);
        RoutingTable remoteRT = new RoutingTable();
        remoteRT.setLocalhost(new PGridHost("127.0.0.1", 3000));
        context.setRemoteInfo(remoteRT);

        localExchange_.execute(context);
    }

    // public void execute(ExchangeContext context)
    @Test(expected = IllegalStateException.class)
    public void WhenExecutingWithValidLocalInvalidRemoteInfo_ExpectException() throws UnknownHostException {
        RoutingTable localRT = new RoutingTable();
        localRT.setLocalhost(new PGridHost("127.0.0.1", 3000));
        ExchangeContext context = new ExchangeContext(localRT, false, REF_MAX);
        // no remote routing table was set
        localExchange_.execute(context);
    }

    // public void execute(ExchangeContext context)
    @Test(expected = IllegalStateException.class)
    public void WhenExecutingWithValidLocalSemiInvalidRemoteInfo_ExpectException() throws UnknownHostException {
        RoutingTable localRT = new RoutingTable();
        localRT.setLocalhost(new PGridHost("127.0.0.1", 3000));
        ExchangeContext context = new ExchangeContext(localRT, false, REF_MAX);
        context.setRemoteInfo(new RoutingTable());
        // no localhost set to the remote routing table
        localExchange_.execute(context);
    }

    // public void execute(ExchangeContext context)
    @Test
    public void WhenExecutingWithEqualPaths_ExpectCase1Results() {
        // CASE == 1: local == 0 && remote == 0
        ExchangeTestbed.Case CASE = ExchangeTestbed.Case.CASE_1;

        ExchangeContext local = ExchangeTestbed.createLocalContext(CASE);
        ExchangeContext remote = ExchangeTestbed.createRemoteContext(CASE);

        executeExchange(local, remote);

        Host localhost = local.getLocalRoutingTable().getLocalhost();
        Host remotehost = remote.getLocalRoutingTable().getLocalhost();

        Assert.assertTrue(validatePath(CASE, localhost.getHostPath().toString(), true));
        Assert.assertTrue(validatePath(CASE, remotehost.getHostPath().toString(), false));

        Assert.assertTrue(local.getLocalRoutingTable().levelNumber() == 4);
        Assert.assertTrue(remote.getLocalRoutingTable().levelNumber() == 4);
    }

    // public void execute(ExchangeContext context)
    @Test
    public void WhenExecutingWithOneLongerPath_ExpectCase2And3Results() {
        // CASE == 2: local == 0 && remote > 0
        ExchangeTestbed.Case CASE = ExchangeTestbed.Case.CASE_2_3;

        ExchangeContext local = ExchangeTestbed.createLocalContext(CASE);
        ExchangeContext remote = ExchangeTestbed.createRemoteContext(CASE);

        executeExchange(local, remote);

        Host localhost = local.getLocalRoutingTable().getLocalhost();
        Host remotehost = remote.getLocalRoutingTable().getLocalhost();

        Assert.assertTrue(validatePath(CASE, localhost.getHostPath().toString(), true));
        Assert.assertTrue(validatePath(CASE, remotehost.getHostPath().toString(), false));

        Assert.assertTrue(local.getLocalRoutingTable().levelNumber() == 3);
        Assert.assertTrue(remote.getLocalRoutingTable().levelNumber() == 3);
    }

    // public void execute(ExchangeContext context)
    @Test
    public void WhenExecutingWithTwoLongerPaths_ExpectCase4Results() {
        // CASE == 4: local > 0 && remote > 0
        ExchangeTestbed.Case CASE = ExchangeTestbed.Case.CASE_4;

        ExchangeContext local = ExchangeTestbed.createLocalContext(CASE);
        ExchangeContext remote = ExchangeTestbed.createRemoteContext(CASE);

        executeExchange(local, remote);

        Host localhost = local.getLocalRoutingTable().getLocalhost();
        Host remotehost = remote.getLocalRoutingTable().getLocalhost();

        Assert.assertTrue(validatePath(CASE, localhost.getHostPath().toString(), true));
        Assert.assertTrue(validatePath(CASE, remotehost.getHostPath().toString(), false));

        Assert.assertTrue(local.getLocalRoutingTable().levelNumber() == 3);
        Assert.assertTrue(remote.getLocalRoutingTable().levelNumber() == 3);

        Assert.assertTrue(local.isRecursive());
        Assert.assertTrue(remote.isRecursive());

        Assert.assertTrue(local.getCommonPathLength() == remote.getCommonPathLength());
        Assert.assertTrue(local.getCommonPathLength() + 1 == 2);
    }
}
