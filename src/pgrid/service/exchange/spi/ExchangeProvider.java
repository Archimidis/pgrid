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

package pgrid.service.exchange.spi;

import pgrid.service.LocalPeerContext;
import pgrid.service.anotations.constants.MaxRecursions;
import pgrid.service.anotations.constants.MaxRef;
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
    private final ExchangeAlgorithm algorithm_;

    private final int MAX_RECURSIONS;
    private final int MAX_REF;

    /**
     * Constructor.
     *
     * @param context  contains all the information about the local peer.
     * @param algo     an implementation of the exchange algorithm.
     * @param maxRef   maximum references a level in the routing table can hold.
     * @param maxRecur maximum recursions for case 4 in the exchange algorithm.
     */
    @Inject
    public ExchangeProvider(LocalPeerContext context, ExchangeAlgorithm algo, @MaxRef int maxRef, @MaxRecursions int maxRecur) {
        context_ = context;
        algorithm_ = algo;
        MAX_RECURSIONS = maxRecur;
        MAX_REF = maxRef;
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
                algorithm_,
                MAX_REF,
                MAX_RECURSIONS
        );
    }
}
