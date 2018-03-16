# Consul maven plugin
[![Apache License, Version 2.0, January 2004][shield-license]][apache-license]


# Overview
Maven plugin for exporting properties from files and directories to [consul](https://www.consul.io/) server 🎲


## License
[Apache License, Version 2.0, January 2004](http://www.apache.org/licenses/)

## Usage

There are only three steps

### Add the plugin to your pom.xml:

```xml
<plugin>
    <groupId>ru.izebit</groupId>
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
### Create a file of properties with the next structure:

```properties
first-key = first-value
#it is a comment
second-key =
third-key = #third value
```

### Run a goal to export properties from file:
```
ru.izebit:consul-maven-plugin:export 
```

[apache-license]: https://www.apache.org/licenses/
[shield-license]: https://img.shields.io/github/license/khmarbaise/echo-maven-plugin.svg?label=License

