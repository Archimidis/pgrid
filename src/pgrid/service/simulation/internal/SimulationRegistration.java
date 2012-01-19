package pgrid.service.simulation.internal;

import org.omg.CORBA.ORB;
import org.omg.CORBA.ORBPackage.InvalidName;
import org.omg.PortableServer.POA;
import org.omg.PortableServer.POAHelper;
import org.omg.PortableServer.POAPackage.ServantAlreadyActive;
import org.omg.PortableServer.POAPackage.ServantNotActive;
import org.omg.PortableServer.POAPackage.WrongPolicy;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import pgrid.service.LocalPeerContext;
import pgrid.service.ServiceRegistration;
import pgrid.service.ServiceRegistrationException;
import pgrid.service.simulation.spi.SimulationHandlerProvider;
import pgrid.service.spi.corba.repair.RepairHandleHelper;
import pgrid.service.spi.corba.simulation.SimulationHandlePOA;

import javax.inject.Inject;

/**
 * @author Vourlakis Nikolas <nvourlakis@gmail.com>
 */
public class SimulationRegistration implements ServiceRegistration {
    private static final Logger logger_ = LoggerFactory.getLogger(SimulationRegistration.class);

    private final ORB orb_;
    private final SimulationHandlerProvider provider_;

    @Inject
    public SimulationRegistration(SimulationHandlerProvider provider, LocalPeerContext context) {
        orb_ = context.getCorba();
        provider_ = provider;
    }

    @Override
    public void register() throws ServiceRegistrationException {
        try {
            POA rootPOA = POAHelper.narrow(orb_.resolve_initial_references("RootPOA"));
            SimulationHandlePOA simulationServant = provider_.get();
            rootPOA.activate_object(simulationServant);
            String[] ID = RepairHandleHelper.id().split(":");
            ((com.sun.corba.se.spi.orb.ORB) orb_).register_initial_reference(
                    ID[1],
                    rootPOA.servant_to_reference(simulationServant)
            );
            logger_.info("Simulation service registered");
        } catch (InvalidName invalidName) {
            throw new ServiceRegistrationException(invalidName);
        } catch (WrongPolicy wrongPolicy) {
            throw new ServiceRegistrationException(wrongPolicy);
        } catch (ServantNotActive servantNotActive) {
            throw new ServiceRegistrationException(servantNotActive);
        } catch (ServantAlreadyActive servantAlreadyActive) {
            throw new ServiceRegistrationException(servantAlreadyActive);
        }
    }
}
