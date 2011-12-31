/*
 * This file (pgrid.service.ServiceModule) is part of the libpgrid project.
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

package pgrid.service;

import com.google.inject.AbstractModule;
import pgrid.service.exchange.ExchangeService;
import pgrid.service.exchange.internal.DefaultExchangeAlgorithm;
import pgrid.service.exchange.internal.DefaultExchangeHandle;
import pgrid.service.exchange.spi.ExchangeAlgorithm;
import pgrid.service.exchange.spi.ExchangeProvider;
import pgrid.service.spi.corba.ExchangeHandlePOA;

import javax.inject.Singleton;

/**
 * @author Vourlakis Nikolas
 */
public class ServiceModule extends AbstractModule {
    @Override
    protected void configure() {
        System.out.println("Setting up service module");

        bind(ExchangeAlgorithm.class).to(DefaultExchangeAlgorithm.class);
        bind(ExchangeHandlePOA.class).to(DefaultExchangeHandle.class);
        bind(ExchangeService.class).toProvider(ExchangeProvider.class);

        bind(LocalPeerContext.class).in(Singleton.class);

        bind(ExchangeService.class).toProvider(ExchangeProvider.class);
    }
}
