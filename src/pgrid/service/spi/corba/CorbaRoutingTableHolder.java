package pgrid.service.spi.corba;

/**
* pgrid/service/spi/corba/CorbaRoutingTableHolder.java .
* Generated by the IDL-to-Java compiler (portable), version "3.2"
* from resources/pgrid_corba.idl
* Friday, January 13, 2012 9:51:40 PM EET
*/

public final class CorbaRoutingTableHolder implements org.omg.CORBA.portable.Streamable
{
  public pgrid.service.spi.corba.CorbaRoutingTable value = null;

  public CorbaRoutingTableHolder ()
  {
  }

  public CorbaRoutingTableHolder (pgrid.service.spi.corba.CorbaRoutingTable initialValue)
  {
    value = initialValue;
  }

  public void _read (org.omg.CORBA.portable.InputStream i)
  {
    value = pgrid.service.spi.corba.CorbaRoutingTableHelper.read (i);
  }

  public void _write (org.omg.CORBA.portable.OutputStream o)
  {
    pgrid.service.spi.corba.CorbaRoutingTableHelper.write (o, value);
  }

  public org.omg.CORBA.TypeCode _type ()
  {
    return pgrid.service.spi.corba.CorbaRoutingTableHelper.type ();
  }

}
