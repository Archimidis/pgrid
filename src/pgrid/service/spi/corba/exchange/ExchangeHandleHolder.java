package pgrid.service.spi.corba.exchange;

/**
* pgrid/service/spi/corba/exchange/ExchangeHandleHolder.java .
* Generated by the IDL-to-Java compiler (portable), version "3.2"
* from resources/pgrid_corba.idl
* Thursday, January 19, 2012 9:18:13 PM EET
*/

public final class ExchangeHandleHolder implements org.omg.CORBA.portable.Streamable
{
  public pgrid.service.spi.corba.exchange.ExchangeHandle value = null;

  public ExchangeHandleHolder ()
  {
  }

  public ExchangeHandleHolder (pgrid.service.spi.corba.exchange.ExchangeHandle initialValue)
  {
    value = initialValue;
  }

  public void _read (org.omg.CORBA.portable.InputStream i)
  {
    value = pgrid.service.spi.corba.exchange.ExchangeHandleHelper.read (i);
  }

  public void _write (org.omg.CORBA.portable.OutputStream o)
  {
    pgrid.service.spi.corba.exchange.ExchangeHandleHelper.write (o, value);
  }

  public org.omg.CORBA.TypeCode _type ()
  {
    return pgrid.service.spi.corba.exchange.ExchangeHandleHelper.type ();
  }

}
