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

import pgrid.entity.Host;
import pgrid.entity.internal.PGridHost;
import pgrid.entity.routingtable.RoutingTable;
import pgrid.service.simulation.internal.XMLPersistencyService;
import pgrid.service.simulation.spi.PersistencyDelegate;

import java.io.File;
import java.io.FileNotFoundException;
import java.net.UnknownHostException;

/**
 * Code that generates the topologies that will be used for the experiments.
 *
 * @author Vourlakis Nikolas <nvourlakis@gmail.com>
 */
public class TopologyGeneration {

    public static void main(String[] args) throws UnknownHostException, FileNotFoundException {
        topology1();
        topology2();
        topology3();
    }

    public static void topology1() throws UnknownHostException, FileNotFoundException {
        Host A = new PGridHost("wn010.grid.tuc.gr", 3000);
        A.setHostPath("000");

        Host B = new PGridHost("wn011.grid.tuc.gr", 3000);
        B.setHostPath("001");

        Host C = new PGridHost("wn012.grid.tuc.gr", 3000);
        C.setHostPath("01");

        Host D = new PGridHost("wn013.grid.tuc.gr", 3000);
        D.setHostPath("1");

        RoutingTable routingTableD = new RoutingTable();
        routingTableD.setLocalhost(D);
        routingTableD.addReference(0, A);
        routingTableD.addReference(0, B);
        routingTableD.addReference(0, C);

        RoutingTable routingTableA = new RoutingTable();
        routingTableA.setLocalhost(A);
        routingTableA.addReference(0, D);
        routingTableA.addReference(1, C);
        routingTableA.addReference(2, B);

        RoutingTable routingTableB = new RoutingTable();
        routingTableB.setLocalhost(B);
        routingTableB.addReference(0, D);
        routingTableB.addReference(1, C);
        routingTableB.addReference(2, A);

        RoutingTable routingTableC = new RoutingTable();
        routingTableC.setLocalhost(C);
        routingTableC.addReference(0, D);
        routingTableC.addReference(1, A);
        routingTableC.addReference(1, B);

        String filePrefix = "experiments/Topology1/";
        File file = new File(filePrefix);
        if (!file.exists()) {
            file.mkdir();
        }
        PersistencyDelegate delegate = new XMLPersistencyService();
        delegate.store(filePrefix + "A.xml", routingTableA);
        delegate.store(filePrefix + "B.xml", routingTableB);
        delegate.store(filePrefix + "C.xml", routingTableC);
        delegate.store(filePrefix + "D.xml", routingTableD);
    }

    public static void topology2() throws UnknownHostException, FileNotFoundException {
        Host A = new PGridHost("wn010.grid.tuc.gr", 3000);
        A.setHostPath("00");

        Host B = new PGridHost("wn011.grid.tuc.gr", 3000);
        B.setHostPath("01");

        Host C = new PGridHost("wn012.grid.tuc.gr", 3000);
        C.setHostPath("10");

        Host D = new PGridHost("wn013.grid.tuc.gr", 3000);
        D.setHostPath("11");


        RoutingTable routingTableA = new RoutingTable();
        routingTableA.setLocalhost(A);
        routingTableA.addReference(0, C);
        routingTableA.addReference(0, D);
        routingTableA.addReference(1, B);

        RoutingTable routingTableB = new RoutingTable();
        routingTableB.setLocalhost(B);
        routingTableB.addReference(0, C);
        routingTableB.addReference(0, D);
        routingTableB.addReference(1, A);

        RoutingTable routingTableC = new RoutingTable();
        routingTableC.setLocalhost(C);
        routingTableC.addReference(0, A);
        routingTableC.addReference(0, B);
        routingTableC.addReference(1, D);

        RoutingTable routingTableD = new RoutingTable();
        routingTableD.setLocalhost(D);
        routingTableD.addReference(0, A);
        routingTableD.addReference(0, B);
        routingTableD.addReference(1, C);

        String filePrefix = "experiments/Topology2/";
        File file = new File(filePrefix);
        if (!file.exists()) {
            file.mkdir();
        }
        PersistencyDelegate delegate = new XMLPersistencyService();
        delegate.store(filePrefix + "A.xml", routingTableA);
        delegate.store(filePrefix + "B.xml", routingTableB);
        delegate.store(filePrefix + "C.xml", routingTableC);
        delegate.store(filePrefix + "D.xml", routingTableD);
    }

    public static void topology3() throws UnknownHostException, FileNotFoundException {
        Host A = new PGridHost("wn010.grid.tuc.gr", 3000);
        A.setHostPath("000");

        Host B = new PGridHost("wn011.grid.tuc.gr", 3000);
        B.setHostPath("0010");

        Host C = new PGridHost("wn012.grid.tuc.gr", 3000);
        C.setHostPath("0011");

        Host D = new PGridHost("wn013.grid.tuc.gr", 3000);
        D.setHostPath("01");

        Host E = new PGridHost("wn013.grid.tuc.gr", 3000);
        E.setHostPath("1");


        RoutingTable routingTableA = new RoutingTable();
        routingTableA.setLocalhost(A);
        routingTableA.addReference(0, E);
        routingTableA.addReference(1, D);
        routingTableA.addReference(2, B);
        routingTableA.addReference(2, C);

        RoutingTable routingTableB = new RoutingTable();
        routingTableB.setLocalhost(B);
        routingTableB.addReference(0, E);
        routingTableB.addReference(1, D);
        routingTableB.addReference(2, A);
        routingTableB.addReference(3, C);

        RoutingTable routingTableC = new RoutingTable();
        routingTableC.setLocalhost(C);
        routingTableC.addReference(0, E);
        routingTableC.addReference(1, D);
        routingTableC.addReference(2, A);
        routingTableC.addReference(3, B);

        RoutingTable routingTableD = new RoutingTable();
        routingTableD.setLocalhost(D);
        routingTableD.addReference(0, E);
        routingTableD.addReference(1, A);
        routingTableD.addReference(1, B);
        routingTableD.addReference(1, C);

        RoutingTable routingTableE = new RoutingTable();
        routingTableE.setLocalhost(E);
        routingTableE.addReference(0, A);
        routingTableE.addReference(0, B);
        routingTableE.addReference(0, C);
        routingTableE.addReference(0, D);

        String filePrefix = "experiments/Topology3/";
        File file = new File(filePrefix);
        if (!file.exists()) {
            file.mkdir();
        }
        PersistencyDelegate delegate = new XMLPersistencyService();
        delegate.store(filePrefix + "A.xml", routingTableA);
        delegate.store(filePrefix + "B.xml", routingTableB);
        delegate.store(filePrefix + "C.xml", routingTableC);
        delegate.store(filePrefix + "D.xml", routingTableD);
        delegate.store(filePrefix + "E.xml", routingTableE);
    }
}
