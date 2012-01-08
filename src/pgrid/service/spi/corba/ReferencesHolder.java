/*
 * This file (pgrid.service.spi.corba.ReferencesHolder) is part of the libpgrid project.
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

package pgrid.service.spi.corba;


/**
 * pgrid/service/spi/corba/ReferencesHolder.java .
 * Generated by the IDL-to-Java compiler (portable), version "3.2"
 * from C:/Users/user/Desktop/DIPLOMATIKH_NIKOLA/libpgrid/resources/pgrid_corba.idl
 */

public final class ReferencesHolder implements org.omg.CORBA.portable.Streamable {
    public pgrid.service.spi.corba.PeerReference value[][] = null;

    public ReferencesHolder() {
    }

    public ReferencesHolder(pgrid.service.spi.corba.PeerReference[][] initialValue) {
        value = initialValue;
    }

    public void _read(org.omg.CORBA.portable.InputStream i) {
        value = pgrid.service.spi.corba.ReferencesHelper.read(i);
    }

    public void _write(org.omg.CORBA.portable.OutputStream o) {
        pgrid.service.spi.corba.ReferencesHelper.write(o, value);
    }

    public org.omg.CORBA.TypeCode _type() {
        return pgrid.service.spi.corba.ReferencesHelper.type();
    }

}
