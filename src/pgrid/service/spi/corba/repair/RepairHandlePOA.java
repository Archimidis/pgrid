package pgrid.service.spi.corba.repair;


/**
* pgrid/service/spi/corba/repair/RepairHandlePOA.java .
* Generated by the IDL-to-Java compiler (portable), version "3.2"
* from resources/pgrid_corba.idl
* Thursday, January 19, 2012 10:19:41 PM EET
*/

public abstract class RepairHandlePOA extends org.omg.PortableServer.Servant
 implements pgrid.service.spi.corba.repair.RepairHandleOperations, org.omg.CORBA.portable.InvokeHandler
{

  // Constructors

  private static java.util.Hashtable _methods = new java.util.Hashtable ();
  static
  {
    _methods.put ("fixNode", new java.lang.Integer (0));
    _methods.put ("fixSubtree", new java.lang.Integer (1));
    _methods.put ("replace", new java.lang.Integer (2));
    _methods.put ("broadcastSolution", new java.lang.Integer (3));
  }

  public org.omg.CORBA.portable.OutputStream _invoke (String $method,
                                org.omg.CORBA.portable.InputStream in,
                                org.omg.CORBA.portable.ResponseHandler $rh)
  {
    org.omg.CORBA.portable.OutputStream out = null;
    java.lang.Integer __method = (java.lang.Integer)_methods.get ($method);
    if (__method == null)
      throw new org.omg.CORBA.BAD_OPERATION (0, org.omg.CORBA.CompletionStatus.COMPLETED_MAYBE);

    switch (__method.intValue ())
    {
       case 0:  // pgrid/service/spi/corba/repair/RepairHandle/fixNode
       {
         String footpath = in.read_string ();
         pgrid.service.spi.corba.repair.RepairIssue issue = pgrid.service.spi.corba.repair.RepairIssueHelper.read (in);
         this.fixNode (footpath, issue);
         out = $rh.createReply();
         break;
       }

       case 1:  // pgrid/service/spi/corba/repair/RepairHandle/fixSubtree
       {
         String footpath = in.read_string ();
         String subtreePrefix = in.read_string ();
         pgrid.service.spi.corba.repair.RepairIssue issues[] = pgrid.service.spi.corba.repair.RepairHandlePackage.IssueArrayHelper.read (in);
         this.fixSubtree (footpath, subtreePrefix, issues);
         out = $rh.createReply();
         break;
       }

       case 2:  // pgrid/service/spi/corba/repair/RepairHandle/replace
       {
         String failedPath = in.read_string ();
         pgrid.service.spi.corba.repair.RepairIssue issues[] = pgrid.service.spi.corba.repair.RepairHandlePackage.IssueArrayHelper.read (in);
         this.replace (failedPath, issues);
         out = $rh.createReply();
         break;
       }

       case 3:  // pgrid/service/spi/corba/repair/RepairHandle/broadcastSolution
       {
         pgrid.service.spi.corba.repair.RepairSolution solution = pgrid.service.spi.corba.repair.RepairSolutionHelper.read (in);
         this.broadcastSolution (solution);
         out = $rh.createReply();
         break;
       }

       default:
         throw new org.omg.CORBA.BAD_OPERATION (0, org.omg.CORBA.CompletionStatus.COMPLETED_MAYBE);
    }

    return out;
  } // _invoke

  // Type-specific CORBA::Object operations
  private static String[] __ids = {
    "IDL:pgrid/service/spi/corba/repair/RepairHandle:1.0"};

  public String[] _all_interfaces (org.omg.PortableServer.POA poa, byte[] objectId)
  {
    return (String[])__ids.clone ();
  }

  public RepairHandle _this() 
  {
    return RepairHandleHelper.narrow(
    super._this_object());
  }

  public RepairHandle _this(org.omg.CORBA.ORB orb) 
  {
    return RepairHandleHelper.narrow(
    super._this_object(orb));
  }


} // class RepairHandlePOA
