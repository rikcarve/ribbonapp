package carve.ribbonapp;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.retry.RetryNTimes;
import org.apache.curator.x.discovery.ServiceDiscovery;
import org.apache.curator.x.discovery.ServiceDiscoveryBuilder;
import org.apache.curator.x.discovery.ServiceInstance;

import com.netflix.client.config.IClientConfig;
import com.netflix.loadbalancer.AbstractServerList;
import com.netflix.loadbalancer.Server;

/**
 * Ribbon server list which uses Curator to lookup available servers. Must be
 * specified through the property "NIWSServerListClassName"
 */
public class CuratorServerList extends AbstractServerList<Server> {

    private static ServiceDiscovery<Object> serviceDiscovery;
    private String clientName;

    static {
        CuratorFramework curatorFramework = CuratorFrameworkFactory.newClient(
                "localhost:2181", new RetryNTimes(5, 1000));
        curatorFramework.start();

        serviceDiscovery = ServiceDiscoveryBuilder
                .builder(Object.class).basePath("carve")
                .client(curatorFramework).build();
        try {
            serviceDiscovery.start();
        } catch (Exception e) {
            e.printStackTrace();
        }
        System.out.println("Curator init complete");
    }

    @Override
    public List<Server> getInitialListOfServers() {
        System.out.println("getInitialListOfServers");
        return null;
    }

    @Override
    public List<Server> getUpdatedListOfServers() {
        System.out.println("getUpdatedListOfServers for " + clientName);
        return getListOfServers(clientName);
        // List<Server> list = new ArrayList<Server>();
        // list.add(new Server("localhost", 8180));
        // return list;
    }

    @Override
    public void initWithNiwsConfig(IClientConfig clientConfig) {
        clientName = clientConfig.getClientName();
    }

    private List<Server> getListOfServers(String serviceName) {
        List<Server> list = new ArrayList<Server>();
        try {
            Collection<ServiceInstance<Object>> instances = serviceDiscovery.queryForInstances(serviceName);
            for (ServiceInstance<Object> instance : instances) {
                list.add(new Server(instance.getAddress(), instance.getPort()));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }

}
