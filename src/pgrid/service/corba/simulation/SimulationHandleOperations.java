package pgrid.service.corba.simulation;


/**
 * pgrid/service/corba/simulation/SimulationHandleOperations.java .
 * Generated by the IDL-to-Java compiler (portable), version "3.2"
 * from resources/pgrid_corba.idl
 * Wednesday, April 18, 2012 12:20:15 PM EEST
 */

public interface SimulationHandleOperations {
    pgrid.service.corba.PeerReference getInfo();

    void die();

    void terminateSimulation();
} // interface SimulationHandleOperations
