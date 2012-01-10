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

package pgrid.service.repair.internal;

import pgrid.service.spi.corba.repair.RepairIssue;

import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

/**
 * A register that will hold all the unsolved issues occurred from failed
 * communications. An issue lives here till it gets repaired obviously.
 *
 * @author Vourlakis Nikolas <nvourlakis@gmail.com>
 */
public class RepairIssueRegistry {
    private static final Map<UUID, RepairIssue> unsolvedIssues_ = new ConcurrentHashMap<UUID, RepairIssue>();
    private static final Map<String, RepairIssue> unsolvedPaths_ = new ConcurrentHashMap<String, RepairIssue>();

    public RepairIssue getIssue(UUID repairID) {
        return unsolvedIssues_.get(repairID);
    }

    public Set<String> getUnsolvedPaths() {
        return unsolvedPaths_.keySet();
    }

    public RepairIssue removeIssue(UUID repairID) {
        RepairIssue issue = unsolvedIssues_.remove(repairID);
        unsolvedPaths_.remove(issue.failedPeerPath);
        return issue;
    }

    public boolean containsID(UUID repairID) {
        return unsolvedIssues_.containsKey(repairID);
    }

    public boolean containsPath(String path) {
        return unsolvedPaths_.containsKey(path);
    }

    public void newIssue(RepairIssue repairIssue) {
        unsolvedIssues_.put(UUID.fromString(repairIssue.repairID), repairIssue);
        unsolvedPaths_.put(repairIssue.failedPeerPath, repairIssue);
    }

    public void clear() {
        unsolvedIssues_.clear();
        unsolvedPaths_.clear();
    }

    public boolean isEmpty() {
        return unsolvedIssues_.isEmpty();
    }
}
