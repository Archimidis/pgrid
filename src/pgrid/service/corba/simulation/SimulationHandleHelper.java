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

package pgrid.service.corba.simulation;


/**
 * pgrid/service/corba/simulation/SimulationHandleHelper.java .
 * Generated by the IDL-to-Java compiler (portable), version "3.2"
 * from resources/pgrid_corba.idl
 * Tuesday, January 24, 2012 7:07:45 PM EET
 */

abstract public class SimulationHandleHelper {
    private static String _id = "IDL:pgrid/service/corba/simulation/SimulationHandle:1.0";

    public static void insert(org.omg.CORBA.Any a, pgrid.service.corba.simulation.SimulationHandle that) {
        org.omg.CORBA.portable.OutputStream out = a.create_output_stream();
        a.type(type());
        write(out, that);
        a.read_value(out.create_input_stream(), type());
    }

    public static pgrid.service.corba.simulation.SimulationHandle extract(org.omg.CORBA.Any a) {
        return read(a.create_input_stream());
    }

    private static org.omg.CORBA.TypeCode __typeCode = null;

    synchronized public static org.omg.CORBA.TypeCode type() {
        if (__typeCode == null) {
            __typeCode = org.omg.CORBA.ORB.init().create_interface_tc(pgrid.service.corba.simulation.SimulationHandleHelper.id(), "SimulationHandle");
        }
        return __typeCode;
    }

    public static String id() {
        return _id;
    }

    public static pgrid.service.corba.simulation.SimulationHandle read(org.omg.CORBA.portable.InputStream istream) {
        return narrow(istream.read_Object(_SimulationHandleStub.class));
    }

    public static void write(org.omg.CORBA.portable.OutputStream ostream, pgrid.service.corba.simulation.SimulationHandle value) {
        ostream.write_Object((org.omg.CORBA.Object) value);
    }

    public static pgrid.service.corba.simulation.SimulationHandle narrow(org.omg.CORBA.Object obj) {
        if (obj == null)
            return null;
        else if (obj instanceof pgrid.service.corba.simulation.SimulationHandle)
            return (pgrid.service.corba.simulation.SimulationHandle) obj;
        else if (!obj._is_a(id()))
            throw new org.omg.CORBA.BAD_PARAM();
        else {
            org.omg.CORBA.portable.Delegate delegate = ((org.omg.CORBA.portable.ObjectImpl) obj)._get_delegate();
            pgrid.service.corba.simulation._SimulationHandleStub stub = new pgrid.service.corba.simulation._SimulationHandleStub();
            stub._set_delegate(delegate);
            return stub;
        }
    }

    public static pgrid.service.corba.simulation.SimulationHandle unchecked_narrow(org.omg.CORBA.Object obj) {
        if (obj == null)
            return null;
        else if (obj instanceof pgrid.service.corba.simulation.SimulationHandle)
            return (pgrid.service.corba.simulation.SimulationHandle) obj;
        else {
            org.omg.CORBA.portable.Delegate delegate = ((org.omg.CORBA.portable.ObjectImpl) obj)._get_delegate();
            pgrid.service.corba.simulation._SimulationHandleStub stub = new pgrid.service.corba.simulation._SimulationHandleStub();
            stub._set_delegate(delegate);
            return stub;
        }
    }

}
