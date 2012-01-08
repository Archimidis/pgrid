/*
 * This file (pgrid.service.spi.corba.repair.RepairSolutionHelper) is part of the libpgrid project.
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
 * pgrid/service/spi/corba/repair/RepairSolutionHelper.java .
 * Generated by the IDL-to-Java compiler (portable), version "3.2"
 * from C:/Users/user/Desktop/DIPLOMATIKH_NIKOLA/libpgrid/resources/pgrid_corba.idl
 */

abstract public class RepairSolutionHelper {
    private static String _id = "IDL:pgrid/service/spi/corba/repair/RepairSolution:1.0";

    public static void insert(org.omg.CORBA.Any a, pgrid.service.spi.corba.repair.RepairSolution that) {
        org.omg.CORBA.portable.OutputStream out = a.create_output_stream();
        a.type(type());
        write(out, that);
        a.read_value(out.create_input_stream(), type());
    }

    public static pgrid.service.spi.corba.repair.RepairSolution extract(org.omg.CORBA.Any a) {
        return read(a.create_input_stream());
    }

    private static org.omg.CORBA.TypeCode __typeCode = null;
    private static boolean __active = false;

    synchronized public static org.omg.CORBA.TypeCode type() {
        if (__typeCode == null) {
            synchronized (org.omg.CORBA.TypeCode.class) {
                if (__typeCode == null) {
                    if (__active) {
                        return org.omg.CORBA.ORB.init().create_recursive_tc(_id);
                    }
                    __active = true;
                    org.omg.CORBA.StructMember[] _members0 = new org.omg.CORBA.StructMember[6];
                    org.omg.CORBA.TypeCode _tcOf_members0 = null;
                    _tcOf_members0 = org.omg.CORBA.ORB.init().create_string_tc(0);
                    _members0[0] = new org.omg.CORBA.StructMember(
                            "repairID",
                            _tcOf_members0,
                            null);
                    _tcOf_members0 = org.omg.CORBA.ORB.init().get_primitive_tc(org.omg.CORBA.TCKind.tk_longlong);
                    _members0[1] = new org.omg.CORBA.StructMember(
                            "repairTimestamp",
                            _tcOf_members0,
                            null);
                    _tcOf_members0 = org.omg.CORBA.ORB.init().create_string_tc(0);
                    _members0[2] = new org.omg.CORBA.StructMember(
                            "hotPath",
                            _tcOf_members0,
                            null);
                    _tcOf_members0 = pgrid.service.spi.corba.PeerReferenceHelper.type();
                    _tcOf_members0 = org.omg.CORBA.ORB.init().create_sequence_tc(0, _tcOf_members0);
                    _members0[3] = new org.omg.CORBA.StructMember(
                            "failedPeer",
                            _tcOf_members0,
                            null);
                    _tcOf_members0 = pgrid.service.spi.corba.PeerReferenceHelper.type();
                    _members0[4] = new org.omg.CORBA.StructMember(
                            "replacerPeer",
                            _tcOf_members0,
                            null);
                    _tcOf_members0 = pgrid.service.spi.corba.PeerReferenceHelper.type();
                    _members0[5] = new org.omg.CORBA.StructMember(
                            "twinPeer",
                            _tcOf_members0,
                            null);
                    __typeCode = org.omg.CORBA.ORB.init().create_struct_tc(pgrid.service.spi.corba.repair.RepairSolutionHelper.id(), "RepairSolution", _members0);
                    __active = false;
                }
            }
        }
        return __typeCode;
    }

    public static String id() {
        return _id;
    }

    public static pgrid.service.spi.corba.repair.RepairSolution read(org.omg.CORBA.portable.InputStream istream) {
        pgrid.service.spi.corba.repair.RepairSolution value = new pgrid.service.spi.corba.repair.RepairSolution();
        value.repairID = istream.read_string();
        value.repairTimestamp = istream.read_longlong();
        value.hotPath = istream.read_string();
        int _len0 = istream.read_long();
        value.failedPeer = new pgrid.service.spi.corba.PeerReference[_len0];
        for (int _o1 = 0; _o1 < value.failedPeer.length; ++_o1)
            value.failedPeer[_o1] = pgrid.service.spi.corba.PeerReferenceHelper.read(istream);
        value.replacerPeer = pgrid.service.spi.corba.PeerReferenceHelper.read(istream);
        value.twinPeer = pgrid.service.spi.corba.PeerReferenceHelper.read(istream);
        return value;
    }

    public static void write(org.omg.CORBA.portable.OutputStream ostream, pgrid.service.spi.corba.repair.RepairSolution value) {
        ostream.write_string(value.repairID);
        ostream.write_longlong(value.repairTimestamp);
        ostream.write_string(value.hotPath);
        ostream.write_long(value.failedPeer.length);
        for (int _i0 = 0; _i0 < value.failedPeer.length; ++_i0)
            pgrid.service.spi.corba.PeerReferenceHelper.write(ostream, value.failedPeer[_i0]);
        pgrid.service.spi.corba.PeerReferenceHelper.write(ostream, value.replacerPeer);
        pgrid.service.spi.corba.PeerReferenceHelper.write(ostream, value.twinPeer);
    }

}
