package pgrid.service.corba.repair.RepairHandlePackage;


/**
 * pgrid/service/spi/corba/repair/RepairHandlePackage/IssueArrayHelper.java .
 * Generated by the IDL-to-Java compiler (portable), version "3.2"
 * from resources/pgrid_corba.idl
 * Saturday, January 21, 2012 1:52:15 AM EET
 */

abstract public class IssueArrayHelper {
    private static String _id = "IDL:pgrid/service/spi/corba/repair/RepairHandle/IssueArray:1.0";

    public static void insert(org.omg.CORBA.Any a, pgrid.service.corba.repair.RepairIssue[] that) {
        org.omg.CORBA.portable.OutputStream out = a.create_output_stream();
        a.type(type());
        write(out, that);
        a.read_value(out.create_input_stream(), type());
    }

    public static pgrid.service.corba.repair.RepairIssue[] extract(org.omg.CORBA.Any a) {
        return read(a.create_input_stream());
    }

    private static org.omg.CORBA.TypeCode __typeCode = null;

    synchronized public static org.omg.CORBA.TypeCode type() {
        if (__typeCode == null) {
            __typeCode = pgrid.service.corba.repair.RepairIssueHelper.type();
            __typeCode = org.omg.CORBA.ORB.init().create_sequence_tc(0, __typeCode);
            __typeCode = org.omg.CORBA.ORB.init().create_alias_tc(pgrid.service.corba.repair.RepairHandlePackage.IssueArrayHelper.id(), "IssueArray", __typeCode);
        }
        return __typeCode;
    }

    public static String id() {
        return _id;
    }

    public static pgrid.service.corba.repair.RepairIssue[] read(org.omg.CORBA.portable.InputStream istream) {
        pgrid.service.corba.repair.RepairIssue value[] = null;
        int _len0 = istream.read_long();
        value = new pgrid.service.corba.repair.RepairIssue[_len0];
        for (int _o1 = 0; _o1 < value.length; ++_o1)
            value[_o1] = pgrid.service.corba.repair.RepairIssueHelper.read(istream);
        return value;
    }

    public static void write(org.omg.CORBA.portable.OutputStream ostream, pgrid.service.corba.repair.RepairIssue[] value) {
        ostream.write_long(value.length);
        for (int _i0 = 0; _i0 < value.length; ++_i0)
            pgrid.service.corba.repair.RepairIssueHelper.write(ostream, value[_i0]);
    }

}
