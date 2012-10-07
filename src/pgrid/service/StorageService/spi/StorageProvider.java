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

package pgrid.service.StorageService.spi;

import pgrid.entity.storage.FilenameHashAlgorithm;
import pgrid.entity.storage.Storage;
import pgrid.service.LocalPeerContext;
import pgrid.service.StorageService.StorageService;
import pgrid.service.StorageService.internal.FileStorageService;
import pgrid.service.StorageService.internal.StorageDelegate;
import pgrid.utilities.ArgumentCheck;

import javax.inject.Inject;
import javax.inject.Provider;

/**
 * @author Nikolas Vourlakis <nvourlakis@gmail.com>
 */
public class StorageProvider implements Provider<StorageService> {

    private final LocalPeerContext context_;
    private final Storage storage_;

    private FilenameHashAlgorithm hashAlgorithm_;

    @Inject
    public StorageProvider(LocalPeerContext context, Storage storage) {
        ArgumentCheck.checkNotNull(context, "Cannot initialize a StorageProvider object with a null LocalPeerContext value.");
        ArgumentCheck.checkNotNull(context.getCorba(), "Uninitialized ORB in LocalPeerContext object passed to StorageProvider.");
        ArgumentCheck.checkNotNull(context.getLocalRT(), "Uninitialized RoutingTable in LocalPeerContext object passed to StorageProvider.");
        ArgumentCheck.checkNotNull(storage, "Cannot initialize a StorageProvider object with a null Storage value.");

        context_ = context;
        storage_ = storage;
    }

    @Inject
    protected void setHashingAlgorithm(FilenameHashAlgorithm hashAlgorithm) {
        ArgumentCheck.checkNotNull(hashAlgorithm, "Tried to pass a null FilenameHashAlgorithm object at StorageProvider.");
        hashAlgorithm_ = hashAlgorithm;
    }

    @Override
    public StorageService get() {
        StorageDelegate delegate_ = new StorageDelegate(context_.getLocalRT(), storage_, context_.getCorba());
        delegate_.setHashingAlgorithm(hashAlgorithm_);

        return new FileStorageService(delegate_);
    }
}
