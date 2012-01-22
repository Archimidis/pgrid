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

package pgrid.service.corba.repair;


/**
 * pgrid/service/spi/corba/repair/IssueTypeHelper.java .
 * Generated by the IDL-to-Java compiler (portable), version "3.2"
 * from resources/pgrid_corba.idl
 * Thursday, January 19, 2012 7:42:09 PM EET
 */

abstract public class IssueTypeHelper {
    private static String _id = "IDL:pgrid/service/spi/corba/repair/IssueType:1.0";

    public static void insert(org.omg.CORBA.Any a, pgrid.service.corba.repair.IssueType that) {
        org.omg.CORBA.portable.OutputStream out = a.create_output_stream();
        a.type(type());
        write(out, that);
        a.read_value(out.create_input_stream(), type());
    }

    public static pgrid.service.corba.repair.IssueType extract(org.omg.CORBA.Any a) {
        return read(a.create_input_stream());
    }

    private static org.omg.CORBA.TypeCode __typeCode = null;

    synchronized public static org.omg.CORBA.TypeCode type() {
        if (__typeCode == null) {
            __typeCode = org.omg.CORBA.ORB.init().create_enum_tc(pgrid.service.corba.repair.IssueTypeHelper.id(), "IssueType", new String[]{"SINGLE_NODE", "SUBTREE"});
        }
        return __typeCode;
    }

    public static String id() {
        return _id;
    }

    public static pgrid.service.corba.repair.IssueType read(org.omg.CORBA.portable.InputStream istream) {
        return pgrid.service.corba.repair.IssueType.from_int(istream.read_long());
    }

    public static void write(org.omg.CORBA.portable.OutputStream ostream, pgrid.service.corba.repair.IssueType value) {
        ostream.write_long(value.value());
    }

}
