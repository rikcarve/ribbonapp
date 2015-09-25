package carve.ribbonapp;

import java.util.ArrayList;
import java.util.List;

import com.netflix.client.config.IClientConfig;
import com.netflix.loadbalancer.AbstractServerList;
import com.netflix.loadbalancer.Server;

public class CuratorServerList extends AbstractServerList<Server> {

    @Override
    public List<Server> getInitialListOfServers() {
        System.out.println("getInitialListOfServers");
        return null;
    }

    @Override
    public List<Server> getUpdatedListOfServers() {
        System.out.println("getUpdatedListOfServers");
        List<Server> list = new ArrayList<Server>();
        list.add(new Server("localhost", 8180));
        return list;
    }

    @Override
    public void initWithNiwsConfig(IClientConfig clientConfig) {
    }

}
