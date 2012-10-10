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

import org.omg.CORBA.ORB;
import org.omg.CORBA.SystemException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import pgrid.entity.Host;
import pgrid.service.CommunicationException;
import pgrid.service.corba.fileTransfer.TransferHandle;
import pgrid.service.corba.fileTransfer.TransferHandleHelper;
import pgrid.utilities.ArgumentCheck;

import java.io.*;

/**
 * This class is responsible to answer any transfer requests given to the host as
 * a client or as a server.
 * <p/>
 * <b>This class is for internal use only.</b>
 *
 * @author Nikolas Vourlakis <nvourlakis@gmail.com>
 */
public class FileTransferDelegate {

    private static final Logger logger_ = LoggerFactory.getLogger(FileTransferDelegate.class);

    private final ORB orb_;

    private static String DOWNLOAD_DIR;
    private static String SHARED_DIR;

    public FileTransferDelegate(ORB orb) {
        ArgumentCheck.checkNotNull(orb, "Cannot initialize a RepairDelegate object with a null ORB value.");

        orb_ = orb;
    }

    /**
     * Sets the directory that will be used for storing the files that will
     * be transferred from remote hosts and the directory where the local host
     * stores its files.
     *
     * @param downloadDir the directory where the transferred files will end.
     * @param sharedDir   the directory that contains the files of the local
     *                    host.
     */
    public void setDirectories(String downloadDir, String sharedDir) {
        DOWNLOAD_DIR = downloadDir;
        SHARED_DIR = sharedDir;
    }

    /**
     * The local host will start a transfer operation with the given remote
     * host. It is supposed that the remote host has the file.
     * <p/>
     * <b>This method is meant to be used by the CLIENT side of the service.</b>
     *
     * @param filename  to be downloaded from the remote host.
     * @param fileOwner host that physically owns the file.
     * @return a {@link File} object linked with the downloaded file.
     * @throws CommunicationException if a communication error arise between
     *                                the local and the remote host.
     */
    public File receiveFile(String filename, Host fileOwner) throws CommunicationException {
        logger_.info("Starting a transfer operation of file {} from host {}:{}.",
                new Object[]{filename, fileOwner.getAddress(), fileOwner.getPort()});
        File transferredFile = null;
        TransferHandle remoteHandle = getRemoteHandle(fileOwner);
        byte[] data = remoteHandle.transfer(filename);
        String filePath = DOWNLOAD_DIR + File.separator + filename;
        logger_.debug("Transferring has completed, {} bytes received.", data.length);
        try {
            BufferedOutputStream output =
                    new BufferedOutputStream(new FileOutputStream(filePath));
            output.write(data, 0, data.length);
            output.flush();
            output.close();
            transferredFile = new File(filePath);
        } catch (FileNotFoundException ignored) {
        } catch (IOException ignored) {
        }

        return transferredFile;
    }

    /**
     * A remote host requests a certain file. This is the method to send the
     * file.
     * <p/>
     * <b>This method is meant to be used by the SERVER side of the service.</b>
     *
     * @param filename to send.
     * @return a byte stream of the file.
     */
    public byte[] sendFile(String filename) {
        String filePath = SHARED_DIR + File.separator + filename;
        logger_.info("Requested a transfer operation of file {}.", filePath);
        int i;
        StringBuilder sValue = new StringBuilder();
        try {
            FileInputStream fin = new FileInputStream(filePath);

            /*Transfer the file to a string variable*/
            do {
                i = fin.read(); //Read the file contents
                if (i != -1)
                    sValue.append((char) i);
            } while (i != -1);
            //Until end of the file is reached, copy the contents
            fin.close();
        } catch (FileNotFoundException e) {
            logger_.error("File {} that was requested not found.", filePath);
        } catch (IOException ignored) {
        }
        //return sValue; //Return the file as string to the client
        return sValue.toString().getBytes();
    }

    /**
     * Given a host, it will return a corba handle of the transfer interface
     * stub so the local host can execute rpc.
     *
     * @param host that the local host wants to communicate with.
     * @return the transfer interface stub.
     * @throws pgrid.service.CommunicationException
     *          if the remote host cannot be reached.
     */
    private TransferHandle getRemoteHandle(Host host) throws CommunicationException {
        String[] transferHandleID = TransferHandleHelper.id().split(":");
        String corbaloc = "corbaloc:iiop:["
                + host.getAddress().getHostAddress() + "]:" + host.getPort()
                + "/" + transferHandleID[1];
        logger_.debug("CORBALOC: {}", corbaloc);
        org.omg.CORBA.Object object = orb_.string_to_object(corbaloc);

        TransferHandle handle;
        try {
            handle = TransferHandleHelper.narrow(object);
        } catch (SystemException e) {
            throw new CommunicationException(e.getCause());
        }
        return handle;
    }
}
