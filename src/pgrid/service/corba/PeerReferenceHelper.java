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
 * pgrid/service/corba/PeerReferenceHelper.java .
 * Generated by the IDL-to-Java compiler (portable), version "3.2"
 * from resources/pgrid_corba.idl
 * Tuesday, January 24, 2012 7:07:45 PM EET
 */

abstract public class PeerReferenceHelper {
    private static String _id = "IDL:pgrid/service/corba/PeerReference:1.0";

    public static void insert(org.omg.CORBA.Any a, pgrid.service.corba.PeerReference that) {
        org.omg.CORBA.portable.OutputStream out = a.create_output_stream();
        a.type(type());
        write(out, that);
        a.read_value(out.create_input_stream(), type());
    }

    public static pgrid.service.corba.PeerReference extract(org.omg.CORBA.Any a) {
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
                    org.omg.CORBA.StructMember[] _members0 = new org.omg.CORBA.StructMember[5];
                    org.omg.CORBA.TypeCode _tcOf_members0 = null;
                    _tcOf_members0 = org.omg.CORBA.ORB.init().create_string_tc(0);
                    _members0[0] = new org.omg.CORBA.StructMember(
                            "address",
                            _tcOf_members0,
                            null);
                    _tcOf_members0 = org.omg.CORBA.ORB.init().get_primitive_tc(org.omg.CORBA.TCKind.tk_long);
                    _members0[1] = new org.omg.CORBA.StructMember(
                            "port",
                            _tcOf_members0,
                            null);
                    _tcOf_members0 = org.omg.CORBA.ORB.init().create_string_tc(0);
                    _members0[2] = new org.omg.CORBA.StructMember(
                            "path",
                            _tcOf_members0,
                            null);
                    _tcOf_members0 = org.omg.CORBA.ORB.init().get_primitive_tc(org.omg.CORBA.TCKind.tk_longlong);
                    _members0[3] = new org.omg.CORBA.StructMember(
                            "timestamp",
                            _tcOf_members0,
                            null);
                    _tcOf_members0 = org.omg.CORBA.ORB.init().create_string_tc(0);
                    _members0[4] = new org.omg.CORBA.StructMember(
                            "uuid",
                            _tcOf_members0,
                            null);
                    __typeCode = org.omg.CORBA.ORB.init().create_struct_tc(pgrid.service.corba.PeerReferenceHelper.id(), "PeerReference", _members0);
                    __active = false;
                }
            }
        }
        return __typeCode;
    }

    public static String id() {
        return _id;
    }

    public static pgrid.service.corba.PeerReference read(org.omg.CORBA.portable.InputStream istream) {
        pgrid.service.corba.PeerReference value = new pgrid.service.corba.PeerReference();
        value.address = istream.read_string();
        value.port = istream.read_long();
        value.path = istream.read_string();
        value.timestamp = istream.read_longlong();
        value.uuid = istream.read_string();
        return value;
    }

    public static void write(org.omg.CORBA.portable.OutputStream ostream, pgrid.service.corba.PeerReference value) {
        ostream.write_string(value.address);
        ostream.write_long(value.port);
        ostream.write_string(value.path);
        ostream.write_longlong(value.timestamp);
        ostream.write_string(value.uuid);
    }

}
