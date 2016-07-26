TTL ThreadContextMap For Log4j2 
===================================

[![Maven Central](https://maven-badges.herokuapp.com/maven-central/com.alibaba/log4j2-ttl-thread-context-map/badge.svg)](https://maven-badges.herokuapp.com/maven-central/com.alibaba/log4j2-ttl-thread-context-map/)
[![GitHub release](https://img.shields.io/github/release/oldratlee/log4j2-ttl-thread-context-map.svg)](https://github.com/oldratlee/log4j2-ttl-thread-context-map/releases)
[![Dependency Status](https://www.versioneye.com/user/projects/5796ba8e4fe9180028717750/badge.svg)](https://www.versioneye.com/user/projects/5796ba8e4fe9180028717750)  
[![GitHub issues](https://img.shields.io/github/issues/oldratlee/log4j2-ttl-thread-context-map.svg)](https://github.com/oldratlee/log4j2-ttl-thread-context-map/issues)
[![License](https://img.shields.io/badge/license-Apache%202-4EB1BA.svg)](https://www.apache.org/licenses/LICENSE-2.0.html)

- [`TtlThreadContextMap` Implementation](src/main/java/com/alibaba/ttl/log4j2/TtlThreadContextMap.java).  
    - [Transmittable ThreadLocal(TTL)](https://github.com/alibaba/transmittable-thread-local), a simple 0-dependency java lib for transmitting ThreadLocal value between threads even using thread pool.
- [Demo Code](src/test/java/com/alibaba/ttl/log4j2/TtlThreadContextMapTest.java).

Run Demo:

```bash
mvn clean test
```

Dependency
--------------------------

```xml
<dependency>
    <groupId>com.alibaba</groupId>
    <artifactId>log4j2-ttl-thread-context-map</artifactId>
    <version>1.1.0</version>
</dependency>
```

Find available versions at [search.maven.org](http://search.maven.org/#search%7Cga%7C1%7Cg%3A%22com.alibaba%22%20AND%20a%3A%22log4j2-ttl-thread-context-map%22).
