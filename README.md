# TTL ThreadContextMap For Log4j2

[![Build Status](https://travis-ci.org/oldratlee/log4j2-ttl-thread-context-map.svg?branch=master)](https://travis-ci.org/oldratlee/log4j2-ttl-thread-context-map)
[![Coverage Status](https://img.shields.io/codecov/c/github/oldratlee/log4j2-ttl-thread-context-map/master.svg)](https://codecov.io/gh/oldratlee/log4j2-ttl-thread-context-map/branch/master)
[![Maven Central](https://img.shields.io/maven-central/v/com.alibaba/log4j2-ttl-thread-context-map.svg)](http://search.maven.org/#search%7Cgav%7C1%7Cg%3A%22com.alibaba%22%20AND%20a%3A%22log4j2-ttl-thread-context-map%22)
[![GitHub release](https://img.shields.io/github/release/oldratlee/log4j2-ttl-thread-context-map.svg)](https://github.com/oldratlee/log4j2-ttl-thread-context-map/releases)
[![GitHub issues](https://img.shields.io/github/issues/oldratlee/log4j2-ttl-thread-context-map.svg)](https://github.com/oldratlee/log4j2-ttl-thread-context-map/issues)
[![License](https://img.shields.io/badge/license-Apache%202-4EB1BA.svg)](https://www.apache.org/licenses/LICENSE-2.0.html)

- [`TtlThreadContextMap` Implementation](src/main/java/com/alibaba/ttl/log4j2/TtlThreadContextMap.java).  
    - [Transmittable ThreadLocal(TTL)](https://github.com/alibaba/transmittable-thread-local)  
        📌 The missing std Java™ lib(simple & 0-dependency) for framework/middleware, transmitting ThreadLocal value between threads even using thread pool like components.
- [Demo Code](src/test/java/com/alibaba/ttl/log4j2/Demo.java).

## Run Demo

```bash
./mvnw clean test-compile -Dexec.classpathScope=test -Dexec.mainClass=com.alibaba.ttl.log4j2.Demo exec:java
```

# Dependency

```xml
<dependency>
    <groupId>com.alibaba</groupId>
    <artifactId>log4j2-ttl-thread-context-map</artifactId>
    <version>1.2.0</version>
</dependency>
```

Find available versions at [search.maven.org](http://search.maven.org/#search%7Cga%7C1%7Cg%3A%22com.alibaba%22%20AND%20a%3A%22log4j2-ttl-thread-context-map%22).
