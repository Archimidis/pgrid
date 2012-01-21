package pgrid.process;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import pgrid.entity.Host;
import pgrid.service.repair.RepairService;
import pgrid.service.simulation.SimulationService;

/**
 * @author Vourlakis Nikolas <nvourlakis@gmail.com>
 */
public class SimulationScenarioProcess {

    private static final Logger logger_ = LoggerFactory.getLogger(SimulationScenarioProcess.class);

    private SimulationService simulation_;
    private RepairService repair_;

    public SimulationScenarioProcess(SimulationService simulation, RepairService repair) {
        simulation_ = simulation;
        repair_ = repair;
    }

    public void singleHostFailure(String filename, Host hostToKill, Host... network) {
        simulation_.killHost(hostToKill);

        try {
            Thread.sleep(1000);
        } catch (InterruptedException ignored) {
        }

        repair_.fixNode(hostToKill);

        try {
            Thread.sleep(1000);
        } catch (InterruptedException ignored) {
        }

        for (Host host : network) {
            simulation_.terminateSimulation(network);
        }

        try {
            Thread.sleep(1000);
        } catch (InterruptedException ignored) {
        }

        simulation_.shutdownLocalPeer();
    }
}
