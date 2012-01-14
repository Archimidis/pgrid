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

package pgrid.service.bootstrap.internal;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import pgrid.entity.Host;
import pgrid.entity.internal.PGridHost;
import pgrid.entity.routingtable.RoutingTable;
import pgrid.entity.utilities.IOUtilities;
import pgrid.service.bootstrap.FileBootstrapService;
import pgrid.service.bootstrap.PersistencyException;

import javax.xml.stream.*;
import javax.xml.stream.events.*;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.UnknownHostException;
import java.util.*;

/**
 * The default implementation of the {@link pgrid.service.bootstrap.FileBootstrapService} interface.
 * The format that stores into and loads from is xml.
 * There must be first a block with the tag "localpeer" that contains all the
 * information needed for the localhost. After that entry blocks with the tag
 * "host" can follow. These describe entries in the routing table. The tag also
 * has a value attribute indicating the level where the particular host belongs
 * to.
 * <p/>
 * At the moment a validating mechanism that check for constraints, such as the
 * position of the localpeer block, does not exists. Also there are no sanity
 * check (eg. host levels given are a lot higher than the path of the local
 * peer). Of course invalid values for host initialization will make the
 * process stop and throw exceptions.
 * <p/>
 * Example routingTable.xml:
 * <pre>
 * {@code
 * <RoutingTable>
 *     <localpeer>
 *         <address>localhost</address>
 *         <port>3000</port>
 *         <path>000</path>
 *         <timestamp>10001</timestamp>
 *         <uuid>12345678-1231-1234-1234-0123456789ad</uuid>
 *     </localpeer>
 *
 *     <host level="0">
 *         <address>localhost</address>
 *         <port>3000</port>
 *         <path>1</path>
 *         <timestamp>1</timestamp>
 *         <uuid>10d82159-3713-4b78-884e-4104720ae2d8</uuid>
 *     </host>
 * </RoutingTable>
 * }
 * </pre>
 *
 * @author Vourlakis Nikolas <nvourlakis@gmail.com>
 */
public class XMLBootstrapService implements FileBootstrapService {

    private static final Logger logger_ = LoggerFactory.getLogger(XMLBootstrapService.class);

    private static final String ROOT = "RoutingTable";
    private static final String LOCALPEER = "localpeer";
    private static final String HOST = "host";
    private static final String LEVEL = "level";
    private static final String ADDRESS = "address";
    private static final String PORT = "port";
    private static final String PATH = "path";
    private static final String UUID_STR = "uuid";
    private static final String TIMESTAMP = "timestamp";

    public void load(String file, RoutingTable routingTable) throws FileNotFoundException, PersistencyException {
        InputStream inStream = null;

        try {
            Map<Integer, List<Host>> map = new HashMap<Integer, List<Host>>();
            XMLInputFactory inputFactory = XMLInputFactory.newInstance();
            inStream = new FileInputStream(file);
            XMLEventReader eventReader = inputFactory.createXMLEventReader(inStream);

            int level = -1;
            String address = null;
            int port = -1;
            String path = "";
            String uuid = "";
            long ts = -1;

            while (eventReader.hasNext()) {
                XMLEvent event = eventReader.nextEvent();

                if (event.isStartElement()) {
                    StartElement startElement = event.asStartElement();
                    if (startElement.getName().getLocalPart().equals(HOST)) {
                        Iterator attributes = startElement.getAttributes();
                        while (attributes.hasNext()) {
                            Attribute attribute = (Attribute) attributes.next();
                            if (attribute.getName().toString().equals(LEVEL)) {
                                level = Integer.parseInt(attribute.getValue());
                            }
                        }
                    }

                    // Keep for Java 6
                    if (event.asStartElement().getName().getLocalPart().equals(ADDRESS)) {
                        event = eventReader.nextEvent();
                        address = event.asCharacters().getData();
                        continue;
                    }
                    if (event.asStartElement().getName().getLocalPart().equals(PORT)) {
                        event = eventReader.nextEvent();
                        port = Integer.parseInt(event.asCharacters().getData());
                        continue;
                    }

                    if (event.asStartElement().getName().getLocalPart().equals(PATH)) {
                        event = eventReader.nextEvent();
                        path = event.asCharacters().getData();
                        continue;
                    }

                    if (event.asStartElement().getName().getLocalPart().equals(UUID_STR)) {
                        event = eventReader.nextEvent();
                        uuid = event.asCharacters().getData();
                        continue;
                    }

                    if (event.asStartElement().getName().getLocalPart().equals(TIMESTAMP)) {
                        event = eventReader.nextEvent();
                        ts = Integer.parseInt(event.asCharacters().getData());
                        continue;
                    }

//                    switch (event.asStartElement().getName().getLocalPart()) { // java 7
//                        case ADDRESS:
//                            event = eventReader.nextEvent();
//                            address = event.asCharacters().getData();
//                            break;
//                        case PORT:
//                            event = eventReader.nextEvent();
//                            port = Integer.parseInt(event.asCharacters().getData());
//                            break;
//                        case PATH:
//                            event = eventReader.nextEvent();
//                            path = event.asCharacters().getData();
//                            break;
//                        case UUID_STR:
//                            event = eventReader.nextEvent();
//                            uuid = event.asCharacters().getData();
//                            break;
//                        case TIMESTAMP:
//                            event = eventReader.nextEvent();
//                            ts = Integer.parseInt(event.asCharacters().getData());
//                            break;
//                    }
                }

                if (event.isEndElement()) {
                    EndElement endElement = event.asEndElement();
                    if (endElement.getName().getLocalPart().equals(HOST)
                            || endElement.getName().getLocalPart().equals(LOCALPEER)) {
                        Host host = new PGridHost(address, port);
                        host.setHostPath(path);
                        host.setUUID(UUID.fromString(uuid));
                        if (ts > 0) {
                            host.setTimestamp(--ts);
                        } else {
                            host.resetTimestamp();
                        }
                        if (endElement.getName().getLocalPart().equals(HOST)) {
                            if (!map.containsKey(level)) {
                                map.put(level, new ArrayList<Host>(1));
                            }
                            map.get(level).add(host);
                        } else if (endElement.getName().getLocalPart().equals(LOCALPEER)) {
                            routingTable.setLocalhost(host);
                        }
                        level = -1;
                        address = null;
                        port = -1;
                        path = "";
                        uuid = "";
                        ts = -1;
                    }
                }
            }

            for (Map.Entry<Integer, List<Host>> entry : map.entrySet()) {
                routingTable.addReference(entry.getKey(), entry.getValue());
            }
        } catch (UnknownHostException e) {
            logger_.error("{}", e);
            throw new PersistencyException("Tried to construct a PGridHost " +
                    "object with illegal INetAddress and port values.");
        } catch (Exception e) {
            logger_.error("{}", e);
            throw new PersistencyException(e);
        } finally {
            IOUtilities.closeQuietly(inStream);
        }
    }

    public void store(String filename, RoutingTable routingTable) throws FileNotFoundException {
        FileOutputStream outStream = null;
        try {
            outStream = new FileOutputStream(filename);

            XMLOutputFactory outputFactory = XMLOutputFactory.newInstance();
            XMLEventWriter eventWriter = outputFactory
                    .createXMLEventWriter(outStream);

            XMLEventFactory eventFactory = XMLEventFactory.newInstance();
            XMLEvent end = eventFactory.createDTD("\n");
            XMLEvent tab = eventFactory.createDTD("\t");
            XMLEvent doubleTab = eventFactory.createDTD("\t\t");

            StartDocument startDocument = eventFactory.createStartDocument();
            eventWriter.add(startDocument);
            eventWriter.add(end);

            StartElement rtStartElement = eventFactory.createStartElement("", "", ROOT);
            eventWriter.add(rtStartElement);
            eventWriter.add(end);


            int levelIndex = 0;

            Host localpeer = routingTable.getLocalhost();
            StartElement localPeerStartElement = eventFactory.createStartElement("", "", LOCALPEER);
            eventWriter.add(tab);
            eventWriter.add(localPeerStartElement);
            eventWriter.add(end);

            createNode(eventWriter, doubleTab, ADDRESS, localpeer.getAddress().getHostName());
            createNode(eventWriter, doubleTab, PORT, Integer.toString(localpeer.getPort()));
            createNode(eventWriter, doubleTab, PATH, localpeer.getHostPath().toString());
            createNode(eventWriter, doubleTab, TIMESTAMP, Long.toString(localpeer.getTimestamp()));
            createNode(eventWriter, doubleTab, UUID_STR, localpeer.getUUID().toString());

            eventWriter.add(tab);
            eventWriter.add(eventFactory.createEndElement("", "", LOCALPEER));
            eventWriter.add(end);
            eventWriter.add(end);

            for (Collection<Host> levels : routingTable.getAllHostsByLevels()) {
                for (Host host : levels) {
                    StartElement hostStartElement = eventFactory.createStartElement("", "", HOST);
                    Attribute attribute = eventFactory.createAttribute(LEVEL, Integer.toString(levelIndex));
                    eventWriter.add(tab);
                    eventWriter.add(hostStartElement);
                    eventWriter.add(attribute);
                    eventWriter.add(end);

                    createNode(eventWriter, doubleTab, ADDRESS, host.getAddress().getHostName());
                    createNode(eventWriter, doubleTab, PORT, Integer.toString(host.getPort()));
                    createNode(eventWriter, doubleTab, PATH, host.getHostPath().toString());
                    createNode(eventWriter, doubleTab, TIMESTAMP, Long.toString(host.getTimestamp()));
                    createNode(eventWriter, doubleTab, UUID_STR, host.getUUID().toString());

                    eventWriter.add(tab);
                    eventWriter.add(eventFactory.createEndElement("", "", HOST));
                    eventWriter.add(end);
                }
                eventWriter.add(end);
                levelIndex++;
            }

            eventWriter.add(eventFactory.createEndElement("", "", ROOT));
            eventWriter.add(end);
            eventWriter.add(eventFactory.createEndDocument());
            eventWriter.close();
        } catch (XMLStreamException e) {
            logger_.error("{}", e);
        } finally {
            IOUtilities.closeQuietly(outStream);
        }
    }

    private void createNode(XMLEventWriter eventWriter, XMLEvent indent, String name, String value)
            throws XMLStreamException {
        XMLEventFactory eventFactory = XMLEventFactory.newInstance();
        XMLEvent end = eventFactory.createDTD("\n");

        // Create Start node
        StartElement sElement = eventFactory.createStartElement("", "", name);
        eventWriter.add(indent);
        eventWriter.add(sElement);
        // Create Content
        Characters characters = eventFactory.createCharacters(value);
        eventWriter.add(characters);
        // Create End node
        EndElement eElement = eventFactory.createEndElement("", "", name);
        eventWriter.add(eElement);
        eventWriter.add(end);

    }
}


