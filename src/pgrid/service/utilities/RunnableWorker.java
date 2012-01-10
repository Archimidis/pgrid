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

package pgrid.service.utilities;

/**
 * @author Vourlakis Nikolas
 */

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * A utility class that adds a cancel mechanism to a Runnable. The class that
 * inherits only need to implement {@link RunnableWorker#workload()} method.
 *
 * @author Vourlakis Nikolas <nvourlakis@gmail.com>
 */
public abstract class RunnableWorker implements Runnable {

    private static final Logger logger_ = LoggerFactory.getLogger(RunnableWorker.class);

    private volatile Thread blinker_ = null;

    @Override
    public final void run() {
        Thread thisThread = Thread.currentThread();
        blinker_ = thisThread;
        while (thisThread == blinker_) {
            workload();
        }
        logger_.debug("RunnableWorker is exiting.");
    }

    /**
     * Cancels this Runnable that runs on a Thread.
     */
    public final void cancel() {
        blinker_ = null;
    }

    /**
     * This method must implement the actual work that the
     * {@link Runnable} will call in its {@link Runnable#run()} method.
     */
    public abstract void workload();
}
