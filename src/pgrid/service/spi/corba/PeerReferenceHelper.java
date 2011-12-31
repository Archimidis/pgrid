package pgrid.service.spi.corba;


/**
 * pgrid/service/spi/corba/PeerReferenceHelper.java .
 * Generated by the IDL-to-Java compiler (portable), version "3.2"
 * from C:/Users/user/Desktop/DIPLOMATIKH_NIKOLA/libpgrid/resources/pgrid_corba.idl
 */

abstract public class PeerReferenceHelper {
    private static String _id = "IDL:pgrid/service/spi/corba/PeerReference:1.0";

    public static void insert(org.omg.CORBA.Any a, pgrid.service.spi.corba.PeerReference that) {
        org.omg.CORBA.portable.OutputStream out = a.create_output_stream();
        a.type(type());
        write(out, that);
        a.read_value(out.create_input_stream(), type());
    }

    public static pgrid.service.spi.corba.PeerReference extract(org.omg.CORBA.Any a) {
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
                    org.omg.CORBA.StructMember[] _members0 = new org.omg.CORBA.StructMember[5];
                    org.omg.CORBA.TypeCode _tcOf_members0 = null;
                    _tcOf_members0 = org.omg.CORBA.ORB.init().create_string_tc(0);
                    _members0[0] = new org.omg.CORBA.StructMember(
                            "address",
                            _tcOf_members0,
                            null);
                    _tcOf_members0 = org.omg.CORBA.ORB.init().get_primitive_tc(org.omg.CORBA.TCKind.tk_long);
                    _members0[1] = new org.omg.CORBA.StructMember(
                            "port",
                            _tcOf_members0,
                            null);
                    _tcOf_members0 = org.omg.CORBA.ORB.init().create_string_tc(0);
                    _members0[2] = new org.omg.CORBA.StructMember(
                            "path",
                            _tcOf_members0,
                            null);
                    _tcOf_members0 = org.omg.CORBA.ORB.init().get_primitive_tc(org.omg.CORBA.TCKind.tk_longlong);
                    _members0[3] = new org.omg.CORBA.StructMember(
                            "timestamp",
                            _tcOf_members0,
                            null);
                    _tcOf_members0 = org.omg.CORBA.ORB.init().create_string_tc(0);
                    _members0[4] = new org.omg.CORBA.StructMember(
                            "uuid",
                            _tcOf_members0,
                            null);
                    __typeCode = org.omg.CORBA.ORB.init().create_struct_tc(pgrid.service.spi.corba.PeerReferenceHelper.id(), "PeerReference", _members0);
                    __active = false;
                }
            }
        }
        return __typeCode;
    }

    public static String id() {
        return _id;
    }

    public static pgrid.service.spi.corba.PeerReference read(org.omg.CORBA.portable.InputStream istream) {
        pgrid.service.spi.corba.PeerReference value = new pgrid.service.spi.corba.PeerReference();
        value.address = istream.read_string();
        value.port = istream.read_long();
        value.path = istream.read_string();
        value.timestamp = istream.read_longlong();
        value.uuid = istream.read_string();
        return value;
    }

    public static void write(org.omg.CORBA.portable.OutputStream ostream, pgrid.service.spi.corba.PeerReference value) {
        ostream.write_string(value.address);
        ostream.write_long(value.port);
        ostream.write_string(value.path);
        ostream.write_longlong(value.timestamp);
        ostream.write_string(value.uuid);
    }

}
