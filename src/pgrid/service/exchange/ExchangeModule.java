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

package pgrid.service.exchange;

import com.google.inject.AbstractModule;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import pgrid.service.exchange.internal.AbererExchangeAlgorithm;
import pgrid.service.exchange.spi.ExchangeAlgorithm;
import pgrid.service.exchange.spi.ExchangeHandleProvider;
import pgrid.service.exchange.spi.ExchangeProvider;
import pgrid.service.spi.corba.exchange.ExchangeHandlePOA;

/**
 * @author Vourlakis Nikolas
 */
public class ExchangeModule extends AbstractModule {

    private static final Logger logger_ = LoggerFactory.getLogger(ExchangeModule.class);

    @Override
    protected void configure() {
        logger_.debug("Setting up exchange service module");

        bind(ExchangeAlgorithm.class).to(AbererExchangeAlgorithm.class);
        // constructs a new service every time
        bind(ExchangeService.class).toProvider(ExchangeProvider.class);
        // returns the same handle every time
        bind(ExchangeHandlePOA.class).toProvider(ExchangeHandleProvider.class);
    }
}
