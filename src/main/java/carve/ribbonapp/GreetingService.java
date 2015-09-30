package carve.ribbonapp;

import io.netty.buffer.ByteBuf;

import com.netflix.ribbon.RibbonRequest;
import com.netflix.ribbon.proxy.annotation.ClientProperties;
import com.netflix.ribbon.proxy.annotation.ClientProperties.Property;
import com.netflix.ribbon.proxy.annotation.Http;
import com.netflix.ribbon.proxy.annotation.Http.HttpMethod;
import com.netflix.ribbon.proxy.annotation.Hystrix;

@ClientProperties(properties = {
        @Property(name = "ReadTimeout", value = "2000"),
        @Property(name = "ConnectTimeout", value = "1000"),
        @Property(name = "NIWSServerListClassName", value = "carve.ribbonapp.CuratorServerList") })
public interface GreetingService {

    @Http(method = HttpMethod.GET, uri = "/carve.greeting/v1/greeting/")
    @Hystrix(fallbackHandler = GreetingFallback.class)
    RibbonRequest<ByteBuf> sayWorld();

}
