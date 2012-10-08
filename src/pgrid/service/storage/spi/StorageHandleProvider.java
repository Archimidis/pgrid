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

package pgrid.service.storage.spi;

import pgrid.entity.storage.FilenameHashAlgorithm;
import pgrid.entity.storage.Storage;
import pgrid.service.LocalPeerContext;
import pgrid.service.corba.storage.StorageHandlePOA;
import pgrid.service.storage.internal.FileStorageHandle;
import pgrid.service.storage.internal.StorageDelegate;
import pgrid.utilities.ArgumentCheck;

import javax.inject.Inject;
import javax.inject.Provider;

/**
 * @author Nikolas Vourlakis <nvourlakis@gmail.com>
 */
public class StorageHandleProvider implements Provider<StorageHandlePOA> {

    private FileStorageHandle poa_;
    private final StorageDelegate delegate_;

    @Inject
    public StorageHandleProvider(LocalPeerContext context, Storage storage) {
        ArgumentCheck.checkNotNull(context, "Cannot initialize a StorageHandleProvider object with a null LocalPeerContext value.");
        ArgumentCheck.checkNotNull(context.getCorba(), "Uninitialized ORB in LocalPeerContext object passed to StorageHandleProvider.");
        ArgumentCheck.checkNotNull(context.getLocalRT(), "Uninitialized RoutingTable in LocalPeerContext object passed to StorageHandleProvider.");
        ArgumentCheck.checkNotNull(storage, "Cannot initialize a StorageHandleProvider object with a null Storage value.");

        delegate_ = new StorageDelegate(context.getLocalRT(), storage, context.getCorba());
        poa_ = null;
    }

    @Inject
    protected void setHashingAlgorithm(FilenameHashAlgorithm hashAlgorithm) {
        ArgumentCheck.checkNotNull(hashAlgorithm, "Tried to pass a null FilenameHashAlgorithm object at StorageProvider.");
        delegate_.setHashingAlgorithm(hashAlgorithm);
    }

    @Override
    public StorageHandlePOA get() {
        if (poa_ == null) {
            poa_ = new FileStorageHandle(delegate_);
        }

        return poa_;
    }
}
