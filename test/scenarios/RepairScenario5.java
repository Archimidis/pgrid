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

package scenarios;

import com.google.inject.Injector;
import org.junit.Assert;
import org.junit.Test;
import org.omg.CORBA.ORB;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import pgrid.entity.CorbaFactory;
import pgrid.entity.EntityFactory;
import pgrid.entity.Host;
import pgrid.entity.routingtable.RoutingTable;
import pgrid.service.LocalPeerContext;
import pgrid.service.repair.RepairService;

import java.net.UnknownHostException;

/**
 * @author Nikolas Vourlakis <nvourlakis@gmail.com>
 */
public class RepairScenario5 {
    private static final Logger logger_ = LoggerFactory.getLogger(RepairScenario5.class);

    private Thread orbThread_;

    private final int localPort_ = 3000;
    private final String localIP_ = "127.0.0.1";
    private final String localInitPath_ = "1";
    private final String expectedFinalPath_ = "";
    private int expectedLevelNumber_ = 0;

    @Test
    public void executeScenario() throws UnknownHostException {
        logger_.info("[Repair Scenario 5 Start] Peer on \"1\" repairs subtree on \"0\"");
        Injector injector = ScenarioSharedState.getInjector();
        localPeerContextInit(injector);

        LocalPeerContext context = injector.getInstance(LocalPeerContext.class);

        Host[] zeroLevel = context.getLocalRT().getLevelArray(0);
        Assert.assertTrue("Something went wrong during test initialization. " +
                "Conjugate subtree is overpopulated.", zeroLevel.length == 4);
        logger_.info("=====================================================================================");

        Host failed = null;
        for (Host host : zeroLevel) {
            if (host.getHostPath().toString().compareTo("0000") == 0) {
                failed = host;
            }
        }

        logger_.info("Repairing host {}:{} [path: {}]",
                new Object[]{
                        failed.getAddress(),
                        failed.getPort(),
                        failed.getHostPath()});
        RepairService repairService = injector.getInstance(RepairService.class);
        repairService.fixNode(failed);

        logger_.info("Localhost instance: {}:{} [path: {}]",
                new Object[]{
                        context.getLocalRT().getLocalhost().getAddress(),
                        context.getLocalRT().getLocalhost().getPort(),
                        context.getLocalRT().getLocalhost().getHostPath()});
        logger_.info("[Repair Scenario 5 End]");

        Assert.assertTrue(context.getLocalRT().getLocalhost().getAddress().getHostAddress().compareTo(localIP_) == 0);
        Assert.assertTrue(context.getLocalRT().getLocalhost().getPort() == localPort_);
        Assert.assertTrue(context.getLocalRT().getLocalhost().getHostPath().toString().compareTo(expectedFinalPath_) == 0);
        Assert.assertTrue(context.getLocalRT().levelNumber() == expectedLevelNumber_);
    }

    //*******************************************************************************************//
    private void localPeerContextInit(Injector injector) throws UnknownHostException {
        EntityFactory entityFactory = injector.getInstance(EntityFactory.class);
        Host localhost = entityFactory.newHost(localIP_, localPort_);
        localhost.setHostPath(localInitPath_);

        RoutingTable routingTable = injector.getInstance(RoutingTable.class);
        routingTable.setLocalhost(localhost);

        Host A = entityFactory.newHost(localIP_, 1111);
        A.setHostPath("0000");
        routingTable.addReference(0, A);

        Host B = entityFactory.newHost(localIP_, 2222);
        B.setHostPath("0001");
        routingTable.addReference(0, B);

        Host C = entityFactory.newHost(localIP_, 3333);
        C.setHostPath("001");
        routingTable.addReference(0, C);

        Host D = entityFactory.newHost(localIP_, 4444);
        D.setHostPath("01");
        routingTable.addReference(0, D);

        LocalPeerContext context = injector.getInstance(LocalPeerContext.class);
        CorbaFactory corbaFactory = injector.getInstance(CorbaFactory.class);
        ORB orb = corbaFactory.getInstance(localIP_, localPort_);

        context.setOrb(orb);
        context.setRoutingTable(routingTable);
        logger_.info("Localhost instance: {}:{} [path: {}]",
                new Object[]{
                        context.getLocalRT().getLocalhost().getAddress(),
                        context.getLocalRT().getLocalhost().getPort(),
                        context.getLocalRT().getLocalhost().getHostPath()});
    }
}

