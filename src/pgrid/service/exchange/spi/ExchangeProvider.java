/*
 * This file (pgrid.service.exchange.spi.ExchangeProvider) is part of the libpgrid project.
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

package pgrid.service.exchange.spi;

import pgrid.service.LocalPeerContext;
import pgrid.service.exchange.ExchangeService;
import pgrid.service.exchange.internal.DefaultExchangeService;

import javax.inject.Inject;
import javax.inject.Provider;

/**
 * The provider for the ExchangeService.
 *
 * @author Vourlakis Nikolas
 */
public class ExchangeProvider implements Provider<ExchangeService> {
    private final LocalPeerContext context_;
    private ExchangeAlgorithm algorithm_;

    @Inject
    public ExchangeProvider(LocalPeerContext context, ExchangeAlgorithm algo) {
        context_ = context;
        algorithm_ = algo;
    }

    /**
     * Constructs and returns a new ExchangeService object fully initialized.
     *
     * @return an ExchangeService object.
     */
    @Override
    public ExchangeService get() {
        return new DefaultExchangeService(
                context_.getCorba(),
                context_.getLocalRT(),
                algorithm_
        );
    }
}
