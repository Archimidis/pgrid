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

package demo.services;

import com.google.inject.Guice;
import com.google.inject.Injector;
import com.sun.corba.se.spi.logging.CORBALogDomains;
import org.hamcrest.core.Is;
import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;
import org.omg.CORBA.ORB;
import org.omg.CORBA.ORBPackage.InvalidName;
import org.omg.PortableServer.POA;
import org.omg.PortableServer.POAHelper;
import org.omg.PortableServer.POAManagerPackage.AdapterInactive;
import org.omg.PortableServer.POAPackage.ServantAlreadyActive;
import org.omg.PortableServer.POAPackage.ServantNotActive;
import org.omg.PortableServer.POAPackage.WrongPolicy;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import pgrid.entity.CorbaFactory;
import pgrid.entity.EntityFactory;
import pgrid.entity.EntityModule;
import pgrid.entity.Host;
import pgrid.entity.routingtable.RoutingTable;
import pgrid.service.CommunicationException;
import pgrid.service.LocalPeerContext;
import pgrid.service.ServiceModule;
import pgrid.service.corba.fileTransfer.TransferHandleHelper;
import pgrid.service.corba.fileTransfer.TransferHandlePOA;
import pgrid.service.fileTransfer.FileTransferModule;
import pgrid.service.fileTransfer.FileTransferService;

import java.io.*;
import java.net.UnknownHostException;
import java.util.logging.Level;

/**
 * @author Nikolas Vourlakis <nvourlakis@gmail.com>
 */
public class TransferTest {
    private static final Logger logger_ = LoggerFactory.getLogger(StorageTest.class);

    private static Injector injector_;

    private static final String localIP_ = "127.0.0.1";
    private static final int localPort_ = 3000;
    private static final String LOCAL_PATH = "";
    private static Host localhost_;

    private static final String SHARED_DIR = "test/demo/services/shared";
    private static final String DOWNLOAD_DIR = "test/demo/services/downloads";
    private static final String FILE_NAME = "Pirated_Document.txt";
    private static final String FILE_CONTENT =
            "If you can read this line then the file was downloaded successfully :)";

    @BeforeClass
    public static void initialize() {
        new File(SHARED_DIR).mkdir();
        new File(DOWNLOAD_DIR).mkdir();
        File test = new File(SHARED_DIR + File.separator + FILE_NAME);
        try {
            BufferedWriter writer = new BufferedWriter(new FileWriter(test));
            writer.write(FILE_CONTENT, 0, FILE_CONTENT.length());
            writer.flush();
            writer.close();
        } catch (IOException e) {
            logger_.error("Something went wrong during the creation of the test files");
        }

        injector_ = Guice.createInjector(
                new EntityModule(),
                new ServiceModule(localIP_, localPort_, Integer.MAX_VALUE),
                new FileTransferModule(SHARED_DIR, DOWNLOAD_DIR));
        try {
            CorbaFactory corbaFactory = injector_.getInstance(CorbaFactory.class);
            ORB orb = corbaFactory.getInstance(localIP_, localPort_);

            EntityFactory entityFactory = injector_.getInstance(EntityFactory.class);
            localhost_ = entityFactory.newHost(localIP_, localPort_);
            localhost_.setHostPath(LOCAL_PATH);
            RoutingTable routingTable = injector_.getInstance(RoutingTable.class);
            routingTable.setLocalhost(localhost_);

            LocalPeerContext context = injector_.getInstance(LocalPeerContext.class);
            context.setOrb(orb);
            context.setRoutingTable(routingTable);
            serviceRegistration();
        } catch (UnknownHostException e) {
            logger_.error("Something went wrong with the initialization of CORBA.");
        }
    }

    @AfterClass
    public static void deleteTestFiles() {
        new File(DOWNLOAD_DIR + File.separator + FILE_NAME).delete();
        new File(SHARED_DIR).delete();
        new File(SHARED_DIR + File.separator + FILE_NAME).delete();
        new File(DOWNLOAD_DIR).delete();
    }

    @Test
    public void execute() throws CommunicationException {
        // Scenario: Download a file from self.
        logger_.info("[Transfer service test start]");
        FileTransferService transferService = injector_.getInstance(FileTransferService.class);
        File downloaded = transferService.transfer("Pirated_Document.txt", localhost_);
        logger_.debug("Downloaded file {}", downloaded.getName());
        try {
            BufferedReader reader = new BufferedReader(new FileReader(downloaded));
            String downloadedContent = reader.readLine();
            logger_.debug("File contains: {}", downloadedContent);
            Assert.assertThat(downloadedContent, Is.is(FILE_CONTENT));
        } catch (FileNotFoundException e) {
            logger_.error("File {} does not exists. Failed to download it!", downloaded.getAbsolutePath());
        } catch (IOException e) {
            logger_.error("Failed to read from file {}.", downloaded.getAbsolutePath());
        }

    }

    private static void localPeerContextInit() throws UnknownHostException {

    }

    private static void serviceRegistration() {
        final ORB orb = injector_.getInstance(LocalPeerContext.class).getCorba();

        try {
            POA rootPOA = null;
            try {
                rootPOA = POAHelper.narrow(orb.resolve_initial_references("RootPOA"));
            } catch (InvalidName invalidName) {
                invalidName.printStackTrace();
            }
            rootPOA.the_POAManager().activate();

            //********** Transfer Service Registration  **********//
            TransferHandlePOA storageServant = injector_.getProvider(TransferHandlePOA.class).get();
            rootPOA.activate_object(storageServant);
            String[] ID = TransferHandleHelper.id().split(":");
            ((com.sun.corba.se.spi.orb.ORB) orb).register_initial_reference(
                    ID[1],
                    rootPOA.servant_to_reference(storageServant)
            );
            logger_.info("Transfer service registered");
        } catch (ServantNotActive servantNotActive) {
            servantNotActive.printStackTrace();
        } catch (WrongPolicy wrongPolicy) {
            wrongPolicy.printStackTrace();
        } catch (InvalidName invalidName) {
            invalidName.printStackTrace();
        } catch (AdapterInactive adapterInactive) {
            adapterInactive.printStackTrace();
        } catch (ServantAlreadyActive servantAlreadyActive) {
            servantAlreadyActive.printStackTrace();
        }

        Thread orbThread_ = new Thread(new Runnable() {
            private final ORB orb_ = orb;

            @Override
            public void run() {
                orb_.run();
            }
        });
        orbThread_.start();
        logger_.info("Initialization finished!");

        // shutdown logging
        ((com.sun.corba.se.spi.orb.ORB) orb).getLogger(CORBALogDomains.RPC).setLevel(Level.OFF);
    }
}
