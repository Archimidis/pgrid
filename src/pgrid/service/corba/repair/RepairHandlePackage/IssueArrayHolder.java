package pgrid.service.corba.repair.RepairHandlePackage;


/**
 * pgrid/service/corba/repair/RepairHandlePackage/IssueArrayHolder.java .
 * Generated by the IDL-to-Java compiler (portable), version "3.2"
 * from resources/pgrid_corba.idl
 * Wednesday, April 18, 2012 12:20:15 PM EEST
 */

public final class IssueArrayHolder implements org.omg.CORBA.portable.Streamable {
    public pgrid.service.corba.repair.RepairIssue value[] = null;

    public IssueArrayHolder() {
    }

    public IssueArrayHolder(pgrid.service.corba.repair.RepairIssue[] initialValue) {
        value = initialValue;
    }

    public void _read(org.omg.CORBA.portable.InputStream i) {
        value = pgrid.service.corba.repair.RepairHandlePackage.IssueArrayHelper.read(i);
    }

    public void _write(org.omg.CORBA.portable.OutputStream o) {
        pgrid.service.corba.repair.RepairHandlePackage.IssueArrayHelper.write(o, value);
    }

    public org.omg.CORBA.TypeCode _type() {
        return pgrid.service.corba.repair.RepairHandlePackage.IssueArrayHelper.type();
    }

}
