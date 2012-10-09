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

package pgrid.service.fileTransfer.internal;

import pgrid.service.corba.fileTransfer.TransferHandlePOA;
import pgrid.utilities.ArgumentCheck;

/**
 * <b>This class is for internal use only.</b>
 *
 * @author Nikolas Vourlakis <nvourlakis@gmail.com>
 */
public class FileTransferHandle extends TransferHandlePOA {

    private final FileTransferDelegate delegate_;

    public FileTransferHandle(FileTransferDelegate delegate) {
        ArgumentCheck.checkNotNull(delegate, "Cannot initialize a FileTransferHandle object with a null FileTransferDelegate value.");

        delegate_ = delegate;
    }

    @Override
    public byte[] transfer(String fileName) {
        return delegate_.sendFile(fileName);
    }
}
