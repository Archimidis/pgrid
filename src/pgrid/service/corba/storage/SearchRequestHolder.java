package pgrid.service.corba.storage;

/**
* pgrid/service/corba/storage/SearchRequestHolder.java .
* Generated by the IDL-to-Java compiler (portable), version "3.2"
* from resources/demo_pgrid_corba.idl
* Παρασκευή, 5 Οκτώβριος 2012 7:51:04 μμ EEST
*/

public final class SearchRequestHolder implements org.omg.CORBA.portable.Streamable
{
  public pgrid.service.corba.storage.SearchRequest value = null;

  public SearchRequestHolder ()
  {
  }

  public SearchRequestHolder (pgrid.service.corba.storage.SearchRequest initialValue)
  {
    value = initialValue;
  }

  public void _read (org.omg.CORBA.portable.InputStream i)
  {
    value = pgrid.service.corba.storage.SearchRequestHelper.read (i);
  }

  public void _write (org.omg.CORBA.portable.OutputStream o)
  {
    pgrid.service.corba.storage.SearchRequestHelper.write (o, value);
  }

  public org.omg.CORBA.TypeCode _type ()
  {
    return pgrid.service.corba.storage.SearchRequestHelper.type ();
  }

}
