package pgrid.service.corba.storage;

/**
 * pgrid/service/corba/storage/SearchResponseHolder.java .
 * Generated by the IDL-to-Java compiler (portable), version "3.2"
 * from resources/demo_storage_corba.idl
 * Παρασκευή, 5 Οκτώβριος 2012 7:51:04 μμ EEST
 */

public final class SearchResponseHolder implements org.omg.CORBA.portable.Streamable {
    public pgrid.service.corba.storage.SearchResponse value = null;

    public SearchResponseHolder() {
    }

    public SearchResponseHolder(pgrid.service.corba.storage.SearchResponse initialValue) {
        value = initialValue;
    }

    public void _read(org.omg.CORBA.portable.InputStream i) {
        value = pgrid.service.corba.storage.SearchResponseHelper.read(i);
    }

    public void _write(org.omg.CORBA.portable.OutputStream o) {
        pgrid.service.corba.storage.SearchResponseHelper.write(o, value);
    }

    public org.omg.CORBA.TypeCode _type() {
        return pgrid.service.corba.storage.SearchResponseHelper.type();
    }

}
