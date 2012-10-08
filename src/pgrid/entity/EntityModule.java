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

package pgrid.entity;

import com.google.inject.AbstractModule;
import com.google.inject.assistedinject.FactoryModuleBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import pgrid.entity.clock.LamportClock;
import pgrid.entity.clock.PeerClock;
import pgrid.entity.internal.PGridHost;
import pgrid.entity.internal.PGridKey;
import pgrid.entity.internal.PGridKeyRange;
import pgrid.entity.routingtable.RoutingTableFactory;
import pgrid.entity.routingtable.internal.DefaultRoutingTableFactory;
import pgrid.entity.storage.FilenameHashAlgorithm;
import pgrid.entity.storage.Storage;
import pgrid.entity.storage.internal.BinaryFilenameHashing;
import pgrid.entity.storage.internal.FileStorage;

import javax.inject.Singleton;

/**
 * @author Vourlakis Nikolas
 */
public class EntityModule extends AbstractModule {

    private static final Logger logger_ = LoggerFactory.getLogger(EntityModule.class);

    @Override
    protected void configure() {
        logger_.debug("Setting up entity module");
        install(new FactoryModuleBuilder()
                .implement(Host.class, PGridHost.class)
                .implement(Key.class, PGridKey.class)
                .implement(KeyRange.class, PGridKeyRange.class)
                .build(EntityFactory.class));

        bind(PGridPath.class);

        bind(PeerClock.class).to(LamportClock.class);

        bind(RoutingTableFactory.class).to(DefaultRoutingTableFactory.class);
        //bind(RoutingTable.class); // force the client to use the factory

        bind(CorbaFactory.class).in(Singleton.class);

        bind(Storage.class).to(FileStorage.class);
        bind(FilenameHashAlgorithm.class).to(BinaryFilenameHashing.class);
    }
}
