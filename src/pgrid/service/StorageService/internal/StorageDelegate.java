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


import org.omg.CORBA.ORB;
import org.omg.CORBA.SystemException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import pgrid.entity.Host;
import pgrid.entity.Key;
import pgrid.entity.PGridPath;
import pgrid.entity.internal.PGridKey;
import pgrid.entity.routingtable.RoutingTable;
import pgrid.entity.storage.FilenameHashAlgorithm;
import pgrid.entity.storage.Storage;
import pgrid.service.CommunicationException;
import pgrid.service.corba.storage.SearchRequest;
import pgrid.service.corba.storage.SearchResponse;
import pgrid.service.corba.storage.StorageHandle;
import pgrid.service.corba.storage.StorageHandleHelper;
import pgrid.utilities.ArgumentCheck;
import pgrid.utilities.Deserializer;
import pgrid.utilities.Serializer;

import java.util.Collection;

/**
 * This class is responsible to answer any search requests given to the host as
 * a client or as a server.
 * <p/>
 * <b>This class is for internal use only.</b>
 *
 * @author Nikolas Vourlakis <nvourlakis@gmail.com>
 */
public class StorageDelegate {

    private static final Logger logger_ = LoggerFactory.getLogger(StorageDelegate.class);

    private final RoutingTable routingTable_;
    private final Storage storage_;
    private final ORB orb_;

    private FilenameHashAlgorithm hashingAlgo_;

    public StorageDelegate(RoutingTable routingTable, Storage storage, ORB orb) {
        ArgumentCheck.checkNotNull(routingTable, "Cannot initialize a RepairDelegate object with a null RoutingTable value.");
        ArgumentCheck.checkNotNull(storage, "Cannot initialize a RepairDelegate object with a null Storage value.");
        ArgumentCheck.checkNotNull(orb, "Cannot initialize a RepairDelegate object with a null ORB value.");

        routingTable_ = routingTable;
        storage_ = storage;
        orb_ = orb;
    }

    /**
     * Sets the file hashing algorithm to be used.
     *
     * @param hashingAlgorithm fully initialized {@link FilenameHashAlgorithm} object.
     */
    public void setHashingAlgorithm(FilenameHashAlgorithm hashingAlgorithm) {
        ArgumentCheck.checkNotNull(hashingAlgorithm, "Tried to pass a null FilenameHashAlgorithm object at StorageDelegate.");
        hashingAlgo_ = hashingAlgorithm;
    }

    /**
     * The local host will begin a search operation. The search is performed
     * iteratively. The local host asks one remote host at a time. He then
     * decides if he will continue the search or not based on the response of
     * that remote host.
     * <p/>
     * <b>This method is meant to be used by the CLIENT side of the service.</b>
     *
     * @param filename that will be searched.
     * @return the host that owns the file.
     * @throws CommunicationException in case of a communication error.
     */
    public Host iterativeSearch(String filename) throws CommunicationException {
        ArgumentCheck.checkNotNull(filename, "Cannot initiate or continue a search operation with a null filename.");

        Key fileKey = hashingAlgo_.produceKey(filename);
        SearchRequest request = new SearchRequest(fileKey.toString(), filename);
        Host probableOwner = continuation(fileKey);
        Host localhost = routingTable_.getLocalhost();

        if (!(localhost.compareTo(probableOwner) == 0) ||
                !(localhost.getHostPath().isConjugateTo(probableOwner.getHostPath()))) {
            StorageHandle handle = getRemoteHandle(probableOwner);
            SearchResponse response = handle.search(request);
            if (response.found) {
                probableOwner = Deserializer.deserializeHost(response.peer);
            } else {
                probableOwner = iterativeSearch(filename);
            }
        }

        return probableOwner;
    }

    /**
     * Upon a request receive, the local host will define if he has found the
     * owner of the file that was requested. He will form the response and send
     * it.
     * <p/>
     * <b>This method is meant to be used by the SERVER side of the service.</b>
     *
     * @param request from a remote host ready to be served.
     * @return a {@link SearchResponse}.
     */
    public SearchResponse serveRequest(SearchRequest request) {
        ArgumentCheck.checkNotNull(request, "Cannot serve a request if a null object is given.");

        Host probableOwner = continuation(new PGridKey(request.key));
        Host localhost = routingTable_.getLocalhost();

        SearchResponse response = new SearchResponse();
        response.peer = Serializer.serializeHost(probableOwner);
        response.found = (probableOwner.compareTo(localhost) == 0) ||
                (probableOwner.getHostPath().isConjugateTo(localhost.getHostPath())) ||
                storage_.containsFile(request.filename) ||
                storage_.containsFileKey(request.key);

        return response;
    }

    /**
     * Given a {@link Key}, it will find a host known to the local host that is
     * a probable owner of the file or knows the owner.
     *
     * @param fileKey based on which the next host will be found.
     * @return the host that the search will continue on.
     */
    public Host continuation(Key fileKey) {
        ArgumentCheck.checkNotNull(fileKey);

        Host nextHost = null;
        Collection<Host> list = routingTable_.closestHosts(fileKey.toString());
        Host[] hosts = list.toArray(new Host[list.size()]);
        if (hosts.length == 1) {
            nextHost = hosts[0];
        } else if (hosts.length > 1) {
            PGridPath filenameToPath = new PGridPath(fileKey.toString());
            nextHost = hosts[0];
            for (int i = 1; i < hosts.length; i++) {
                int probOwnerLen = nextHost.getHostPath().commonPrefix(filenameToPath).length();
                int otherHostLen = hosts[i].getHostPath().commonPrefix(filenameToPath).length();
                if (probOwnerLen < otherHostLen) {
                    nextHost = hosts[i];
                }
            }
        }
        return nextHost;
    }

    /**
     * Given a host, it will return a corba handle of the storage interface
     * stub so the local host can execute rpc.
     *
     * @param host that the local host wants to communicate with.
     * @return the storage interface stub.
     * @throws CommunicationException if the remote host cannot be reached.
     */
    public StorageHandle getRemoteHandle(Host host) throws CommunicationException {
        String[] repairHandleID = StorageHandleHelper.id().split(":");
        String corbaloc = "corbaloc:iiop:["
                + host.getAddress().getHostAddress() + "]:" + host.getPort()
                + "/" + repairHandleID[1];
        logger_.debug("CORBALOC: {}", corbaloc);
        org.omg.CORBA.Object object = orb_.string_to_object(corbaloc);

        StorageHandle handle;
        try {
            handle = StorageHandleHelper.narrow(object);
        } catch (SystemException e) {
            throw new CommunicationException(e.getCause());
        }
        return handle;
    }
}
