package ru.izebit.mvn.plugin;

import com.orbitz.consul.Consul;
import com.orbitz.consul.KeyValueClient;
import com.pszymczyk.consul.junit.ConsulResource;
import org.junit.ClassRule;
import org.junit.Test;

/**
 * @author <a href="izebit@gmail.com">Artem Konovalov</a> <br/>
 * Date: 10/10/2017/.
 */
public class IntegrationTest {
    @ClassRule
    public static final ConsulResource consul = new ConsulResource();


    @Test
    public void test() {

        KeyValueClient client = Consul
                .builder()
                .withUrl("http://localhost:"+consul.getHttpPort())
                .build()
                .keyValueClient();
        client.putValue("hello", "world");

        System.out.println(client.getValue("hello"));
    }
}
