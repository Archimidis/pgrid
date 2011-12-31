/*
 * This file (pgrid.service.spi.corba.ReferencesHelper) is part of the libpgrid project.
 *
 * Copyright (c) 2011. Vourlakis Nikolas. All rights reserved.
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
* pgrid/service/spi/corba/ReferencesHelper.java .
* Generated by the IDL-to-Java compiler (portable), version "3.2"
* from C:/Users/user/Desktop/DIPLOMATIKH_NIKOLA/libpgrid/resources/pgrid_corba.idl
*/

abstract public class ReferencesHelper
{
  private static String  _id = "IDL:pgrid/service/spi/corba/References:1.0";

  public static void insert (org.omg.CORBA.Any a, pgrid.service.spi.corba.PeerReference[][] that)
  {
    org.omg.CORBA.portable.OutputStream out = a.create_output_stream ();
    a.type (type ());
    write (out, that);
    a.read_value (out.create_input_stream (), type ());
  }

  public static pgrid.service.spi.corba.PeerReference[][] extract (org.omg.CORBA.Any a)
  {
    return read (a.create_input_stream ());
  }

  private static org.omg.CORBA.TypeCode __typeCode = null;
  synchronized public static org.omg.CORBA.TypeCode type ()
  {
    if (__typeCode == null)
    {
      __typeCode = pgrid.service.spi.corba.PeerReferenceHelper.type ();
      __typeCode = org.omg.CORBA.ORB.init ().create_sequence_tc (0, __typeCode);
      __typeCode = org.omg.CORBA.ORB.init ().create_alias_tc (pgrid.service.spi.corba.LevelHelper.id (), "Level", __typeCode);
      __typeCode = org.omg.CORBA.ORB.init ().create_sequence_tc (0, __typeCode);
      __typeCode = org.omg.CORBA.ORB.init ().create_alias_tc (pgrid.service.spi.corba.ReferencesHelper.id (), "References", __typeCode);
    }
    return __typeCode;
  }

  public static String id ()
  {
    return _id;
  }

  public static pgrid.service.spi.corba.PeerReference[][] read (org.omg.CORBA.portable.InputStream istream)
  {
    pgrid.service.spi.corba.PeerReference value[][] = null;
    int _len0 = istream.read_long ();
    value = new pgrid.service.spi.corba.PeerReference[_len0][];
    for (int _o1 = 0;_o1 < value.length; ++_o1)
      value[_o1] = pgrid.service.spi.corba.LevelHelper.read (istream);
    return value;
  }

  public static void write (org.omg.CORBA.portable.OutputStream ostream, pgrid.service.spi.corba.PeerReference[][] value)
  {
    ostream.write_long (value.length);
    for (int _i0 = 0;_i0 < value.length; ++_i0)
      pgrid.service.spi.corba.LevelHelper.write (ostream, value[_i0]);
  }

}
