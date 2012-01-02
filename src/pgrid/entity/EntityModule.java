/*
 * This file (pgrid.entity.EntityModule) is part of the libpgrid project.
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

package pgrid.entity;

import com.google.inject.AbstractModule;
import com.google.inject.assistedinject.FactoryModuleBuilder;
import pgrid.entity.clock.LamportClock;
import pgrid.entity.clock.PeerClock;
import pgrid.entity.internal.PGridHost;
import pgrid.entity.internal.PGridKey;
import pgrid.entity.internal.PGridKeyRange;
import pgrid.entity.routingtable.PersistenceDelegate;
import pgrid.entity.routingtable.RoutingTable;
import pgrid.entity.routingtable.internal.XMLPersistenceDelegate;

import javax.inject.Singleton;

/**
 * @author Vourlakis Nikolas
 */
public class EntityModule extends AbstractModule {

    @Override
    protected void configure() {
        System.out.println("Setting up entity module");
        // XXX: does not force checked exceptions thrown by PGridHost at construction time.
        install(new FactoryModuleBuilder()
                .implement(Host.class, PGridHost.class)
                .implement(Key.class, PGridKey.class)
                .implement(KeyRange.class, PGridKeyRange.class)
                .build(EntityFactory.class));

        bind(PGridPath.class);

        bind(PeerClock.class).to(LamportClock.class);

        bind(RoutingTable.class);
        bind(PersistenceDelegate.class).to(XMLPersistenceDelegate.class);

        bind(CorbaFactory.class).in(Singleton.class);
    }
}
