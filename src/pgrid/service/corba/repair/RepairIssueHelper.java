package pgrid.service.corba.repair;


/**
* pgrid/service/corba/repair/RepairIssueHelper.java .
* Generated by the IDL-to-Java compiler (portable), version "3.2"
* from resources/pgrid_corba.idl
* Wednesday, April 18, 2012 12:20:15 PM EEST
*/

abstract public class RepairIssueHelper
{
  private static String  _id = "IDL:pgrid/service/corba/repair/RepairIssue:1.0";

  public static void insert (org.omg.CORBA.Any a, pgrid.service.corba.repair.RepairIssue that)
  {
    org.omg.CORBA.portable.OutputStream out = a.create_output_stream ();
    a.type (type ());
    write (out, that);
    a.read_value (out.create_input_stream (), type ());
  }

  public static pgrid.service.corba.repair.RepairIssue extract (org.omg.CORBA.Any a)
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
          org.omg.CORBA.StructMember[] _members0 = new org.omg.CORBA.StructMember [2];
          org.omg.CORBA.TypeCode _tcOf_members0 = null;
          _tcOf_members0 = pgrid.service.corba.repair.IssueStateHelper.type ();
          _members0[0] = new org.omg.CORBA.StructMember (
            "issueState",
            _tcOf_members0,
            null);
          _tcOf_members0 = pgrid.service.corba.PeerReferenceHelper.type ();
          _members0[1] = new org.omg.CORBA.StructMember (
            "failedPeer",
            _tcOf_members0,
            null);
          __typeCode = org.omg.CORBA.ORB.init ().create_struct_tc (pgrid.service.corba.repair.RepairIssueHelper.id (), "RepairIssue", _members0);
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

  public static pgrid.service.corba.repair.RepairIssue read (org.omg.CORBA.portable.InputStream istream)
  {
    pgrid.service.corba.repair.RepairIssue value = new pgrid.service.corba.repair.RepairIssue ();
    value.issueState = pgrid.service.corba.repair.IssueStateHelper.read (istream);
    value.failedPeer = pgrid.service.corba.PeerReferenceHelper.read (istream);
    return value;
  }

  public static void write (org.omg.CORBA.portable.OutputStream ostream, pgrid.service.corba.repair.RepairIssue value)
  {
    pgrid.service.corba.repair.IssueStateHelper.write (ostream, value.issueState);
    pgrid.service.corba.PeerReferenceHelper.write (ostream, value.failedPeer);
  }

}
