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

package pgrid.service.StorageService.internal;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import pgrid.entity.Host;
import pgrid.service.CommunicationException;
import pgrid.service.StorageService.StorageService;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;

import java.io.File;

/**
 * <b>NOTE: </b>
 * If an error occurred while executing the method {@link #ownerOf(String)}, a
 * null {@link Host} will be returned. No repair will be initiated. That's not
 * the purpose of the file sharing demo.
 *
 * @author Nikolas Vourlakis <nvourlakis@gmail.com>
 */
public class FileStorageService implements StorageService {

    private static final Logger logger_ = LoggerFactory.getLogger(StorageDelegate.class);

    private final StorageDelegate delegate_;

    public FileStorageService(StorageDelegate delegate) {
        delegate_ = delegate;
    }

    @Override
    public void store(File filename) {
        throw new NotImplementedException();
    }

    @Override
    public Host ownerOf(String filename) {
        Host owner = null;
        try {
            owner = delegate_.iterativeSearch(filename);
        } catch (CommunicationException e) {
            logger_.info("An error occurred in the network during the search of the filename.");
        }
        return owner;
    }
}
