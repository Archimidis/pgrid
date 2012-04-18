package pgrid.service.corba.repair;


/**
* pgrid/service/corba/repair/_RepairHandleStub.java .
* Generated by the IDL-to-Java compiler (portable), version "3.2"
* from resources/pgrid_corba.idl
* Wednesday, April 18, 2012 12:20:15 PM EEST
*/

public class _RepairHandleStub extends org.omg.CORBA.portable.ObjectImpl implements pgrid.service.corba.repair.RepairHandle
{

  public void fixNode (String footpath, pgrid.service.corba.repair.RepairIssue issue)
  {
            org.omg.CORBA.portable.InputStream $in = null;
            try {
                org.omg.CORBA.portable.OutputStream $out = _request ("fixNode", true);
                $out.write_string (footpath);
                pgrid.service.corba.repair.RepairIssueHelper.write ($out, issue);
                $in = _invoke ($out);
                return;
            } catch (org.omg.CORBA.portable.ApplicationException $ex) {
                $in = $ex.getInputStream ();
                String _id = $ex.getId ();
                throw new org.omg.CORBA.MARSHAL (_id);
            } catch (org.omg.CORBA.portable.RemarshalException $rm) {
                fixNode (footpath, issue        );
            } finally {
                _releaseReply ($in);
            }
  } // fixNode

  public void fixSubtree (String footpath, String subtreePrefix, pgrid.service.corba.repair.RepairIssue[] issues)
  {
            org.omg.CORBA.portable.InputStream $in = null;
            try {
                org.omg.CORBA.portable.OutputStream $out = _request ("fixSubtree", true);
                $out.write_string (footpath);
                $out.write_string (subtreePrefix);
                pgrid.service.corba.repair.RepairHandlePackage.IssueArrayHelper.write ($out, issues);
                $in = _invoke ($out);
                return;
            } catch (org.omg.CORBA.portable.ApplicationException $ex) {
                $in = $ex.getInputStream ();
                String _id = $ex.getId ();
                throw new org.omg.CORBA.MARSHAL (_id);
            } catch (org.omg.CORBA.portable.RemarshalException $rm) {
                fixSubtree (footpath, subtreePrefix, issues        );
            } finally {
                _releaseReply ($in);
            }
  } // fixSubtree

  public pgrid.service.corba.PeerReference replace (String failedPath, pgrid.service.corba.repair.RepairIssue[] issues)
  {
            org.omg.CORBA.portable.InputStream $in = null;
            try {
                org.omg.CORBA.portable.OutputStream $out = _request ("replace", true);
                $out.write_string (failedPath);
                pgrid.service.corba.repair.RepairHandlePackage.IssueArrayHelper.write ($out, issues);
                $in = _invoke ($out);
                pgrid.service.corba.PeerReference $result = pgrid.service.corba.PeerReferenceHelper.read ($in);
                return $result;
            } catch (org.omg.CORBA.portable.ApplicationException $ex) {
                $in = $ex.getInputStream ();
                String _id = $ex.getId ();
                throw new org.omg.CORBA.MARSHAL (_id);
            } catch (org.omg.CORBA.portable.RemarshalException $rm) {
                return replace (failedPath, issues        );
            } finally {
                _releaseReply ($in);
            }
  } // replace

  public void pushSolution (pgrid.service.corba.repair.RepairSolution solution)
  {
            org.omg.CORBA.portable.InputStream $in = null;
            try {
                org.omg.CORBA.portable.OutputStream $out = _request ("pushSolution", true);
                pgrid.service.corba.repair.RepairSolutionHelper.write ($out, solution);
                $in = _invoke ($out);
                return;
            } catch (org.omg.CORBA.portable.ApplicationException $ex) {
                $in = $ex.getInputStream ();
                String _id = $ex.getId ();
                throw new org.omg.CORBA.MARSHAL (_id);
            } catch (org.omg.CORBA.portable.RemarshalException $rm) {
                pushSolution (solution        );
            } finally {
                _releaseReply ($in);
            }
  } // pushSolution

  // Type-specific CORBA::Object operations
  private static String[] __ids = {
    "IDL:pgrid/service/corba/repair/RepairHandle:1.0"};

  public String[] _ids ()
  {
    return (String[])__ids.clone ();
  }

  private void readObject (java.io.ObjectInputStream s) throws java.io.IOException
  {
     String str = s.readUTF ();
     String[] args = null;
     java.util.Properties props = null;
     org.omg.CORBA.ORB orb = org.omg.CORBA.ORB.init (args, props);
   try {
     org.omg.CORBA.Object obj = orb.string_to_object (str);
     org.omg.CORBA.portable.Delegate delegate = ((org.omg.CORBA.portable.ObjectImpl) obj)._get_delegate ();
     _set_delegate (delegate);
   } finally {
     orb.destroy() ;
   }
  }

  private void writeObject (java.io.ObjectOutputStream s) throws java.io.IOException
  {
     String[] args = null;
     java.util.Properties props = null;
     org.omg.CORBA.ORB orb = org.omg.CORBA.ORB.init (args, props);
   try {
     String str = orb.object_to_string (this);
     s.writeUTF (str);
   } finally {
     orb.destroy() ;
   }
  }
} // class _RepairHandleStub
