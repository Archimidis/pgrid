/*
 * This file (pgrid.service.spi.corba.PeerReference) is part of the libpgrid project.
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
* pgrid/service/spi/corba/PeerReference.java .
* Generated by the IDL-to-Java compiler (portable), version "3.2"
* from C:/Users/user/Desktop/DIPLOMATIKH_NIKOLA/libpgrid/resources/pgrid_corba.idl
*/

public final class PeerReference implements org.omg.CORBA.portable.IDLEntity
{
  public String address = null;
  public int port = (int)0;
  public String path = null;
  public long timestamp = (long)0;
  public String uuid = null;

  public PeerReference ()
  {
  } // ctor

  public PeerReference (String _address, int _port, String _path, long _timestamp, String _uuid)
  {
    address = _address;
    port = _port;
    path = _path;
    timestamp = _timestamp;
    uuid = _uuid;
  } // ctor

} // class PeerReference
