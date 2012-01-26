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

package pgrid.process.meeting.internal;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import pgrid.entity.Host;
import pgrid.process.meeting.PeerMeetingProcess;
import pgrid.service.CommunicationException;
import pgrid.service.exchange.ExchangeService;
import pgrid.service.repair.RepairService;
import pgrid.utilities.ArgumentCheck;

/**
 * The default implementation includes an execution attempt of the exchange
 * service between the local host and a the given remote host. If the remote
 * host cannot be reached for various reasons (e.g. he has failed) then the
 * local host will execute the repair service.
 *
 * @author Vourlakis Nikolas <nvourlakis@gmail.com>
 */
public class DefaultMeetingProcess implements PeerMeetingProcess {

    private static final Logger logger_ = LoggerFactory.getLogger(DefaultMeetingProcess.class);

    private ExchangeService exchange_;
    private RepairService repair_;

    /**
     * Constructor.
     *
     * @param exchange a fully initialized exchange service object.
     * @param repair   a fully initialized repair service object.
     */
    public DefaultMeetingProcess(ExchangeService exchange, RepairService repair) {
        ArgumentCheck.checkNotNull(exchange, "Instead of an ExchangeService object a null value was given.");
        ArgumentCheck.checkNotNull(repair, "Instead of an RepairService object a null value was given.");

        exchange_ = exchange;
        repair_ = repair;
    }

    /**
     * The two hosts will execute the exchange service. If the remote given
     * host has failed then the repair service will be executed.
     *
     * @param host to communicate with.
     */
    @Override
    public void meetWith(Host host) {
        ArgumentCheck.checkNotNull(host, "A null host was given.");

        try {
            exchange_.execute(host);
        } catch (CommunicationException e) {
            logger_.error("{}:{} cannot be reached.", host, host.getPort());
            repair_.fixNode(host);
        }
    }
}
