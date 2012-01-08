/*
 * This file (pgrid.service.spi.corba.exchange._ExchangeHandleStub) is part of the libpgrid project.
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

package pgrid.service.spi.corba.exchange;


/**
 * pgrid/service/spi/corba/exchange/_ExchangeHandleStub.java .
 * Generated by the IDL-to-Java compiler (portable), version "3.2"
 * from C:/Users/user/Desktop/DIPLOMATIKH_NIKOLA/libpgrid/resources/pgrid_corba.idl
 */

public class _ExchangeHandleStub extends org.omg.CORBA.portable.ObjectImpl implements pgrid.service.spi.corba.exchange.ExchangeHandle {

    public pgrid.service.spi.corba.CorbaRoutingTable routingTable() {
        org.omg.CORBA.portable.InputStream $in = null;
        try {
            org.omg.CORBA.portable.OutputStream $out = _request("_get_routingTable", true);
            $in = _invoke($out);
            pgrid.service.spi.corba.CorbaRoutingTable $result = pgrid.service.spi.corba.CorbaRoutingTableHelper.read($in);
            return $result;
        } catch (org.omg.CORBA.portable.ApplicationException $ex) {
            $in = $ex.getInputStream();
            String _id = $ex.getId();
            throw new org.omg.CORBA.MARSHAL(_id);
        } catch (org.omg.CORBA.portable.RemarshalException $rm) {
            return routingTable();
        } finally {
            _releaseReply($in);
        }
    } // routingTable

    public void exchange(pgrid.service.spi.corba.CorbaRoutingTable routingTable) {
        org.omg.CORBA.portable.InputStream $in = null;
        try {
            org.omg.CORBA.portable.OutputStream $out = _request("exchange", true);
            pgrid.service.spi.corba.CorbaRoutingTableHelper.write($out, routingTable);
            $in = _invoke($out);
            return;
        } catch (org.omg.CORBA.portable.ApplicationException $ex) {
            $in = $ex.getInputStream();
            String _id = $ex.getId();
            throw new org.omg.CORBA.MARSHAL(_id);
        } catch (org.omg.CORBA.portable.RemarshalException $rm) {
            exchange(routingTable);
        } finally {
            _releaseReply($in);
        }
    } // exchange

    // Type-specific CORBA::Object operations
    private static String[] __ids = {
            "IDL:pgrid/service/spi/corba/exchange/ExchangeHandle:1.0"};

    public String[] _ids() {
        return (String[]) __ids.clone();
    }

    private void readObject(java.io.ObjectInputStream s) throws java.io.IOException {
        String str = s.readUTF();
        String[] args = null;
        java.util.Properties props = null;
        org.omg.CORBA.ORB orb = org.omg.CORBA.ORB.init(args, props);
        try {
            org.omg.CORBA.Object obj = orb.string_to_object(str);
            org.omg.CORBA.portable.Delegate delegate = ((org.omg.CORBA.portable.ObjectImpl) obj)._get_delegate();
            _set_delegate(delegate);
        } finally {
            orb.destroy();
        }
    }

    private void writeObject(java.io.ObjectOutputStream s) throws java.io.IOException {
        String[] args = null;
        java.util.Properties props = null;
        org.omg.CORBA.ORB orb = org.omg.CORBA.ORB.init(args, props);
        try {
            String str = orb.object_to_string(this);
            s.writeUTF(str);
        } finally {
            orb.destroy();
        }
    }
} // class _ExchangeHandleStub
