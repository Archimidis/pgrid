/*
 * This file (pgrid.service.repair.internal.ThesisFixNodeAlgorithmTest) is part of the libpgrid project.
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

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import pgrid.entity.Host;
import pgrid.entity.PGridPath;
import pgrid.entity.internal.PGridHost;
import pgrid.entity.routingtable.RoutingTable;
import pgrid.service.repair.spi.FixNodeAlgorithm;

import java.net.UnknownHostException;

/**
 * @author Vourlakis Nikolas
 */
public class ThesisFixNodeAlgorithmTest {
    @Before
    public void setUp() {

    }

    @After
    public void tearDown() {

    }

    @Test(expected = NullPointerException.class)
    public void WhenExecutingWithNullHost_ExpectException() {
        RoutingTable rt = new RoutingTable();
        FixNodeAlgorithm algorithm = new ThesisFixNodeAlgorithm(rt);
        algorithm.execute(null, new PGridPath(""));
    }

    @Test(expected = NullPointerException.class)
    public void WhenExecutingWithNullPath_ExpectException() throws UnknownHostException {
        RoutingTable rt = new RoutingTable();
        FixNodeAlgorithm algorithm = new ThesisFixNodeAlgorithm(rt);
        algorithm.execute(new PGridHost("127.0.0.1", 3000), null);
    }
    
    @Test
    public void WhenExecutingAndLocalhostIsSolution_ExpectLocalhost() throws UnknownHostException {
        RoutingTable rt = new RoutingTable();
        Host localhost = new PGridHost("127.0.0.1", 3000);
        localhost.setHostPath("010");
        rt.setLocalhost(localhost);
        FixNodeAlgorithm algorithm = new ThesisFixNodeAlgorithm(rt);
        algorithm.execute(new PGridHost("127.0.0.1", 1111), new PGridPath("00")); // problematic case
    }
}
