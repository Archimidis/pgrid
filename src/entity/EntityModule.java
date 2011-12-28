/*
 * This file (entity.EnityModule) is part of the libpgrid project.
 *
 * Copyright (c) 2011. Vourlakis Nikolas. All rights reserved.
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

package entity;

import com.google.inject.AbstractModule;
import com.google.inject.assistedinject.FactoryModuleBuilder;
import entity.clock.LamportClock;
import entity.clock.PeerClock;
import entity.internal.PGridHost;
import entity.internal.PGridKey;
import entity.internal.PGridKeyRange;
import entity.routingtable.PersistenceDelegate;
import entity.routingtable.RoutingTable;
import entity.routingtable.internal.XMLPersistenceDelegate;

/**
 * @author Vourlakis Nikolas
 */
public class EntityModule extends AbstractModule {

    @Override
    protected void configure() {
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
    }
}

