package pgrid.service.simulation.internal;

import org.omg.CORBA.ORB;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import pgrid.entity.routingtable.RoutingTable;
import pgrid.service.simulation.PersistencyException;
import pgrid.service.simulation.spi.PersistencyDelegate;

import java.io.FileNotFoundException;

/**
 * @author Vourlakis Nikolas <nvourlakis@gmail.com>
 */
public class SimulationDelegate {
    private static final Logger logger_ = LoggerFactory.getLogger(SimulationDelegate.class);

    private final RoutingTable routingTable_;
    private final ORB orb_;
    private final PersistencyDelegate delegate_;

    public SimulationDelegate(ORB orb, RoutingTable routingTable, PersistencyDelegate delegate) {
        orb_ = orb;
        routingTable_ = routingTable;
        delegate_ = delegate;
    }


    public void initLocal(String filename) throws PersistencyException, FileNotFoundException {
        routingTable_.clear();
        delegate_.load(filename, routingTable_);
    }

    public void die() {
        logger_.info("Local peer is shutting down");
        String filename = routingTable_.getLocalhost().toString() + ".xml";
        try {
            delegate_.store(filename, routingTable_);
        } catch (FileNotFoundException e) {
        }
        logger_.info("Routing table stored in {}", filename);
        orb_.shutdown(false);
        //orb_.destroy();
        logger_.info("Corba facility terminated");

        logger_.info("Local peer {}:{} is terminating",
                routingTable_.getLocalhost(), routingTable_.getLocalhost().getPort());
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
        }
        System.exit(0);
    }
}
