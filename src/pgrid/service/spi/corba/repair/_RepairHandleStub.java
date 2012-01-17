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

package pgrid.service.spi.corba.repair;


/**
 * pgrid/service/spi/corba/repair/_RepairHandleStub.java .
 * Generated by the IDL-to-Java compiler (portable), version "3.2"
 * from resources/pgrid_corba.idl
 * Friday, January 13, 2012 9:51:40 PM EET
 */

public class _RepairHandleStub extends org.omg.CORBA.portable.ObjectImpl implements pgrid.service.spi.corba.repair.RepairHandle {

    public void fixIssue(pgrid.service.spi.corba.repair.RepairIssue issue, String footpath) {
        org.omg.CORBA.portable.InputStream $in = null;
        try {
            org.omg.CORBA.portable.OutputStream $out = _request("fixIssue", true);
            pgrid.service.spi.corba.repair.RepairIssueHelper.write($out, issue);
            $out.write_string(footpath);
            $in = _invoke($out);
            return;
        } catch (org.omg.CORBA.portable.ApplicationException $ex) {
            $in = $ex.getInputStream();
            String _id = $ex.getId();
            throw new org.omg.CORBA.MARSHAL(_id);
        } catch (org.omg.CORBA.portable.RemarshalException $rm) {
            fixIssue(issue, footpath);
        } finally {
            _releaseReply($in);
        }
    } // fixIssue

    public void broadcastSolution(pgrid.service.spi.corba.repair.RepairSolution solution) {
        org.omg.CORBA.portable.InputStream $in = null;
        try {
            org.omg.CORBA.portable.OutputStream $out = _request("broadcastSolution", true);
            pgrid.service.spi.corba.repair.RepairSolutionHelper.write($out, solution);
            $in = _invoke($out);
            return;
        } catch (org.omg.CORBA.portable.ApplicationException $ex) {
            $in = $ex.getInputStream();
            String _id = $ex.getId();
            throw new org.omg.CORBA.MARSHAL(_id);
        } catch (org.omg.CORBA.portable.RemarshalException $rm) {
            broadcastSolution(solution);
        } finally {
            _releaseReply($in);
        }
    } // broadcastSolution

    // Type-specific CORBA::Object operations
    private static String[] __ids = {
            "IDL:pgrid/service/spi/corba/repair/RepairHandle:1.0"};

    public String[] _ids() {
        return (String[]) __ids.clone();
    }

    private void readObject(java.io.ObjectInputStream s) throws java.io.IOException {
        String str = s.readUTF();
        String[] args = null;
        java.util.Properties props = null;
        org.omg.CORBA.Object obj = org.omg.CORBA.ORB.init(args, props).string_to_object(str);
        org.omg.CORBA.portable.Delegate delegate = ((org.omg.CORBA.portable.ObjectImpl) obj)._get_delegate();
        _set_delegate(delegate);
    }

    private void writeObject(java.io.ObjectOutputStream s) throws java.io.IOException {
        String[] args = null;
        java.util.Properties props = null;
        String str = org.omg.CORBA.ORB.init(args, props).object_to_string(this);
        s.writeUTF(str);
    }
} // class _RepairHandleStub
