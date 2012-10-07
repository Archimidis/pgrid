package pgrid.service.corba.storage;


/**
* pgrid/service/corba/storage/StorageHandleHelper.java .
* Generated by the IDL-to-Java compiler (portable), version "3.2"
* from resources/demo_pgrid_corba.idl
* Παρασκευή, 5 Οκτώβριος 2012 7:51:04 μμ EEST
*/

abstract public class StorageHandleHelper
{
  private static String  _id = "IDL:pgrid/service/corba/storage/StorageHandle:1.0";

  public static void insert (org.omg.CORBA.Any a, pgrid.service.corba.storage.StorageHandle that)
  {
    org.omg.CORBA.portable.OutputStream out = a.create_output_stream ();
    a.type (type ());
    write (out, that);
    a.read_value (out.create_input_stream (), type ());
  }

  public static pgrid.service.corba.storage.StorageHandle extract (org.omg.CORBA.Any a)
  {
    return read (a.create_input_stream ());
  }

  private static org.omg.CORBA.TypeCode __typeCode = null;
  synchronized public static org.omg.CORBA.TypeCode type ()
  {
    if (__typeCode == null)
    {
      __typeCode = org.omg.CORBA.ORB.init ().create_interface_tc (pgrid.service.corba.storage.StorageHandleHelper.id (), "StorageHandle");
    }
    return __typeCode;
  }

  public static String id ()
  {
    return _id;
  }

  public static pgrid.service.corba.storage.StorageHandle read (org.omg.CORBA.portable.InputStream istream)
  {
    return narrow (istream.read_Object (_StorageHandleStub.class));
  }

  public static void write (org.omg.CORBA.portable.OutputStream ostream, pgrid.service.corba.storage.StorageHandle value)
  {
    ostream.write_Object ((org.omg.CORBA.Object) value);
  }

  public static pgrid.service.corba.storage.StorageHandle narrow (org.omg.CORBA.Object obj)
  {
    if (obj == null)
      return null;
    else if (obj instanceof pgrid.service.corba.storage.StorageHandle)
      return (pgrid.service.corba.storage.StorageHandle)obj;
    else if (!obj._is_a (id ()))
      throw new org.omg.CORBA.BAD_PARAM ();
    else
    {
      org.omg.CORBA.portable.Delegate delegate = ((org.omg.CORBA.portable.ObjectImpl)obj)._get_delegate ();
      pgrid.service.corba.storage._StorageHandleStub stub = new pgrid.service.corba.storage._StorageHandleStub ();
      stub._set_delegate(delegate);
      return stub;
    }
  }

  public static pgrid.service.corba.storage.StorageHandle unchecked_narrow (org.omg.CORBA.Object obj)
  {
    if (obj == null)
      return null;
    else if (obj instanceof pgrid.service.corba.storage.StorageHandle)
      return (pgrid.service.corba.storage.StorageHandle)obj;
    else
    {
      org.omg.CORBA.portable.Delegate delegate = ((org.omg.CORBA.portable.ObjectImpl)obj)._get_delegate ();
      pgrid.service.corba.storage._StorageHandleStub stub = new pgrid.service.corba.storage._StorageHandleStub ();
      stub._set_delegate(delegate);
      return stub;
    }
  }

}
