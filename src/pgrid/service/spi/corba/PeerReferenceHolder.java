package pgrid.service.spi.corba;

/**
* pgrid/service/spi/corba/PeerReferenceHolder.java .
* Generated by the IDL-to-Java compiler (portable), version "3.2"
* from resources/pgrid_corba.idl
* Thursday, January 19, 2012 10:19:41 PM EET
*/

public final class PeerReferenceHolder implements org.omg.CORBA.portable.Streamable
{
  public pgrid.service.spi.corba.PeerReference value = null;

  public PeerReferenceHolder ()
  {
  }

  public PeerReferenceHolder (pgrid.service.spi.corba.PeerReference initialValue)
  {
    value = initialValue;
  }

  public void _read (org.omg.CORBA.portable.InputStream i)
  {
    value = pgrid.service.spi.corba.PeerReferenceHelper.read (i);
  }

  public void _write (org.omg.CORBA.portable.OutputStream o)
  {
    pgrid.service.spi.corba.PeerReferenceHelper.write (o, value);
  }

  public org.omg.CORBA.TypeCode _type ()
  {
    return pgrid.service.spi.corba.PeerReferenceHelper.type ();
  }

}
