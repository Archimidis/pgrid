package pgrid.service.spi.corba;


/**
* pgrid/service/spi/corba/ReferencesHolder.java .
* Generated by the IDL-to-Java compiler (portable), version "3.2"
* from C:/Users/user/Desktop/DIPLOMATIKH_NIKOLA/libpgrid/resources/pgrid_corba.idl
*/

public final class ReferencesHolder implements org.omg.CORBA.portable.Streamable
{
  public pgrid.service.spi.corba.PeerReference value[][] = null;

  public ReferencesHolder ()
  {
  }

  public ReferencesHolder (pgrid.service.spi.corba.PeerReference[][] initialValue)
  {
    value = initialValue;
  }

  public void _read (org.omg.CORBA.portable.InputStream i)
  {
    value = pgrid.service.spi.corba.ReferencesHelper.read (i);
  }

  public void _write (org.omg.CORBA.portable.OutputStream o)
  {
    pgrid.service.spi.corba.ReferencesHelper.write (o, value);
  }

  public org.omg.CORBA.TypeCode _type ()
  {
    return pgrid.service.spi.corba.ReferencesHelper.type ();
  }

}
