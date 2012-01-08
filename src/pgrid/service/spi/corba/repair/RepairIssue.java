/*
 * This file (pgrid.service.spi.corba.repair.RepairIssue) is part of the libpgrid project.
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

package pgrid.service.spi.corba.repair;


/**
 * pgrid/service/spi/corba/repair/RepairIssue.java .
 * Generated by the IDL-to-Java compiler (portable), version "3.2"
 * from C:/Users/user/Desktop/DIPLOMATIKH_NIKOLA/libpgrid/resources/pgrid_corba.idl
 */

public final class RepairIssue implements org.omg.CORBA.portable.IDLEntity {
    public String repairID = null;
    public long repairTimestamp = (long) 0;
    public long timeoutTimestamp = (long) 0;
    public pgrid.service.spi.corba.PeerReference senderPeer = null;
    public String failedPeerPath = null;
    public pgrid.service.spi.corba.PeerReference failedPeer = null;
    public String footpath = null;

    public RepairIssue() {
    } // ctor

    public RepairIssue(String _repairID, long _repairTimestamp, long _timeoutTimestamp, pgrid.service.spi.corba.PeerReference _senderPeer, String _failedPeerPath, pgrid.service.spi.corba.PeerReference _failedPeer, String _footpath) {
        repairID = _repairID;
        repairTimestamp = _repairTimestamp;
        timeoutTimestamp = _timeoutTimestamp;
        senderPeer = _senderPeer;
        failedPeerPath = _failedPeerPath;
        failedPeer = _failedPeer;
        footpath = _footpath;
    } // ctor

} // class RepairIssue
