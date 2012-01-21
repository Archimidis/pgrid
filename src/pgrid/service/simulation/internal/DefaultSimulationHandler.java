package pgrid.service.simulation.internal;

import pgrid.service.corba.simulation.SimulationHandlePOA;

/**
 * @author Vourlakis Nikolas <nvourlakis@gmail.com>
 */
public class DefaultSimulationHandler extends SimulationHandlePOA {

    private final SimulationDelegate delegate_;

    public DefaultSimulationHandler(SimulationDelegate simulationDelegate) {
        delegate_ = simulationDelegate;
    }

    @Override
    public void die() {
        delegate_.die();
    }

    @Override
    public void terminateSimulation() {
        delegate_.die();
    }
}
