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

package pgrid;

import org.junit.Test;

import java.io.File;
import java.io.IOException;

/**
 * @author Vourlakis Nikolas <nvourlakis@gmail.com>
 */
public class Play {
    @Test
    public void test() throws IOException {
//        Injector injector = Guice.createInjector(
//                new EntityModule(),
//                new ServiceModule("127.0.0.1", 3000, 5),
//                new ProcessModule());
//
//        PeerMeetingProcess process = injector.getProvider(PeerMeetingProcess.class).get();
//        System.out.println(process);
//
//        process = injector.getProvider(Key.get(PeerMeetingProcess.class, Scheduled.class)).get();
//        System.out.println(process);
//
//        Thread work = new Thread((Runnable) process);
//        work.start();
//        try {
//            work.join(500);
//        } catch (InterruptedException e) {}
        File file = new File("/home/ern/Thesis/pgrid/test/pgrid/Play.java");
        System.out.println("Absolute Path:  " + file.getAbsolutePath());
        System.out.println("Canonical Path: " + file.getCanonicalPath());
        System.out.println("Path:           " + file.getPath());
        System.out.println("Name:           " + file.getName());
    }
}
