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

import pgrid.entity.Host;
import pgrid.service.CommunicationException;

/**
 * This interface defines an exchange service. It gives the capability to the
 * local peer to communicate with a remote peer and both start and execute
 * successfully the exchange algorithm.
 *
 * @author Vourlakis Nikolas
 */
public interface ExchangeService {

    /**
     * Given a remote host, the local peer will try to communicate with him.
     * If the remote host is alive, they will both execute the exchange
     * algorithm. In case of a problem during the communication a
     * {@link CommunicationException} will be thrown. This may mean that the
     * given host cannot be reached. Note that there isn't a way to
     * distinguish a host that exists but has come offline to a valid host but
     * nonexistent whatsoever in the network.
     *
     * @param host to execute the exchange algorithm with.
     * @throws CommunicationException in case of a communication error coming
     *                                from the underlying network facility.
     */
    public void execute(Host host) throws CommunicationException;
}
