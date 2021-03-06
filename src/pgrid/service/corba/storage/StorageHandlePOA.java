package pgrid.service.corba.storage;


/**
 * pgrid/service/corba/storage/StorageHandlePOA.java .
 * Generated by the IDL-to-Java compiler (portable), version "3.2"
 * from resources/demo_storage_corba.idl
 * Παρασκευή, 5 Οκτώβριος 2012 7:51:04 μμ EEST
 */

public abstract class StorageHandlePOA extends org.omg.PortableServer.Servant
        implements pgrid.service.corba.storage.StorageHandleOperations, org.omg.CORBA.portable.InvokeHandler {

    // Constructors

    private static java.util.Hashtable _methods = new java.util.Hashtable();

    static {
        _methods.put("search", new java.lang.Integer(0));
    }

    public org.omg.CORBA.portable.OutputStream _invoke(String $method,
                                                       org.omg.CORBA.portable.InputStream in,
                                                       org.omg.CORBA.portable.ResponseHandler $rh) {
        org.omg.CORBA.portable.OutputStream out = null;
        java.lang.Integer __method = (java.lang.Integer) _methods.get($method);
        if (__method == null)
            throw new org.omg.CORBA.BAD_OPERATION(0, org.omg.CORBA.CompletionStatus.COMPLETED_MAYBE);

        switch (__method.intValue()) {
            case 0:  // pgrid/service/corba/storage/StorageHandle/search
            {
                pgrid.service.corba.storage.SearchRequest request = pgrid.service.corba.storage.SearchRequestHelper.read(in);
                pgrid.service.corba.storage.SearchResponse $result = null;
                $result = this.search(request);
                out = $rh.createReply();
                pgrid.service.corba.storage.SearchResponseHelper.write(out, $result);
                break;
            }

            default:
                throw new org.omg.CORBA.BAD_OPERATION(0, org.omg.CORBA.CompletionStatus.COMPLETED_MAYBE);
        }

        return out;
    } // _invoke

    // Type-specific CORBA::Object operations
    private static String[] __ids = {
            "IDL:pgrid/service/corba/storage/StorageHandle:1.0"};

    public String[] _all_interfaces(org.omg.PortableServer.POA poa, byte[] objectId) {
        return (String[]) __ids.clone();
    }

    public StorageHandle _this() {
        return StorageHandleHelper.narrow(
                super._this_object());
    }

    public StorageHandle _this(org.omg.CORBA.ORB orb) {
        return StorageHandleHelper.narrow(
                super._this_object(orb));
    }


} // class StorageHandlePOA
