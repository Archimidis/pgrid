/*
 * This file (pgrid.entity.CorbaFactory) is part of the libpgrid project.
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

package pgrid.entity;

import org.omg.CORBA.ORB;

import java.util.Properties;

/**
 * @author Vourlakis Nikolas
 */
public class CorbaFactory {
    private static ORB orb_;

    public synchronized ORB getInstance(String initialHost, int initialPort) {
        if (orb_ == null) {
            Properties props = new Properties();
            props.put("com.sun.CORBA.POA.ORBPersistentServerPort",
                    Integer.toString(initialPort));
            props.put("com.sun.CORBA.POA.ORBPersistentServerHost",
                    initialHost);
            orb_ = ORB.init((String[]) null, props);
        }
        return orb_;
    }
}
