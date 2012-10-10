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

import com.google.inject.AbstractModule;
import com.google.inject.Scopes;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import pgrid.service.ServiceRegistration;
import pgrid.service.anotations.constants.DownloadDir;
import pgrid.service.anotations.constants.SharedDir;
import pgrid.service.corba.fileTransfer.TransferHandlePOA;
import pgrid.service.fileTransfer.internal.FileTransferRegistration;
import pgrid.service.fileTransfer.spi.FileTransferHandleProvider;
import pgrid.service.fileTransfer.spi.FileTransferProvider;

/**
 * @author Nikolas Vourlakis <nvourlakis@gmail.com>
 */
public class FileTransferModule extends AbstractModule {

    private static final Logger logger_ = LoggerFactory.getLogger(FileTransferModule.class);
    private static String sharedDir_;
    private static String downloadDir_;

    public FileTransferModule(String sharedDir, String downloadDir) {
        sharedDir_ = sharedDir;
        downloadDir_ = downloadDir;
    }

    @Override
    protected void configure() {
        logger_.debug("Setting up transfer service module");

        bindConstant()
                .annotatedWith(SharedDir.class)
                .to(sharedDir_);
        bindConstant()
                .annotatedWith(DownloadDir.class)
                .to(downloadDir_);

        bind(FileTransferService.class).toProvider(FileTransferProvider.class);
        bind(TransferHandlePOA.class).toProvider(FileTransferHandleProvider.class);
        bind(FileTransferHandleProvider.class).in(Scopes.SINGLETON);

        bind(ServiceRegistration.class)
                .annotatedWith(FileTransfer.class)
                .to(FileTransferRegistration.class);
    }
}
