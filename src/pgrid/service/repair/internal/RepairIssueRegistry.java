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

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import pgrid.entity.Host;
import pgrid.entity.PGridPath;
import pgrid.service.spi.corba.repair.IssueType;
import pgrid.service.spi.corba.repair.RepairIssue;
import pgrid.service.utilities.Deserializer;
import pgrid.service.utilities.Serializer;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * A register that will hold all the unsolved issues occurred from failed
 * communications. An issue lives here till it gets repaired obviously.
 *
 * @author Vourlakis Nikolas <nvourlakis@gmail.com>
 */
public class RepairIssueRegistry {
    private static final Logger logger_ = LoggerFactory.getLogger(RepairIssueRegistry.class);

    private static final Map<UUID, RepairIssue> unsolvedIssues_ = new ConcurrentHashMap<UUID, RepairIssue>();
    private static final Map<String, RepairIssue> unsolvedPaths_ = new ConcurrentHashMap<String, RepairIssue>();

    public RepairIssue getIssue(String path) {
        return unsolvedPaths_.get(path);
    }

    public Set<String> getUnsolvedPaths() {
        return unsolvedPaths_.keySet();
    }

    public RepairIssue removeIssue(UUID hostUUID) {
        RepairIssue issue = unsolvedIssues_.remove(hostUUID);
        unsolvedPaths_.remove(issue.failedPath);
        return issue;
    }

    public boolean containsHost(UUID hostUUID) {
        return unsolvedIssues_.containsKey(hostUUID);
    }

    public boolean containsPath(String path) {
        return unsolvedPaths_.containsKey(path);
    }

    public void newIssue(RepairIssue repairIssue) {
        unsolvedIssues_.put(UUID.fromString(repairIssue.failedPeer.uuid), repairIssue);
        unsolvedPaths_.put(repairIssue.failedPath, repairIssue);
    }

    public void newIssue(Host failedHost) {
        RepairIssue repairIssue = new RepairIssue(IssueType.SINGLE_NODE,
                Serializer.serializeHost(failedHost),
                failedHost.getHostPath().toString());
        unsolvedIssues_.put(UUID.fromString(repairIssue.failedPeer.uuid), repairIssue);
        unsolvedPaths_.put(repairIssue.failedPath, repairIssue);
    }

    public void clear() {
        unsolvedIssues_.clear();
        unsolvedPaths_.clear();
    }

    public boolean isEmpty() {
        return unsolvedIssues_.isEmpty();
    }

    public void newSubtreeFailure(String subtreePath) {
        PGridPath subtree = new PGridPath(subtreePath);
        for (String storedPath : unsolvedPaths_.keySet()) {
            PGridPath stored = new PGridPath(storedPath);
            if (stored.hasPrefix(subtree)) {
                unsolvedPaths_.get(storedPath).issueType = IssueType.SUBTREE;
            }
        }
    }

    public int commonPrefixedPathNumber(String prefix) {
        int count = 0;
        PGridPath prefixPath = new PGridPath(prefix);
        for (String path : unsolvedPaths_.keySet()) {
            PGridPath otherPath = new PGridPath(path);
            if (otherPath.hasPrefix(prefixPath)) {
                ++count;
            }
        }
        return count;
    }

    public List<Host> commonPrefixIssues(String prefix) {
        List<Host> list = new ArrayList<Host>();
        PGridPath prefixPath = new PGridPath(prefix);
        for (String path : unsolvedPaths_.keySet()) {
            PGridPath otherPath = new PGridPath(path);
            if (otherPath.hasPrefix(prefixPath)) {
                list.add(Deserializer.deserializeHost(unsolvedPaths_.get(path).failedPeer));
            }
        }
        return list;
    }

    public boolean isCompleteSubtree(PGridPath prefix) {
        int prefixCount = 0;
        int conjCount = 0;

        for (String unsolved : unsolvedPaths_.keySet()) {
            PGridPath unsolvedPath = new PGridPath(unsolved);
            if (unsolvedPath.hasPrefix(prefix)) {
                prefixCount++;
                if (unsolvedPath.length() - prefix.length() == 1) {
                    conjCount++;
                }
            }
        }
        if (conjCount == 2) {
            return true;
        } else if(prefixCount == 1 && conjCount == 0) {
            char last = prefix.value(prefix.length() - 1);
            last = last == '0' ? '1' : '0';
            PGridPath conjPath = new PGridPath(prefix.subPath(0, prefix.length() - 1) + last);
            boolean result = isCompleteSubtree(conjPath);
            return result;
        } else if (prefixCount != conjCount) {
            PGridPath zero = new PGridPath(prefix.toString() + '0');
            PGridPath one = new PGridPath(prefix.toString() + '1');
            boolean zeroResult = isCompleteSubtree(zero);
            boolean oneResult = isCompleteSubtree(one);
            return zeroResult && oneResult;
        }
        return false;
    }

}
