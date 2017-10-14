# Consul maven plugin
[![Apache License, Version 2.0, January 2004][shield-license]][apache-license]


#Overview
Maven plugin for export properties to consul from files and directories 🎲


## License
[Apache License, Version 2.0, January 2004](http://www.apache.org/licenses/)

## Usage

### add dependency to pom.xml

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
### structure of file with properties

```properties
first-key = first-value
#it is a comment
second-key =
third-key = #third value
```

### running goal
```
ru.izebit:consul-maven-plugin:export 
```
