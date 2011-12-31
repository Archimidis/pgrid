/*
 * This file (pgrid.service.exchange.spi.ExchangeAlgorithm) is part of the libpgrid project.
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

/**
 * Following the strategy design pattern, this interface abstracts the exchange
 * algorithm.
 *
 * @author Vourlakis Nikolas <nvourlakis@gmail.com>
18
 */
public interface ExchangeAlgorithm {

    /**
     * Executes the exchange algorithm given a context. The context must be
     * valid and correctly initialized. If the context is null, a
     * {@link NullPointerException} must be thrown. If
     * {@link pgrid.service.exchange.spi.ExchangeContext#isReadyForExchange()}
     * returns false, that is the context is not fully initialized, a
     * {@link IllegalStateException} must be thrown.
     *
     * @param context information needed to execute the algorithm.
     */
    void execute(ExchangeContext context);
}
