package pgrid.service.corba;

/**
 * pgrid/service/corba/CorbaRoutingTableHolder.java .
 * Generated by the IDL-to-Java compiler (portable), version "3.2"
 * from resources/pgrid_corba.idl
 * Wednesday, April 18, 2012 12:20:15 PM EEST
 */

public final class CorbaRoutingTableHolder implements org.omg.CORBA.portable.Streamable {
    public pgrid.service.corba.CorbaRoutingTable value = null;

    public CorbaRoutingTableHolder() {
    }

    public CorbaRoutingTableHolder(pgrid.service.corba.CorbaRoutingTable initialValue) {
        value = initialValue;
    }

    public void _read(org.omg.CORBA.portable.InputStream i) {
        value = pgrid.service.corba.CorbaRoutingTableHelper.read(i);
    }

    public void _write(org.omg.CORBA.portable.OutputStream o) {
        pgrid.service.corba.CorbaRoutingTableHelper.write(o, value);
    }

    public org.omg.CORBA.TypeCode _type() {
        return pgrid.service.corba.CorbaRoutingTableHelper.type();
    }

}
