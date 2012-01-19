package pgrid.service.simulation.spi;

import pgrid.service.LocalPeerContext;
import pgrid.service.simulation.internal.DefaultSimulationHandler;
import pgrid.service.simulation.internal.SimulationDelegate;
import pgrid.service.spi.corba.simulation.SimulationHandlePOA;

import javax.inject.Inject;
import javax.inject.Provider;

/**
 * @author Vourlakis Nikolas <nvourlakis@gmail.com>
 */
public class SimulationHandlerProvider implements Provider<SimulationHandlePOA> {
    private DefaultSimulationHandler poa_;

    @Inject
    public SimulationHandlerProvider(LocalPeerContext context, PersistencyDelegate persistency) {
        SimulationDelegate delegate = new SimulationDelegate(context.getCorba(), context.getLocalRT(), persistency);
        poa_ = new DefaultSimulationHandler(delegate);
    }


    @Override
    public SimulationHandlePOA get() {
        return poa_;
    }
}
