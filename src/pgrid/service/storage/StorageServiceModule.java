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

package pgrid.service.storage;

import com.google.inject.AbstractModule;
import com.google.inject.Scopes;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import pgrid.service.ServiceRegistration;
import pgrid.service.corba.storage.StorageHandlePOA;
import pgrid.service.storage.internal.StorageRegistration;
import pgrid.service.storage.spi.StorageHandleProvider;
import pgrid.service.storage.spi.StorageProvider;

/**
 * @author Nikolas Vourlakis <nvourlakis@gmail.com>
 */
public class StorageServiceModule extends AbstractModule {

    private static final Logger logger_ = LoggerFactory.getLogger(StorageServiceModule.class);

    @Override
    protected void configure() {
        logger_.debug("Setting up storage service module");

        bind(StorageService.class).toProvider(StorageProvider.class);
        bind(StorageHandlePOA.class).toProvider(StorageHandleProvider.class);
        bind(StorageHandleProvider.class).in(Scopes.SINGLETON);

        bind(ServiceRegistration.class)
                .annotatedWith(Storage.class)
                .to(StorageRegistration.class);
    }
}
