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

package pgrid.process.downloadFile.internal;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import pgrid.entity.Host;
import pgrid.process.downloadFile.DownloadFileProcess;
import pgrid.service.CommunicationException;
import pgrid.service.fileTransfer.FileTransferService;
import pgrid.service.repair.RepairService;
import pgrid.service.storage.StorageService;
import pgrid.utilities.ArgumentCheck;

import javax.inject.Inject;

/**
 * @author Nikolas Vourlakis <nvourlakis@gmail.com>
 */
public class DemoDownloadFileProcess implements DownloadFileProcess {

    private static final Logger logger_ = LoggerFactory.getLogger(DemoDownloadFileProcess.class);

    private StorageService storage_;
    private FileTransferService transfer_;
    private RepairService repair_;

    @Inject
    public DemoDownloadFileProcess(StorageService storage, FileTransferService transfer, RepairService repair) {
        ArgumentCheck.checkNotNull(storage, "Instead of an StorageService object a null value was given.");
        ArgumentCheck.checkNotNull(transfer, "Instead of an FileTransferService object a null value was given.");
        ArgumentCheck.checkNotNull(repair, "Instead of an RepairService object a null value was given.");

        storage_ = storage;
        transfer_ = transfer;
        repair_ = repair;
    }


    @Override
    public Status download(String filename) {
        ArgumentCheck.checkNotNull(filename, "A null name as a filename was given.");

        Host owner = storage_.ownerOf(filename);
        if (owner == null) {
            return Status.FILE_NOT_FOUND;
        }

        try {
            transfer_.transfer(filename, owner);
        } catch (CommunicationException e) {
            logger_.warn("Unable to reach [{}] {}:{}. Starting repair service.",
                    new Object[]{owner.getHostPath(), owner.getAddress(), owner.getPort()});
            repair_.fixNode(owner);
            return Status.NETWORK_ERROR;
        }
        return Status.FILE_DOWNLOADED;
    }
}
