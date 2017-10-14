# consul-maven-plugin

maven plugin for export properties to consul

# how to use it?

```xml
<plugin>
    <groupId>com.fasten.infra</groupId>
    <artifactId>consul-maven-plugin</artifactId>
    <version>1.0-SNAPSHOT</version>
    <configuration>
        <connection>
            <username>username</username>
            <password>qwerty</password>
            <url>http://localhost:8500</url>
        </connection>
        <prefix>app/config/</prefix>
        <sources>
            <source>src/main/resources/</source>
        </sources>
    </configuration>
</plugin>
```

# the goal for running

> ru.izebit:consul-maven-plugin:export 