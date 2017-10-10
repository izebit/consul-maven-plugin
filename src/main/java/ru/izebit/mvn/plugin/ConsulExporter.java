package ru.izebit.mvn.plugin;

import com.orbitz.consul.KeyValueClient;

import java.util.Map.Entry;

/**
 * @author <a href="izebit@gmail.com">Artem Konovalov</a> <br/>
 * Date: 09/10/2017/.
 */
public class ConsulExporter {
    private final KeyValueClient client;
    private final String prefix;


    public ConsulExporter(KeyValueClient client, String prefix) {
        if (prefix.endsWith("/")) {
            this.prefix = prefix;
        } else {
            this.prefix = prefix + "/";
        }

        this.client = client;
    }

    public void export(Entry<String, String> entry) {
        client.putValue(getFullKey(entry.getKey()), entry.getValue());
    }

    private String getFullKey(String key) {
        return prefix + key;
    }
}
