package pgrid.service.spi.corba;


/**
* pgrid/service/spi/corba/PeerReference.java .
* Generated by the IDL-to-Java compiler (portable), version "3.2"
* from resources/pgrid_corba.idl
* Thursday, January 19, 2012 10:19:41 PM EET
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
