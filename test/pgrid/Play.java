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

package pgrid;

import com.google.inject.Guice;
import com.google.inject.Injector;
import org.junit.Test;
import pgrid.entity.EntityFactory;
import pgrid.entity.EntityModule;
import pgrid.entity.Host;
import pgrid.entity.routingtable.RoutingTable;
import pgrid.entity.routingtable.RoutingTableFactory;

import java.net.UnknownHostException;

/**
 * @author Vourlakis Nikolas <nvourlakis@gmail.com>
 */
public class Play {
    @Test
    public void test() throws UnknownHostException {
        Injector injector = Guice.createInjector(new EntityModule());
        EntityFactory enFactory = injector.getInstance(EntityFactory.class);
        RoutingTableFactory rtFactory = injector.getInstance(RoutingTableFactory.class);
        Host localhost = enFactory.newHost("127.0.0.1", 3000);
        RoutingTable rt = rtFactory.create(localhost);
        System.out.println(rt.getLocalhost());
    }
}
