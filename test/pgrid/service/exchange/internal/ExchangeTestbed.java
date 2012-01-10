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

import pgrid.entity.Host;
import pgrid.entity.internal.PGridHost;
import pgrid.entity.routingtable.RoutingTable;
import pgrid.service.exchange.spi.ExchangeContext;

import java.net.UnknownHostException;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * This class is used for simulating purposes in unit tests for the exchange
 * algorithm. Given a CASE {1, 2, 3}, it construct the contexts needed by
 * the Exchange class to run the algorithm. The P-Grid trees that are
 * constructed for each case and are shown bellow.
 * <ul>
 * <li>CASE 1: local == 0 && remote == 0
 * <pre>
 *          0/\1
 *          /  [host1]
 *        0/ \1
 *        /   [host2]
 *     0/   \1
 *   [A|B]   [host3]
 * </pre>
 * </li>
 * <li>CASE 2 & 3: local == 0 && remote > 0</dd>
 * <pre>
 *          0/\1
 *          /  [host1]
 *        0/ \1
 *       [B]  [host2]
 *     0/   \1
 *    [A]    [host3]
 * </pre>
 * </li>
 * <li>CASE 4: local > 0 && remote > 0
 * <pre>
 *          0/\1
 *          /  [host1]
 *        0/ \1
 *        /   [host2]
 *     0/   \1
 *    [A]   [B]
 * </pre>
 * </li>
 * <li>BOOTSTRAP CASE 1: local == 0 && remote == 0
 * <pre>
 *    [A]   [B] (both with path "")
 * </pre>
 * </li>
 * <li>BOOTSTRAP CASE 2 & 3: local > 0 && remote == 0
 * <pre>
 *        [B] (B with path "")
 *      0/
 *    [A]
 * </pre>
 * </li>
 * </ul>
 *
 * @author Vourlakis Nikolas <nvourlakis@gmail.com>
 */
class ExchangeTestbed {

    public enum Case {
        CASE_1,
        CASE_2_3,
        CASE_4,
        BOOTSTRAP_1,
        BOOTSTRAP_2
    }

    private static final Map<Integer, UUID> hostUUID_ = new HashMap<Integer, UUID>();
    private static int REF_MAX = 1;

    static {
        for (int i = 1; i <= 5; i++) {
            hostUUID_.put(i, UUID.randomUUID());
        }
    }

    // take the contexts from the public static methods.
    private ExchangeTestbed() {
    }

    public static String expectedLocalPath(Case CASE) {
        String result = null;
        switch (CASE) {
            case CASE_1:
                result = "0000";
                break;
            case CASE_2_3:
                result = "000";
                break;
            case CASE_4:
                result = "000";
                break;
            case BOOTSTRAP_1:
            case BOOTSTRAP_2:
                result = "0";
                break;
        }
        return result;
    }

    public static String expectedRemotePath(Case CASE) {
        String result = null;
        switch (CASE) {
            case CASE_1:
                result = "0001";
                break;
            case CASE_2_3:
                result = "001";
                break;
            case CASE_4:
                result = "001";
                break;
            case BOOTSTRAP_1:
            case BOOTSTRAP_2:
                result = "1";
                break;
        }
        return result;
    }

    public static ExchangeContext createLocalContext(Case CASE) {
        PGridHost localHost = null;
        PGridHost remoteHost = null;
        RoutingTable localRT = null;
        RoutingTable remoteRT = null;

        try {
            localHost = new PGridHost("127.0.0.1", 3000);
            localHost.setUUID(hostUUID_.get(4));
            remoteHost = new PGridHost("127.0.0.1", 3001);
            localHost.setUUID(hostUUID_.get(5));
            setPath(CASE, localHost, remoteHost);

            localRT = createLocalRT(localHost, CASE);
            remoteRT = createRemoteRT(remoteHost, CASE);
        } catch (UnknownHostException ex) {
            // Silence - Not going to be thrown
        }

        ExchangeContext context = new ExchangeContext(localRT, false, REF_MAX);
        context.setRemoteInfo(remoteRT);
        return context;
    }

    public static ExchangeContext createRemoteContext(Case CASE) {
        PGridHost localHost = null;
        PGridHost remoteHost = null;
        RoutingTable localRT = null;
        RoutingTable remoteRT = null;

        try {
            localHost = new PGridHost("127.0.0.1", 3000);
            localHost.setUUID(hostUUID_.get(4));
            remoteHost = new PGridHost("127.0.0.1", 3001);
            localHost.setUUID(hostUUID_.get(5));
            setPath(CASE, localHost, remoteHost);

            localRT = createLocalRT(localHost, CASE);
            remoteRT = createRemoteRT(remoteHost, CASE);
        } catch (UnknownHostException ex) {
            // Silence - Not going to be thrown
        }

        ExchangeContext context = new ExchangeContext(remoteRT, true, REF_MAX);
        context.setRemoteInfo(localRT);
        return context;
    }

    private static void setPath(Case CASE, PGridHost localHost, PGridHost remoteHost) {
        switch (CASE) {
            case CASE_1:
                localHost.setHostPath("000");
                remoteHost.setHostPath("000");
                break;
            case CASE_2_3:
                localHost.setHostPath("000");
                remoteHost.setHostPath("00");
                break;
            case CASE_4:
                localHost.setHostPath("000");
                remoteHost.setHostPath("001");
                break;
            case BOOTSTRAP_1:
                localHost.setHostPath("");
                remoteHost.setHostPath("");
                break;
            case BOOTSTRAP_2:
                localHost.setHostPath("0");
                remoteHost.setHostPath("");
                break;
        }
    }

    private static RoutingTable createLocalRT(Host host, Case CASE) throws UnknownHostException {
        // the following are stored in the LOCAL host
        RoutingTable localRT = new RoutingTable();
        localRT.setLocalhost(host);

        if (CASE.compareTo(Case.BOOTSTRAP_1) == 0) {
            return localRT;
        }


        // CASE == 1: local == 0 && remote == 0
        PGridHost host1 = new PGridHost("127.0.0.1", 1001);
        host1.setHostPath("1");
        host1.setUUID(hostUUID_.get(1));
        localRT.addReference(0, host1);

        if (CASE.compareTo(Case.BOOTSTRAP_2) == 0) {
            return localRT;
        }

        PGridHost host2 = new PGridHost("127.0.0.1", 1002);
        host2.setHostPath("01");
        host2.setUUID(hostUUID_.get(2));
        localRT.addReference(1, host2);

        PGridHost host3;
        switch (CASE) {
            case CASE_4:
                // CASE == 4: local > 0 && remote > 0
                host3 = new PGridHost("127.0.0.1", 3001); // points to remote host
                break;
            default:
                // CASE == 2 & 3: local == 0 && remote > 0
                host3 = new PGridHost("127.0.0.1", 1003);
                host3.setUUID(hostUUID_.get(3));
                break;
        }

        host3.setHostPath("001");
        localRT.addReference(2, host3);

        return localRT;
    }

    private static RoutingTable createRemoteRT(Host host, Case CASE) throws UnknownHostException {
        // the following are stored in the REMOTE host
        RoutingTable remoteRT = new RoutingTable();
        remoteRT.setLocalhost(host);

        if (CASE.compareTo(Case.BOOTSTRAP_1) == 0 || CASE.compareTo(Case.BOOTSTRAP_2) == 0) {
            return remoteRT;
        }

        // CASE == 2 & 3: local == 0 && remote > 0
        PGridHost host1 = new PGridHost("127.0.0.1", 1001);
        host1.setHostPath("1");
        host1.setUUID(hostUUID_.get(1));
        PGridHost host2 = new PGridHost("127.0.0.1", 1002);
        host2.setHostPath("01");
        host2.setUUID(hostUUID_.get(2));

        remoteRT.addReference(0, host1);
        remoteRT.addReference(1, host2);

        switch (CASE) {
            case CASE_1:
                // CASE == 1: local == 0 && remote == 0
                PGridHost host3 = new PGridHost("127.0.0.1", 1003);
                host3.setHostPath("001");
                remoteRT.addReference(2, host3);
                host3.setUUID(hostUUID_.get(3));
                break;
            case CASE_4:
                // CASE == 4: local > 0 && remote > 0
                PGridHost host6 = new PGridHost("127.0.0.1", 3000); // points to local host
                host6.setHostPath("000");
                host6.setUUID(hostUUID_.get(4));
                remoteRT.addReference(2, host6);
                break;
        }
        return remoteRT;
    }
}

