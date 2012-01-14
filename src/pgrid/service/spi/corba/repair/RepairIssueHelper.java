package pgrid.service.spi.corba.repair;


/**
* pgrid/service/spi/corba/repair/RepairIssueHelper.java .
* Generated by the IDL-to-Java compiler (portable), version "3.2"
* from resources/pgrid_corba.idl
* Friday, January 13, 2012 9:51:40 PM EET
*/

abstract public class RepairIssueHelper
{
  private static String  _id = "IDL:pgrid/service/spi/corba/repair/RepairIssue:1.0";

  public static void insert (org.omg.CORBA.Any a, pgrid.service.spi.corba.repair.RepairIssue that)
  {
    org.omg.CORBA.portable.OutputStream out = a.create_output_stream ();
    a.type (type ());
    write (out, that);
    a.read_value (out.create_input_stream (), type ());
  }

  public static pgrid.service.spi.corba.repair.RepairIssue extract (org.omg.CORBA.Any a)
  {
    return read (a.create_input_stream ());
  }

  private static org.omg.CORBA.TypeCode __typeCode = null;
  private static boolean __active = false;
  synchronized public static org.omg.CORBA.TypeCode type ()
  {
    if (__typeCode == null)
    {
      synchronized (org.omg.CORBA.TypeCode.class)
      {
        if (__typeCode == null)
        {
          if (__active)
          {
            return org.omg.CORBA.ORB.init().create_recursive_tc ( _id );
          }
          __active = true;
          org.omg.CORBA.StructMember[] _members0 = new org.omg.CORBA.StructMember [3];
          org.omg.CORBA.TypeCode _tcOf_members0 = null;
          _tcOf_members0 = pgrid.service.spi.corba.repair.IssueTypeHelper.type ();
          _members0[0] = new org.omg.CORBA.StructMember (
            "issueType",
            _tcOf_members0,
            null);
          _tcOf_members0 = pgrid.service.spi.corba.PeerReferenceHelper.type ();
          _members0[1] = new org.omg.CORBA.StructMember (
            "failedPeer",
            _tcOf_members0,
            null);
          _tcOf_members0 = org.omg.CORBA.ORB.init ().create_string_tc (0);
          _members0[2] = new org.omg.CORBA.StructMember (
            "failedPath",
            _tcOf_members0,
            null);
          __typeCode = org.omg.CORBA.ORB.init ().create_struct_tc (pgrid.service.spi.corba.repair.RepairIssueHelper.id (), "RepairIssue", _members0);
          __active = false;
        }
      }
    }
    return __typeCode;
  }

  public static String id ()
  {
    return _id;
  }

  public static pgrid.service.spi.corba.repair.RepairIssue read (org.omg.CORBA.portable.InputStream istream)
  {
    pgrid.service.spi.corba.repair.RepairIssue value = new pgrid.service.spi.corba.repair.RepairIssue ();
    value.issueType = pgrid.service.spi.corba.repair.IssueTypeHelper.read (istream);
    value.failedPeer = pgrid.service.spi.corba.PeerReferenceHelper.read (istream);
    value.failedPath = istream.read_string ();
    return value;
  }

  public static void write (org.omg.CORBA.portable.OutputStream ostream, pgrid.service.spi.corba.repair.RepairIssue value)
  {
    pgrid.service.spi.corba.repair.IssueTypeHelper.write (ostream, value.issueType);
    pgrid.service.spi.corba.PeerReferenceHelper.write (ostream, value.failedPeer);
    ostream.write_string (value.failedPath);
  }

}
