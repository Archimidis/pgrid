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

package demo.process;

import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.Key;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import pgrid.entity.EntityModule;
import pgrid.entity.Host;
import pgrid.process.doanloadFile.DownloadFileModule;
import pgrid.process.doanloadFile.DownloadFileProcess;
import pgrid.process.initialization.InitializationModule;
import pgrid.process.initialization.SystemInitializationProcess;
import pgrid.service.LocalPeerContext;
import pgrid.service.ServiceModule;
import pgrid.service.ServiceRegistration;
import pgrid.service.fileTransfer.FileTransfer;
import pgrid.service.fileTransfer.FileTransferModule;
import pgrid.service.repair.Repair;
import pgrid.service.storage.Storage;
import pgrid.service.storage.StorageServiceModule;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

/**
 * @author Nikolas Vourlakis <nvourlakis@gmail.com>
 */
public class DownloadFileTest {

    private static final Logger logger_ = LoggerFactory.getLogger(DownloadFileTest.class);

    private String address_ = "127.0.0.1";
    private int port_ = 3000;
    private String path_ = "";
    private int maxRef_ = 2;

    private static final String SHARED_DIR = "test/demo/process/shared";
    private static final String DOWNLOAD_DIR = "test/demo/process/downloads";
    private static final String FILE_NAME = "Pirated_Document.txt";
    private static final String FILE_CONTENT = "Hello PGrid! Yaaarrr";

    @Test
    public void main() {
        Injector injector = Guice.createInjector(
                new EntityModule(),
                new ServiceModule(address_, port_, maxRef_),
                new StorageServiceModule(),
                new FileTransferModule(SHARED_DIR, DOWNLOAD_DIR),
                new InitializationModule(),
                new DownloadFileModule());

        SystemInitializationProcess initProcess =
                injector.getInstance(SystemInitializationProcess.class);

        try {
            initProcess.load("test/demo/process/routing_table.xml");
            ServiceRegistration[] registrations = {
                    injector.getInstance(Key.get(ServiceRegistration.class, Repair.class)),
                    injector.getInstance(Key.get(ServiceRegistration.class, Storage.class)),
                    injector.getInstance(Key.get(ServiceRegistration.class, FileTransfer.class))
            };
            initProcess.serviceRegistration(registrations);
        } catch (Exception e) {
            logger_.error("Error during service registration. {}", e);
            System.exit(1);
        }

        LocalPeerContext context = injector.getInstance(LocalPeerContext.class);
        Host localhost = context.getLocalRT().getLocalhost();

        logger_.debug("Localhost");
        logger_.debug("[{}] {}:{} - '{}'", new Object[]{localhost.getHostPath(), localhost, localhost.getPort(), localhost.getUUID()});
        logger_.debug("Routing Table ({} unique hosts and {} levels)", context.getLocalRT().uniqueHostsNumber(), context.getLocalRT().levelNumber());
        for (Host host : context.getLocalRT().getAllHosts()) {
            logger_.debug("[{}] {}:{} - '{}'", new Object[]{host.getHostPath(), host, host.getPort(), host.getUUID()});
        }

        initProcess.startServer();

        DownloadFileProcess downloadFileProcess = injector.getInstance(DownloadFileProcess.class);
        DownloadFileProcess.Status status = downloadFileProcess.download(FILE_NAME);

        switch (status) {
            case FILE_DOWNLOADED:
                logger_.info("File found and downloaded!");
                break;
            case FILE_NOT_FOUND:
                logger_.info("File not found");
                break;
            case NETWORK_ERROR:
                // If it enters here then paranormal activities are happening!
                logger_.info("Network error");
                break;
        }
    }

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
    }

    @AfterClass
    public static void deleteTestFiles() {
        new File(SHARED_DIR + File.separator + FILE_NAME).delete();
        new File(SHARED_DIR).delete();
        new File(DOWNLOAD_DIR + File.separator + FILE_NAME).delete();
        new File(DOWNLOAD_DIR).delete();
    }
}
