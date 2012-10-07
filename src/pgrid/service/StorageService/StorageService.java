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

package pgrid.service.StorageService;

import pgrid.entity.Host;

import java.io.File;

/**
 * StorageService is a simple abstraction over the P-Grid network. The network
 * is represented as a large storage device.
 * <p/>
 * <h1> This service is a demo for the presentation of the thesis only. Don't
 * expect serious and complete functionality. <h1/>
 *
 * @author Nikolas Vourlakis <nvourlakis@gmail.com>
 */
public interface StorageService {

    /**
     * It stores the given file in the network, this means that the file will
     * probably be transmitted to a remote host.
     *
     * @param filename a {@link File} object associated with the file to be
     *                 stored in the network.
     */
    public void store(File filename);

    /**
     * Given a filename it will search the network and find the host that
     * stores the file in his hard-drive. In the case were no host is found
     * for any reasons (the file does not exists) a null value will be
     * returned. In case of a communication error a corresponding exception
     * must be thrown.
     * <p/>
     * See {@link pgrid.service.CommunicationException} for an
     * explanation of what a communication error is.
     *
     * @param filename to be searched in the network.
     * @return the host that physically holds the file.
     */
    public Host ownerOf(String filename);
}
