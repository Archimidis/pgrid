package pgrid.service.corba.storage;


/**
 * pgrid/service/corba/storage/SearchRequestHelper.java .
 * Generated by the IDL-to-Java compiler (portable), version "3.2"
 * from resources/demo_storage_corba.idl
 * Παρασκευή, 5 Οκτώβριος 2012 7:51:04 μμ EEST
 */

abstract public class SearchRequestHelper {
    private static String _id = "IDL:pgrid/service/corba/storage/SearchRequest:1.0";

    public static void insert(org.omg.CORBA.Any a, pgrid.service.corba.storage.SearchRequest that) {
        org.omg.CORBA.portable.OutputStream out = a.create_output_stream();
        a.type(type());
        write(out, that);
        a.read_value(out.create_input_stream(), type());
    }

    public static pgrid.service.corba.storage.SearchRequest extract(org.omg.CORBA.Any a) {
        return read(a.create_input_stream());
    }

    private static org.omg.CORBA.TypeCode __typeCode = null;
    private static boolean __active = false;

    synchronized public static org.omg.CORBA.TypeCode type() {
        if (__typeCode == null) {
            synchronized (org.omg.CORBA.TypeCode.class) {
                if (__typeCode == null) {
                    if (__active) {
                        return org.omg.CORBA.ORB.init().create_recursive_tc(_id);
                    }
                    __active = true;
                    org.omg.CORBA.StructMember[] _members0 = new org.omg.CORBA.StructMember[2];
                    org.omg.CORBA.TypeCode _tcOf_members0 = null;
                    _tcOf_members0 = org.omg.CORBA.ORB.init().create_string_tc(0);
                    _members0[0] = new org.omg.CORBA.StructMember(
                            "key",
                            _tcOf_members0,
                            null);
                    _tcOf_members0 = org.omg.CORBA.ORB.init().create_string_tc(0);
                    _members0[1] = new org.omg.CORBA.StructMember(
                            "filename",
                            _tcOf_members0,
                            null);
                    __typeCode = org.omg.CORBA.ORB.init().create_struct_tc(pgrid.service.corba.storage.SearchRequestHelper.id(), "SearchRequest", _members0);
                    __active = false;
                }
            }
        }
        return __typeCode;
    }

    public static String id() {
        return _id;
    }

    public static pgrid.service.corba.storage.SearchRequest read(org.omg.CORBA.portable.InputStream istream) {
        pgrid.service.corba.storage.SearchRequest value = new pgrid.service.corba.storage.SearchRequest();
        value.key = istream.read_string();
        value.filename = istream.read_string();
        return value;
    }

    public static void write(org.omg.CORBA.portable.OutputStream ostream, pgrid.service.corba.storage.SearchRequest value) {
        ostream.write_string(value.key);
        ostream.write_string(value.filename);
    }

}