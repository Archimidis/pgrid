package pgrid.service.corba.fileTransfer;

/**
* pgrid/service/corba/fileTransfer/TransferHandleHolder.java .
* Generated by the IDL-to-Java compiler (portable), version "3.2"
* from resources/demo_transfer_corba.idl
* Τρίτη, 9 Οκτώβριος 2012 12:55:40 πμ EEST
*/

public final class TransferHandleHolder implements org.omg.CORBA.portable.Streamable
{
  public pgrid.service.corba.fileTransfer.TransferHandle value = null;

  public TransferHandleHolder ()
  {
  }

  public TransferHandleHolder (pgrid.service.corba.fileTransfer.TransferHandle initialValue)
  {
    value = initialValue;
  }

  public void _read (org.omg.CORBA.portable.InputStream i)
  {
    value = pgrid.service.corba.fileTransfer.TransferHandleHelper.read (i);
  }

  public void _write (org.omg.CORBA.portable.OutputStream o)
  {
    pgrid.service.corba.fileTransfer.TransferHandleHelper.write (o, value);
  }

  public org.omg.CORBA.TypeCode _type ()
  {
    return pgrid.service.corba.fileTransfer.TransferHandleHelper.type ();
  }

}
