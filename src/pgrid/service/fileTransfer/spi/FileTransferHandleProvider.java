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

package pgrid.service.fileTransfer.spi;

import pgrid.service.LocalPeerContext;
import pgrid.service.anotations.constants.DownloadDir;
import pgrid.service.anotations.constants.SharedDir;
import pgrid.service.corba.fileTransfer.TransferHandlePOA;
import pgrid.service.fileTransfer.internal.FileTransferDelegate;
import pgrid.service.fileTransfer.internal.FileTransferHandle;
import pgrid.utilities.ArgumentCheck;

import javax.inject.Inject;
import javax.inject.Provider;

/**
 * @author Nikolas Vourlakis <nvourlakis@gmail.com>
 */
public class FileTransferHandleProvider implements Provider<TransferHandlePOA> {

    private final LocalPeerContext context_;
    private static String DOWNLOAD_DIR;
    private static String SHARED_DIR;

    @Inject
    public FileTransferHandleProvider(LocalPeerContext context, @DownloadDir String downDir, @SharedDir String sharedDir) {
        ArgumentCheck.checkNotNull(context, "Cannot initialize a StorageProvider object with a null LocalPeerContext value.");
        ArgumentCheck.checkNotNull(context.getCorba(), "Uninitialized ORB in LocalPeerContext object passed to StorageProvider.");
        ArgumentCheck.checkNotNull(context.getLocalRT(), "Uninitialized RoutingTable in LocalPeerContext object passed to StorageProvider.");
        ArgumentCheck.checkNotNull(downDir, "Cannot initialize a StorageProvider object with a null value as download directory.");
        ArgumentCheck.checkNotNull(sharedDir, "Cannot initialize a StorageProvider object with a null value as shared directory.");

        context_ = context;
        DOWNLOAD_DIR = downDir;
        SHARED_DIR = sharedDir;
    }

    @Override
    public TransferHandlePOA get() {
        FileTransferDelegate delegate = new FileTransferDelegate(context_.getCorba());
        delegate.setDirectories(DOWNLOAD_DIR, SHARED_DIR);
        return new FileTransferHandle(delegate);
    }
}
