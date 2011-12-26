/*
 * This file (entity.routingtable.RT) is part of the libpgrid project.
 *
 * Copyright (c) 2011. Vourlakis Nikolas. All rights reserved.
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

package entity.routingtable;

import entity.PGridHost;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;


/**
 * This class represents the routing table of the peer and stores pairs
 * containing a {@link entity.PGridHost} associated with a level. The levels
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
    // FIXME: On path reduce of local peer the exceeding levels should be removed.
    private PGridHost localhost_;

    private final List<Set<PGridHost>> references_ =
            new Vector<Set<PGridHost>>();
    private final Map<UUID, PGridHost> uuidRefs_ = new ConcurrentHashMap<UUID, PGridHost>();

    /**
     * Constructor.
     */
    public RoutingTable() {
    }

    public void setLocalhost(PGridHost localhost) {
        if (localhost == null) {
            throw new NullPointerException();
        }
        localhost_ = localhost;
        createMissingLevels(localhost_.getHostPath().length() - 1);
    }

    public PGridHost getLocalhost() {
        return localhost_;
    }

    public void update(int level, RoutingTable routingTable) {
        // TODO: Implement update and Unit Test
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
    public void addReference(int level, PGridHost host) {
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
    public void addReference(int level, Collection<PGridHost> hosts) {
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
        for (PGridHost host : hosts) {
            removeReference(host);
        }

        references_.get(level).addAll(union(references_.get(level), hosts));

        for (PGridHost host : hosts) {
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
    public void updateLevel(int level, Collection<PGridHost> hosts) {
        if (hosts == null) {
            return;
        }

        if (level < 0) {
            throw new IllegalArgumentException("Negative level given");
        }
        if (level >= localhost_.getHostPath().length()) {
            throw new IllegalArgumentException("Level surpasses localhost path length");
        }

        for (PGridHost host : hosts) {
            removeReference(host);
        }

        Collection<PGridHost> result = union(references_.get(level), hosts);
        references_.get(level).clear();
        references_.get(level).addAll(result);
        for (PGridHost host : hosts) {
            uuidRefs_.put(host.getUUID(), host);
        }
    }

    /**
     * If the given host exists in the routing table, it will update the
     * information stored about him. If the host is not contained then nothing
     * will happen.
     *
     * @param host the host to update.
     */
    public void updateReference(PGridHost host) {
        if (host == null) {
            throw new NullPointerException();
        }

        if (uuidRefs_.containsKey(host.getUUID())) {
            for (Set<PGridHost> treeSet : references_) {
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
     * @param routingTable to be unioned with this.
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

        Collection<PGridHost> other = routingTable.getLevel(level);

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
    public Collection<PGridHost> getLevel(int level) {
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
    public Collection<Collection<PGridHost>> getAllHostsByLevels() {
        Collection<Collection<PGridHost>> result = new ArrayList<Collection<PGridHost>>();

        if (references_.size() == 0) {
            return result;
        }

        for (Set<PGridHost> treeSet : references_) {
            result.add(new ArrayList<PGridHost>(treeSet));
        }

        return result;
    }

    /**
     * It will return all the hosts that this routing table contains.
     *
     * @return a collections with the hosts.
     */
    public Collection<PGridHost> getAllHosts() {
        List<PGridHost> result = new ArrayList<PGridHost>(uuidRefs_.size());
        Collections.copy(result, (List<PGridHost>) uuidRefs_.values());
        return result;
    }

    /**
     * Removes the given host if it exists from the routing table.
     *
     * @param host to be removed.
     */
    public void removeReference(PGridHost host) {
        if (host == null) {
            throw new NullPointerException();
        }

        if (!uuidRefs_.containsKey(host.getUUID())) {
            return;
        }

        for (Set<PGridHost> treeSet : references_) {
            treeSet.remove(host);
            uuidRefs_.remove(host.getUUID());
        }
    }

    /**
     * Checks if the routing table contains the given host.
     *
     * @param host to be checked for existence.
     * @return true if it exists, false else.
     */
    public boolean contains(PGridHost host) {
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
    public void clear() {
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
    public PGridHost selectUUIDHost(UUID uuid) {
        return uuidRefs_.get(uuid);
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
    public static Collection<PGridHost> union(Collection<PGridHost> refs1, Collection<PGridHost> refs2) {
        if (refs1 == null || refs2 == null) {
            throw new NullPointerException();
        }
        Collection<PGridHost> result = new TreeSet<PGridHost>(refs1);
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
    public static Collection<PGridHost> randomSelect(int refMax, Collection<PGridHost> commonRefs) {
        if (commonRefs == null) {
            throw new NullPointerException();
        }
        if (refMax < 0) {
            throw new IllegalArgumentException("Negative refMax given");
        }

        int choose = (refMax <= commonRefs.size()) ? refMax : commonRefs.size();

        List<PGridHost> copy = new Vector<PGridHost>(commonRefs);
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
    private void createMissingLevels(int level) {
        // level should be valid cause it should be checked from the public method
        if (level >= references_.size() && level < localhost_.getHostPath().length()) {
            int end = Math.max(level, references_.size());
            int start = Math.min(level, references_.size());

            for (int i = start; i <= end; i++) {
                references_.add(new TreeSet<PGridHost>());
            }
        }
    }
}
