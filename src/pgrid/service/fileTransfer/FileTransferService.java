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

package pgrid.service.fileTransfer;

import pgrid.entity.Host;
import pgrid.service.CommunicationException;

import java.io.File;

/**
 * A service that implements a very basic file transferring protocol.
 * <p/>
 * <h1> This service is a demo for the presentation of the thesis only. Don't
 * expect serious and complete functionality. <h1/>
 *
 * @author Nikolas Vourlakis <nvourlakis@gmail.com>
 */
public interface FileTransferService {

    /**
     * The local host will communicate with the given remote host and ask to
     * transfer the file. The remote host is supposed to be the physical owner
     * of the file, if not a null value will be returned.
     * When this method returns, the local host has a copy of the remote file
     * locally.
     *
     * @param filename  to be transferred from the remote host.
     * @param fileOwner of the file.
     * @return the {@link File} object associated with the file that was
     *         downloaded.
     * @throws CommunicationException if a communication error occurs between
     *                                the local host and the owner of the file.
     */
    public File transfer(String filename, Host fileOwner) throws CommunicationException;
}
