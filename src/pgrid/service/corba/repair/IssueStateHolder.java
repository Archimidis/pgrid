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
 * pgrid/service/spi/corba/repair/IssueStateHolder.java .
 * Generated by the IDL-to-Java compiler (portable), version "3.2"
 * from resources/pgrid_corba.idl
 * Saturday, January 21, 2012 1:52:15 AM EET
 */

public final class IssueStateHolder implements org.omg.CORBA.portable.Streamable {
    public pgrid.service.corba.repair.IssueState value = null;

    public IssueStateHolder() {
    }

    public IssueStateHolder(pgrid.service.corba.repair.IssueState initialValue) {
        value = initialValue;
    }

    public void _read(org.omg.CORBA.portable.InputStream i) {
        value = pgrid.service.corba.repair.IssueStateHelper.read(i);
    }

    public void _write(org.omg.CORBA.portable.OutputStream o) {
        pgrid.service.corba.repair.IssueStateHelper.write(o, value);
    }

    public org.omg.CORBA.TypeCode _type() {
        return pgrid.service.corba.repair.IssueStateHelper.type();
    }

}
