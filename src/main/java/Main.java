import com.google.common.base.Predicate;
import java.io.IOException;
import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.NetworkInterface;
import org.anarres.dhcp.common.address.NetworkAddress;
import org.anarres.dhcp.common.address.Subnet;
import org.apache.directory.server.dhcp.DhcpException;
import org.apache.directory.server.dhcp.io.DhcpRequestContext;
import org.apache.directory.server.dhcp.messages.DhcpMessage;
import org.apache.directory.server.dhcp.messages.HardwareAddress;
import org.apache.directory.server.dhcp.netty.DhcpServer;
import org.apache.directory.server.dhcp.service.manager.AbstractDynamicLeaseManager;

/**
 * Created by mmarsale on 5.8.2015.
 */
public class Main {

    public static void main(String[] args) {
        final AbstractDynamicLeaseManager manager = new AbstractDynamicLeaseManager() {

            @Override protected InetAddress getFixedAddressFor(final HardwareAddress hardwareAddress)
                throws DhcpException {
                System.out.println("NetconfConnectorModule.getFixedAddressFor");
                return null;
            }

            @Override protected Subnet getSubnetFor(final NetworkAddress networkAddress) throws DhcpException {
                System.out.println("NetconfConnectorModule.getSubnetFor");
                return null;
            }

            @Override protected boolean leaseIp(final InetAddress address, final HardwareAddress hardwareAddress,
                final long ttl) throws Exception {
                System.out.println("NetconfConnectorModule.leaseIp");
                return false;
            }

            @Override protected InetAddress leaseMac(final DhcpRequestContext context, final DhcpMessage request,
                final InetAddress clientRequestedAddress, final long ttl) throws Exception {
                System.err.println(request.getOptions());
                System.err.println(clientRequestedAddress);
                System.err.println(ttl);
                System.out.println("NetconfConnectorModule.leaseMac");
                return Inet4Address.getByName("localhost");
            }
        };
        final DhcpServer dhcpServer = new DhcpServer(manager);

        try {
            // dont use start, provide nio event loop group ourselves
            dhcpServer.addInterfaces(new Predicate<NetworkInterface>() {
                public boolean apply(final NetworkInterface input) {
                    return true;
                }
            });
            dhcpServer.start();
            Thread.sleep(500000);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
