/*
 * This file (net.pgrid.LamportClockTest) is part of the libpgrid project.
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

package pgrid.entity.clock;

import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

import java.util.ArrayList;


/**
 * @author Vourlakis Nikolas <nvourlakis@gmail.com>
 */
public class LamportClockTest {

    private static Thread thread1_;
    private static Thread thread2_;
    private static LamportClock clock_;
    private static ArrayList<Long> timestampList_;
    private final static int events1_ = 5;
    private final static int events2_ = 10;
    private final static Object lock_ = new Object();

    public LamportClockTest() {
    }

    @BeforeClass
    public static void setUpClass() {
        clock_ = new LamportClock();
        timestampList_ = new ArrayList<Long>();

        thread1_ = new Thread(new Runnable() {

            @Override
            public void run() {
                for (int i = 1; i <= events1_; i++) {
                    clock_.clockedEvent((long) i);
                    synchronized (lock_) {
                        timestampList_.add(clock_.getTimestamp());
                    }
                }
            }
        });

        thread2_ = new Thread(new Runnable() {

            @Override
            public void run() {
                for (int i = 1; i <= events2_; i++) {
                    clock_.clockedEvent((long) i);
                    synchronized (lock_) {
                        timestampList_.add(clock_.getTimestamp());
                    }
                }
            }
        });
    }

    /**
     * Creating 2 threads that keep updating the entity.clock storing all the
     * timestamps in a list. This methods test if the size of the list is equal
     * to the total number of the events (entity.clock updates) and if the list is
     * sorted.
     *
     * @throws InterruptedException needed fro threads.
     */
    @Test
    public void WhenConcurentChanges_ExpectOrderedTimeEvents() throws InterruptedException {
        // Lamport entity.clock synchronization test

        thread1_.start();
        thread2_.start();
        thread1_.join();
        thread2_.join();

        Assert.assertTrue("Lamport entity.clock failed at synchronization",
                timestampList_.size() == (events1_ + events2_));

        long previous = timestampList_.get(0);
        for (int i = 1; i < timestampList_.size(); i++) {
            long next = timestampList_.get(i);
            String msg1 = previous + " ?<=? " + next;
            Assert.assertTrue(msg1, previous <= next);
            previous = next;
        }
    }
}
