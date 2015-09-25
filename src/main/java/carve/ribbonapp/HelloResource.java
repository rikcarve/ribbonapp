package carve.ribbonapp;

import io.netty.buffer.ByteBuf;

import java.nio.charset.StandardCharsets;

import javax.annotation.PostConstruct;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;

import com.netflix.ribbon.Ribbon;

@Path("/hello")
public class HelloResource {

    private GreetingProxy greetingProxy;

    @PostConstruct
    public void init() {
        greetingProxy = Ribbon.from(GreetingProxy.class);
    }

    @GET
    @Path("world")
    @Produces("text/plain")
    public String world() {
        ByteBuf resp = greetingProxy.sayWorld().execute();
        return resp.toString(StandardCharsets.ISO_8859_1);
    }
}
