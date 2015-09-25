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

import com.netflix.loadbalancer.Server;

/**
 * Locates services (ServiceProvider) in Zookeeper. Helper class to ease the use
 * of Curator/Zookeeper
 */
public class CuratorServiceLocator {
    private static ServiceDiscovery<Object> serviceDiscovery;

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

    public static List<Server> getListOfServers(String serviceName) {
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
