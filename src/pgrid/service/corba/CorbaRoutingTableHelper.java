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

package pgrid.service.corba;


/**
 * pgrid/service/spi/corba/CorbaRoutingTableHelper.java .
 * Generated by the IDL-to-Java compiler (portable), version "3.2"
 * from resources/pgrid_corba.idl
 * Saturday, January 21, 2012 1:52:15 AM EET
 */

abstract public class CorbaRoutingTableHelper {
    private static String _id = "IDL:pgrid/service/spi/corba/CorbaRoutingTable:1.0";

    public static void insert(org.omg.CORBA.Any a, CorbaRoutingTable that) {
        org.omg.CORBA.portable.OutputStream out = a.create_output_stream();
        a.type(type());
        write(out, that);
        a.read_value(out.create_input_stream(), type());
    }

    public static CorbaRoutingTable extract(org.omg.CORBA.Any a) {
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
                    org.omg.CORBA.StructMember[] _members0 = new org.omg.CORBA.StructMember[2];
                    org.omg.CORBA.TypeCode _tcOf_members0 = null;
                    _tcOf_members0 = PeerReferenceHelper.type();
                    _tcOf_members0 = org.omg.CORBA.ORB.init().create_sequence_tc(0, _tcOf_members0);
                    _tcOf_members0 = org.omg.CORBA.ORB.init().create_alias_tc(PeerArrayHelper.id(), "PeerArray", _tcOf_members0);
                    _tcOf_members0 = org.omg.CORBA.ORB.init().create_sequence_tc(0, _tcOf_members0);
                    _tcOf_members0 = org.omg.CORBA.ORB.init().create_alias_tc(ReferencesHelper.id(), "References", _tcOf_members0);
                    _members0[0] = new org.omg.CORBA.StructMember(
                            "refs",
                            _tcOf_members0,
                            null);
                    _tcOf_members0 = PeerReferenceHelper.type();
                    _members0[1] = new org.omg.CORBA.StructMember(
                            "peer",
                            _tcOf_members0,
                            null);
                    __typeCode = org.omg.CORBA.ORB.init().create_struct_tc(CorbaRoutingTableHelper.id(), "CorbaRoutingTable", _members0);
                    __active = false;
                }
            }
        }
        return __typeCode;
    }

    public static String id() {
        return _id;
    }

    public static CorbaRoutingTable read(org.omg.CORBA.portable.InputStream istream) {
        CorbaRoutingTable value = new CorbaRoutingTable();
        value.refs = ReferencesHelper.read(istream);
        value.peer = PeerReferenceHelper.read(istream);
        return value;
    }

    public static void write(org.omg.CORBA.portable.OutputStream ostream, CorbaRoutingTable value) {
        ReferencesHelper.write(ostream, value.refs);
        PeerReferenceHelper.write(ostream, value.peer);
    }

}
