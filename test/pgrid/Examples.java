package pgrid;

import org.junit.Test;
import pgrid.entity.Host;
import pgrid.entity.internal.PGridHost;
import pgrid.entity.routingtable.RoutingTable;

import java.net.UnknownHostException;

/**
 * @author Nikolas Vourlakis <nvourlakis@gmail.com>
 */
public class Examples {
    @Test
    public void closestHost() throws UnknownHostException {
        Host localhost = new PGridHost("127.0.0.1", 3000);
        localhost.setHostPath("000");
        RoutingTable rt = new RoutingTable();
        rt.setLocalhost(localhost);

        Host host1 = new PGridHost("127.0.0.1", 1111);
        host1.setHostPath("1");
        Host host2 = new PGridHost("127.0.0.1", 2222);
        host2.setHostPath("01");
        Host host3 = new PGridHost("127.0.0.1", 3333);
        host3.setHostPath("0010");
        Host host4 = new PGridHost("127.0.0.1", 4444);
        host4.setHostPath("0011");

        rt.addReference(0, host1);
        rt.addReference(1, host2);
        rt.addReference(2, host3);
        rt.addReference(2, host4);
    }
}
