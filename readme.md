[![Release](https://jitpack.io/v/dilbertside/commons-dbs.svg)](https://jitpack.io/#dilbertside/commons-dbs)
[![Build Status](https://travis-ci.org/dilbertside/commons-dbs.svg)](https://travis-ci.org/dilbertside/commons-dbs)

Compendium of functions, routines, utils used in my projects based on Spring Framework

## Dependencies

This library has some hard dependencies with
 
. Google Guava
. Apache commons-lang3 and commons-io, commons-codec
. Fasterxml Jackson
. Spring Context and Web


## Maven how to use

### Prerequisites

#### Minimum version

Maven 3.5.x https://maven.apache.org/

Java 1.8

### POM

To use it in your Maven build add:

#### Repository

```xml
<repositories>
  <repository>
      <id>jitpack.io</id>
      <url>https://jitpack.io</url>
  </repository>
</repositories>
```

#### Maven settings.xml

Modify in your ~/.m2/settings.xml along those lines, or download following template [settings.xml](resources/settings.xml) if none exists currently.

[Maven Settings Reference](https://maven.apache.org/settings.html#Repositories)

#### dependency to add to project:


```xml
  <dependency>
    <groupId>com.github.dilbertside</groupId>
    <artifactId>commons-dbs</artifactId>
    <version>1.0.3</version>
  </dependency>
```

# License

MIT https://opensource.org/licenses/MIT
