/*
 * This file (pgrid.entity.routingtable.RoutingTable) is part of the libpgrid project.
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

package pgrid.entity.routingtable;

import pgrid.entity.Host;
import pgrid.entity.PGridPath;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * This class represents the routing table of the peer and stores pairs
 * containing a {@link pgrid.entity.internal.PGridHost} associated with a level. The levels
 * of the routing table will always be equal to the total path length of the
 * host that this table belongs to. That is [0, ..., path.length).Independent
 * of path schematics, the path can be found by considering the level number
 * and the path of this peer holding this routing table. For example, if the
 * peer has the path "01", then level 0 stores a host associated with path "1",
 * and level 1 stores a host responsible for path "00".
 * Finally, for the same level the routing table holds a number of hosts that
 * are associated with it according to path schematics. In the previous
 * example, level 1 will contain all the hosts that are in prefix relation with
 * the path "00". Level 0 will still have one host. The reasoning is that hosts
 * more specialized need to be in bigger levels. This speed ups lookup
 * operations and various other searches in the network.
 *
 * @author Vourlakis Nikolas <nvourlakis@gmail.com>
 */
public class RoutingTable {

    private Host localhost_ = null;

    private final List<Set<Host>> references_ =
            new Vector<Set<Host>>();
    private final Map<UUID, Host> uuidRefs_ = new ConcurrentHashMap<UUID, Host>();

    /**
     * Constructor.
     */
    public RoutingTable() {
    }

    public void setLocalhost(Host localhost) {
        if (localhost == null) {
            throw new NullPointerException();
        }
        localhost_ = localhost;
        createMissingLevels(localhost_.getHostPath().length() - 1);
    }

    public Host getLocalhost() {
        return localhost_;
    }

    public synchronized void update(RoutingTable routingTable, int commonLength, int refMax) {
        // [Sanity check] In case the localhost has changed its path in the meantime.
        if (commonLength > localhost_.getHostPath().length()) {
            commonLength = localhost_.getHostPath().length();
        }

        // [0, commonLength) -> union & randomSelect per level
        if (commonLength > 0) {
            for (int i = 0; i < commonLength; i++) {
                Collection<Host> commonRefs =
                        union(getLevel(i), routingTable.getLevel(i));
                updateLevel(i, randomSelect(refMax, commonRefs));
            }
        }

        int lLen = getLocalhost().getHostPath().length();
        int rLen = routingTable.getLocalhost().getHostPath().length();
        if ((lLen > commonLength) && (rLen > commonLength)) {
            addReference(commonLength, routingTable.getLocalhost());
        }
        // TODO: [ToComplete] Check the path of the localhost and all the hosts stored. Add/remove levels accordingly.
        int len = localhost_.getHostPath().length() - levelNumber();
        System.out.println("Changed path: " + len);
        if (len > 0) {
            createMissingLevels(localhost_.getHostPath().length() - 1);
        } else if (len < 0) {
            Collection<Host> hosts = new ArrayList<Host>();
            int start = levelNumber() + len;
            int end = levelNumber();
            System.out.println("[" + start + ", " + end + ")");
            for (int i = end - 1; i >= start; i--) {
                Set<Host> level = references_.get(i);
                hosts.addAll(level);
//                for (Host host : level) {
//                    removeReference(host);
//                }
                references_.remove(i);
            }
            System.out.println("levels: " + levelNumber());
            System.out.println(uuidRefs_.size());
            for (Host host : hosts) {
                System.out.println(host.getAddress().getHostAddress() + ":" + host.getPort());
            }
        }
    }

    /**
     * Adds the new host to the specified level in the routing table. The level
     * is invalid if it is negative or if it surpasses the length of the local
     * host. If the host is already in the routing table, when this method
     * will terminate correctly, the host will be only in the level specified.
     *
     * @param level where the host will be added.
     * @param host  to be added.
     */
    public synchronized void addReference(int level, Host host) {
        if (host == null) {
            throw new NullPointerException();
        }
        if (level < 0) {
            throw new IllegalArgumentException("Negative level given");
        }
        if (level >= localhost_.getHostPath().length()) {
            throw new IllegalArgumentException("Level surpasses localhost path length");
        }

        createMissingLevels(level);
        removeReference(host);
        references_.get(level).add(host);
        uuidRefs_.put(host.getUUID(), host);
    }

    /**
     * Adds a collection of hosts to the specified level in the routing table.
     * The level is invalid if it is negative or if it surpasses the length of
     * the local host. When the method terminates correctly, the specified
     * level will contain the union of the given collection with the all the
     * host it contained before. There will be no duplicates in case a host
     * in the collection to be added was in any level before of the routing
     * table before the union.
     *
     * @param level where the hosts will be added.
     * @param hosts to be added.
     */
    public synchronized void addReference(int level, Collection<Host> hosts) {
        if (hosts == null) {
            throw new NullPointerException();
        }
        if (level < 0) {
            throw new IllegalArgumentException("Negative level given");
        }
        if (level >= localhost_.getHostPath().length()) {
            throw new IllegalArgumentException("Level surpasses localhost path length");
        }

        createMissingLevels(level);
        for (Host host : hosts) {
            removeReference(host);
        }

        references_.get(level).addAll(union(references_.get(level), hosts));

        for (Host host : hosts) {
            uuidRefs_.put(host.getUUID(), host);
        }
    }

    /**
     * It updates completely the hosts store in a particular valid level. The
     * level is invalid if it is negative or if it surpasses the length of the
     * local host. If the given hosts were already in the routing table
     * partially or all of them at others level, after the method completes,
     * they will be only in the level specified.
     *
     * @param level where the old hosts will be replaced.
     * @param hosts to replace the old hosts.
     */
    public synchronized void updateLevel(int level, Collection<Host> hosts) {
        if (hosts == null) {
            return;
        }

        if (level < 0) {
            throw new IllegalArgumentException("Negative level given");
        }
        if (level >= localhost_.getHostPath().length()) {
            throw new IllegalArgumentException("Level surpasses localhost path length");
        }

        for (Host host : hosts) {
            removeReference(host);
        }

        Collection<Host> result = union(references_.get(level), hosts);
        references_.get(level).clear();
        references_.get(level).addAll(result);
        for (Host host : hosts) {
            uuidRefs_.put(host.getUUID(), host);
        }
    }

    /**
     * Given the set of hosts contained in the referenced level plus the
     * given host, it selects randomly refMax hosts. This new set will
     * completely update the given valid level. The level is invalid if it is
     * negative or if it surpasses the length of the local host.
     * <p/>
     * The Host argument can be null. In that case it depends only on the value
     * of refMax, so that the resulting updated level will have equal or less
     * size than the initial.
     *
     * @param level  which will be updated.
     * @param host   that will be mixed with the existing hosts of the level.
     * @param refMax the maximum number of hosts that will be chosen to update
     *               the level.
     */
    public void updateLevelRandomly(int level, Host host, int refMax) {
        if (level < 0) {
            throw new IllegalArgumentException("Negative level given");
        }
        if (level >= localhost_.getHostPath().length()) {
            throw new IllegalArgumentException("Level surpasses localhost path length");
        }
        if (refMax < 0) {
            throw new IllegalArgumentException("Negative refMax given.");
        }

        if (host != null) {
            List<Host> r = new ArrayList<Host>(1);
            r.add(host);
            Collection<Host> hosts = randomSelect(refMax, union(r, getLevel(level)));
            getLevel(level).clear();
            updateLevel(level, hosts);
        } else {
            if (refMax < getLevel(level).size()) {
                Collection<Host> hosts = randomSelect(refMax, getLevel(level));
                getLevel(level).clear();
                updateLevel(level, hosts);
            }
        }
    }

    /**
     * If the given host exists in the routing table, it will update the
     * information stored about him. If the host is not contained then nothing
     * will happen.
     *
     * @param host the host to update.
     */
    public synchronized void updateReference(Host host) {
        if (host == null) {
            throw new NullPointerException();
        }

        if (uuidRefs_.containsKey(host.getUUID())) {
            for (Set<Host> treeSet : references_) {
                if (treeSet.contains(host)) {
                    treeSet.remove(host);
                    treeSet.add(host);
                    uuidRefs_.remove(host.getUUID());
                    uuidRefs_.put(host.getUUID(), host);
                }
            }
        }
    }

    /**
     * It performs the union between the level specified of this routing table
     * and different one. It may be possible that the two routing tables will
     * not have the specified level cause of the host path associated with
     * them. In that case nothing happens.
     *
     * @param level        where the union will happen.
     * @param routingTable to be united with this routing table.
     */
    public void unionLevel(int level, RoutingTable routingTable) {
        if (routingTable == null) {
            throw new NullPointerException();
        }
        if (level < 0) {
            throw new IllegalArgumentException("Negative level given");
        }
        if (level >= localhost_.getHostPath().length()) {
            throw new IllegalArgumentException("Level surpasses localhost path length");
        }

        if (level >= routingTable.levelNumber()) {
            return;
        }

        Collection<Host> other = routingTable.getLevel(level);

        updateLevel(level, other);
    }

    /**
     * Returns a collection with all the hosts contained in the specified
     * valid level. The level is invalid if it is negative or if it surpasses
     * the length of the local host. This method may return an empty level in
     * case the local host has updated its path but has not received any new
     * references yet.
     *
     * @param level to get the hosts from.
     * @return a collection with all hosts.
     */
    public Collection<Host> getLevel(int level) {
        if (level < 0) {
            throw new IllegalArgumentException("Negative level given");
        }
        if (level >= localhost_.getHostPath().length()) {
            throw new IllegalArgumentException("Level surpasses localhost path length");
        }
        return references_.get(level);
    }

    /**
     * Retrieve a collections of hosts that mirrors the locations of the hosts
     * stored in the routing table. The collection will have each level and the
     * level will contain all the hosts according to the routing table. This
     * method may return empty levels in case the local host has updated its
     * path but has not received any new references yet.
     *
     * @return a collection within a collection with the hosts ordered by the
     *         level they belong to.
     */
    public Collection<Collection<Host>> getAllHostsByLevels() {
        Collection<Collection<Host>> result = new ArrayList<Collection<Host>>();

        if (references_.size() == 0) {
            return result;
        }

        for (Set<Host> treeSet : references_) {
            result.add(new ArrayList<Host>(treeSet));
        }

        return result;
    }

    /**
     * It will return all the hosts that this routing table contains.
     *
     * @return a collections with the hosts.
     */
    public Collection<Host> getAllHosts() {
        List<Host> result = new ArrayList<Host>(uuidRefs_.size());
        Collections.copy(result, (List<Host>) uuidRefs_.values());
        return result;
    }

    /**
     * Removes the given host if it exists from the routing table.
     *
     * @param host to be removed.
     */
    public synchronized void removeReference(Host host) {
        if (host == null) {
            throw new NullPointerException();
        }

        if (!uuidRefs_.containsKey(host.getUUID())) {
            return;
        }

        for (Set<Host> treeSet : references_) {
            treeSet.remove(host);
            uuidRefs_.remove(host.getUUID());
        }
    }

    /**
     * Removes a level.
     *
     * @param level the level index.
     */
    public synchronized void removeLevel(int level) {
        if (level < 0) {
            throw new IllegalArgumentException("Negative level given");
        }
        if (level >= localhost_.getHostPath().length()) {
            throw new IllegalArgumentException("Level surpasses localhost path length");
        }
        references_.remove(level);
    }

    /**
     * Checks if the routing table contains the given host.
     *
     * @param host to be checked for existence.
     * @return true if it exists, false else.
     */
    public boolean contains(Host host) {
        return uuidRefs_.containsKey(host.getUUID());
    }

    /**
     * Returns the number of the levels that this routing table contains
     * regardless if some of them are empty.
     *
     * @return the number of levels.
     */
    public int levelNumber() {
        return references_.size();
    }

    /**
     * Returns the number of the unique hosts contained in this routing table.
     *
     * @return the number of unique hosts.
     */
    public int uniqueHostsNumber() {
        return uuidRefs_.size();
    }

    /**
     * Clears the routing table.
     */
    public synchronized void clear() {
        references_.clear();
        uuidRefs_.clear();
    }

    /**
     * It returns the host with the given {@link UUID}. If there isn't a host
     * with that UUID a null value will be returned.
     *
     * @param uuid of the host.
     * @return the host associated with the given UUID.
     */
    public Host selectUUIDHost(UUID uuid) {
        return uuidRefs_.get(uuid);
    }

    /**
     * This method will return a collection of hosts that are in prefix
     * relation with the given path or are more likely to know about it.
     *
     * @param searchPath to search for hosts with the closest path to that.
     * @return a collection of hosts.
     */
    public Collection<Host> closestHosts(String searchPath) {
        Collection<Host> closestLevel;

        String prefix = localhost_.getHostPath().commonPrefix(new PGridPath(searchPath));
        int prefixLen = prefix.length();
        int searchLen = searchPath.length();

        if (searchPath.isEmpty() || prefixLen == searchLen || prefixLen == localhost_.getHostPath().length()) {
            closestLevel = new ArrayList<Host>(1);
            closestLevel.add(getLocalhost());
        } else if (prefix.isEmpty()) {
            closestLevel = new ArrayList<Host>(getLevel(0));
        } else {
            int level = prefixLen < levelNumber() ? prefixLen : (levelNumber() - 1);
            closestLevel = new ArrayList<Host>(getLevel(level));
            if (closestLevel.isEmpty()) {
                return closestHosts(searchPath.substring(0, searchPath.length() - 1));
            }
        }

        return closestLevel;
    }

    /**
     * Performs the union of two collections of hosts. The result will have
     * not have any duplicates in case both collections contain some common
     * hosts.
     *
     * @param refs1 the first collection.
     * @param refs2 the second collection.
     * @return the union.
     */
    public static Collection<Host> union(Collection<Host> refs1, Collection<Host> refs2) {
        if (refs1 == null || refs2 == null) {
            throw new NullPointerException();
        }
        Collection<Host> result = new TreeSet<Host>(refs1);
        result.addAll(refs2);

        return result;
    }

    /**
     * Given a collection with hosts, it returns a random subset containing
     * refMax of these hosts.
     *
     * @param refMax     the maximum host to choose from the collection.
     * @param commonRefs to random select from.
     * @return a collection with all the selected hosts.
     */
    public static Collection<Host> randomSelect(int refMax, Collection<Host> commonRefs) {
        if (commonRefs == null) {
            throw new NullPointerException();
        }
        if (refMax < 0) {
            throw new IllegalArgumentException("Negative refMax given");
        }

        int choose = (refMax <= commonRefs.size()) ? refMax : commonRefs.size();

        List<Host> copy = new Vector<Host>(commonRefs);
        Collections.shuffle(copy);
        return copy.subList(0, choose);
    }

    /**
     * Helper method to initialize all the missing levels from the references
     * list.
     *
     * @param level initialization of all the intermediate levels till that
     *              level.
     */
    private synchronized void createMissingLevels(int level) {
        // level should be valid cause it is checked from the public method that called this.
        if (level >= references_.size() && level < localhost_.getHostPath().length()) {
            int end = Math.max(level, references_.size());
            int start = Math.min(level, references_.size());

            for (int i = start; i <= end; i++) {
                references_.add(new TreeSet<Host>());
            }
        }
    }
}
