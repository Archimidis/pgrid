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

package pgrid.service.repair.internal;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import pgrid.entity.Host;
import pgrid.entity.PGridPath;
import pgrid.service.repair.RepairService;

/**
 * @author Vourlakis Nikolas
 */
public class DefaultRepairService implements RepairService {

    private static final Logger logger_ = LoggerFactory.getLogger(DefaultRepairService.class);

    RepairDelegate delegate_;

    public DefaultRepairService(RepairDelegate delegate) {
        delegate_ = delegate;
    }

    @Override
    public void fixNode(Host failedHost) {
        if (failedHost == null) {
            throw new NullPointerException("Cannot execute repair service with a null failed host.");
        }

        PGridPath initFootpath = delegate_.algorithmPathExecution(failedHost.getHostPath());
        delegate_.fixNode(initFootpath.toString(), failedHost);
    }

    @Override
    public void fixSubtree(String subtreePath, Host... failedGroup) {
        if (failedGroup == null) {
            throw new NullPointerException("Cannot execute repair service with a null failed host.");
        }
        if (subtreePath == null) {
            throw new NullPointerException("Cannot execute repair service with a null failed host.");
        }
        // TODO: Implement fixSubtree
    }


}
