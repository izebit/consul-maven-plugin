package ru.izebit.mvn.plugin;

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
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Collection;
import java.util.List;
import java.util.Objects;

/**
 * @author <a href="izebit@gmail.com">Artem Konovalov</a> <br/>
 * Date: 08/10/2017/.
 */

@Mojo(name = "run",
        defaultPhase = LifecyclePhase.PROCESS_RESOURCES,
        threadSafe = true)
public class ExportConsulMojo extends AbstractMojo {
    /**
     * url for connecting to consul
     */
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
        getLog().info("start export properties to consul");
        getLog().info("connect to consul with address: " + connection.getUrl());

        Consul.Builder builder = Consul
                .builder()
                .withUrl(connection.getUrl());
        if (StringUtils.isNotBlank(connection.getUsername())) {
            getLog().info("use authentication for export");
            builder.withBasicAuth(connection.getUsername(), connection.getPassword());
        }

        Consul consul = builder.build();
        ConsulExporter exporter = new ConsulExporter(consul.keyValueClient(), prefix, getLog());

        Path projectDirectory = Paths.get(project.getBasedir().toURI());
        for (File source : sources) {
            Path sourceFullPath = projectDirectory.resolve(Paths.get(source.toURI()));
            getLog().info("scanning " + sourceFullPath);
            if (!Files.exists(sourceFullPath)) {
                getLog().warn("the source does't exist: " + sourceFullPath);
                continue;
            }
            try {
                Collection<PropertyFileHandler> handlers = PropertyFileHandler.getHandlers(sourceFullPath, exporter);
                handlers.forEach(handler -> {
                    try {
                        handler.handle();
                        if (getLog().isDebugEnabled())
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
