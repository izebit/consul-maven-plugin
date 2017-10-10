package com.fasten.infra;

import org.apache.commons.lang3.StringUtils;

import java.io.IOException;
import java.nio.file.FileVisitResult;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.AbstractMap.SimpleEntry;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Map.Entry;
import java.util.stream.Stream;

/**
 * @author <a href="izebit@gmail.com">Artem Konovalov</a> <br/>
 * Date: 09/10/2017/.
 */
public class PropertyFileHandler {
    private static final String ALLOW_EXTENSION = ".properties";
    private static final String DELIMITER = "=";

    private final ConsulExporter exporter;
    private final Path path;


    private PropertyFileHandler(ConsulExporter exporter, Path path) {
        this.exporter = exporter;
        this.path = path;
    }

    static Collection<PropertyFileHandler> getHandlers(Path path, ConsulExporter exporter) throws IOException {
        Collection<PropertyFileHandler> handlers = new ArrayList<>();

        Files.walkFileTree(path, new SimpleFileVisitor<Path>() {
            @Override
            public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
                if (file.getFileName().toString().trim().endsWith(ALLOW_EXTENSION)) {
                    handlers.add(new PropertyFileHandler(exporter, file));
                }
                return super.visitFile(file, attrs);
            }
        });

        return handlers;
    }

    public void handle() throws IOException {
        try (Stream<String> stream = Files.lines(path)) {
            stream
                    .filter(PropertyFileHandler::isValid)
                    .map(PropertyFileHandler::split)
                    .forEach(exporter::export);
        }
    }

    private static boolean isValid(String token) {
        return StringUtils.isNotBlank(token) && !token.trim().startsWith("#");
    }

    private static Entry<String, String> split(String token) {
        int index = token.indexOf(DELIMITER);
        if (index == -1) {
            return null;
        }
        String key = token.substring(0, index).trim();
        String value = token.substring(index + 1).trim();
        return new SimpleEntry<>(key, value);
    }

    @Override
    public String toString() {
        return "PropertyFileHandler{" +
                "path=" + path +
                '}';
    }
}
