package pgrid.service.corba;


/**
 * pgrid/service/corba/CorbaRoutingTable.java .
 * Generated by the IDL-to-Java compiler (portable), version "3.2"
 * from resources/pgrid_corba.idl
 * Wednesday, April 18, 2012 12:20:15 PM EEST
 */

public final class CorbaRoutingTable implements org.omg.CORBA.portable.IDLEntity {
    public pgrid.service.corba.PeerReference refs[][] = null;
    public pgrid.service.corba.PeerReference peer = null;

    public CorbaRoutingTable() {
    } // ctor

    public CorbaRoutingTable(pgrid.service.corba.PeerReference[][] _refs, pgrid.service.corba.PeerReference _peer) {
        refs = _refs;
        peer = _peer;
    } // ctor

} // class CorbaRoutingTable
