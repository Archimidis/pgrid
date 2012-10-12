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

package pgrid.process.downloadFile;

/**
 * This process searches and downloads a file that exists in the pgrid network.
 * <p/>
 * <p/>
 * <h1> This service is a demo for the presentation of the thesis only. Don't
 * expect serious and complete functionality. <h1/>
 *
 * @author Nikolas Vourlakis <nvourlakis@gmail.com>
 */
public interface DownloadFileProcess {

    /**
     * Given a filename it searches for existence in the network.If the file
     * exists then it gets downloaded and stored to a specific folder of the
     * localhost. If a network error arises it must be fixed.
     *
     * @param filename to be downloaded.
     */
    public Status download(String filename);

    enum Status {
        FILE_NOT_FOUND,
        NETWORK_ERROR,
        FILE_DOWNLOADED
    }
}
