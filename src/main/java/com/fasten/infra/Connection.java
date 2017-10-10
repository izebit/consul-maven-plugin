package com.fasten.infra;

import org.apache.maven.plugins.annotations.Parameter;

import java.net.URL;

/**
 * @author <a href="izebit@gmail.com">Artem Konovalov</a> <br/>
 * Date: 08/10/2017/.
 */
public class Connection {
    /**
     * url for connect to consul <br/>
     * for example, http://localhost:8500
     */
    @Parameter(property = "url")
    private URL url;
    /**
     * password for current user
     */
    @Parameter(property = "consul.password")
    private String password;
    /**
     * consul username
     */
    @Parameter(property = "consul.username")
    private String username;

    public URL getUrl() {
        return url;
    }

    public void setUrl(URL url) {
        this.url = url;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    @Override
    public String toString() {
        return "Connection{" +
                "url=" + url +
                ", password='" + password + '\'' +
                ", username='" + username + '\'' +
                '}';
    }
}
