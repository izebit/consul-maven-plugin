package com.fasten.infra;

import com.orbitz.consul.Consul;
import org.apache.commons.lang3.StringUtils;
import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugins.annotations.LifecyclePhase;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;
import org.apache.maven.project.MavenProject;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Collection;
import java.util.List;

/**
 * @author <a href="izebit@gmail.com">Artem Konovalov</a> <br/>
 * Date: 08/10/2017/.
 */

@Mojo(name = "export",
        defaultPhase = LifecyclePhase.PROCESS_SOURCES,
        threadSafe = true)
public class ExportMojo extends AbstractMojo {
    @Parameter(property = "connection", required = true)
    private Connection connection;
    /**
     * prefix for exported properties in consul
     */
    @Parameter(property = "prefix", defaultValue = "")
    private String prefix;
    /**
     * list of sources with contain properties for export
     */
    @Parameter(property = "sources", required = true)
    private List<File> sources;

    @Parameter(defaultValue = "${project}", readonly = true, required = true)
    private MavenProject project;


    public void execute() throws MojoExecutionException {
        getLog().info("connect to consul with address: " + connection.getUrl());

        Consul.Builder builder = Consul
                .builder()
                .withUrl(connection.getUrl());
        if (StringUtils.isNotBlank(connection.getUsername())) {
            getLog().info("use authentication for export");
            builder.withBasicAuth(connection.getUsername(), connection.getPassword());
        }

        Consul consul = builder.build();
        ConsulExporter exporter = new ConsulExporter(consul.keyValueClient(), prefix);

        Path projectDirectory = Paths.get(project.getBasedir().toURI());
        for (File source : sources) {
            Path sourceFullPath = projectDirectory.resolve(Paths.get(source.toURI()));
            getLog().info("scanning " + sourceFullPath);
            try {
                Collection<PropertyFileHandler> handlers = PropertyFileHandler.getHandlers(sourceFullPath, exporter);
                handlers.forEach(handler -> {
                    try {
                        handler.handle();
                        getLog().debug("handler: " + handler);
                    } catch (IOException e) {
                        getLog().error("something wrong happened while working handler: " + handler, e);
                    }
                });
            } catch (IOException e) {
                getLog().error(e);
            }
        }


        consul.destroy();
        getLog().info("export properties to consul has been done");
    }
}
