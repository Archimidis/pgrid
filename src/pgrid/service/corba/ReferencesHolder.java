package pgrid.service.corba;


/**
 * pgrid/service/spi/corba/ReferencesHolder.java .
 * Generated by the IDL-to-Java compiler (portable), version "3.2"
 * from resources/pgrid_corba.idl
 * Saturday, January 21, 2012 1:52:15 AM EET
 */

public final class ReferencesHolder implements org.omg.CORBA.portable.Streamable {
    public PeerReference value[][] = null;

    public ReferencesHolder() {
    }

    public ReferencesHolder(PeerReference[][] initialValue) {
        value = initialValue;
    }

    public void _read(org.omg.CORBA.portable.InputStream i) {
        value = ReferencesHelper.read(i);
    }

    public void _write(org.omg.CORBA.portable.OutputStream o) {
        ReferencesHelper.write(o, value);
    }

    public org.omg.CORBA.TypeCode _type() {
        return ReferencesHelper.type();
    }

}
