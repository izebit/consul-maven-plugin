package ru.izebit.mvn.plugin;

import com.orbitz.consul.KeyValueClient;
import org.apache.commons.lang3.StringUtils;
import org.apache.maven.plugin.logging.Log;

import java.util.Map.Entry;

/**
 * @author <a href="izebit@gmail.com">Artem Konovalov</a> <br/>
 * Date: 09/10/2017/.
 */
public class ConsulExporter {
    private final KeyValueClient client;
    private final String prefix;
    private final Log log;


    public ConsulExporter(KeyValueClient client, String prefix, Log log) {
        if (StringUtils.isBlank(prefix)) {
            this.prefix = "";
        } else if (prefix.endsWith("/")) {
            this.prefix = prefix;
        } else {
            this.prefix = prefix + "/";
        }

        this.client = client;
        this.log = log;
    }

    public void export(Entry<String, String> entry) {
        String fullKey = getFullKey(entry.getKey());
        log.info("put key: " + fullKey + " with value: " + entry.getValue());
        client.putValue(fullKey, entry.getValue());
    }

    private String getFullKey(String key) {
        return prefix + key;
    }
}
