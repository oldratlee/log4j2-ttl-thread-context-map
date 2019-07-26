# 🌳 Log4j2 TTL ThreadContextMap 🌳

[![Build Status](https://travis-ci.org/oldratlee/log4j2-ttl-thread-context-map.svg?branch=master)](https://travis-ci.org/oldratlee/log4j2-ttl-thread-context-map)
[![Coverage Status](https://img.shields.io/codecov/c/github/oldratlee/log4j2-ttl-thread-context-map/master.svg)](https://codecov.io/gh/oldratlee/log4j2-ttl-thread-context-map/branch/master)
[![Maven Central](https://img.shields.io/maven-central/v/com.alibaba/log4j2-ttl-thread-context-map.svg)](http://search.maven.org/#search%7Cgav%7C1%7Cg%3A%22com.alibaba%22%20AND%20a%3A%22log4j2-ttl-thread-context-map%22)
[![GitHub release](https://img.shields.io/github/release/oldratlee/log4j2-ttl-thread-context-map.svg)](https://github.com/oldratlee/log4j2-ttl-thread-context-map/releases)
[![GitHub issues](https://img.shields.io/github/issues/oldratlee/log4j2-ttl-thread-context-map.svg)](https://github.com/oldratlee/log4j2-ttl-thread-context-map/issues)
[![License](https://img.shields.io/badge/license-Apache%202-4EB1BA.svg)](https://www.apache.org/licenses/LICENSE-2.0.html)

--------------------------

<!-- START doctoc generated TOC please keep comment here to allow auto update -->
<!-- DON'T EDIT THIS SECTION, INSTEAD RE-RUN doctoc TO UPDATE -->


- [🔧 Functions](#-functions)
- [👥 Usage](#-usage)
- [🏃 Run Demo](#-run-demo)
- [🍪 Dependency](#-dependency)
- [📚 Related resources](#-related-resources)

<!-- END doctoc generated TOC please keep comment here to allow auto update -->

--------------------------

# 🔧 Functions

👉 Enable the transmitting `Log4j2` `ThreadContext`(`ThreadLocal` value) between threads even using thread pool like components by [Transmittable ThreadLocal(`TTL`)](https://github.com/alibaba/transmittable-thread-local).

Tested and support all `log4j2` version(`2.0` ~ `2.12`) and `java` version 6 ~ 12.

# 👥 Usage

Just add this dependency into your project to activate the Log4j2 TTL ThreadContextMap. ✨

# 🏃 Run Demo

Run Demo Code

```bash
./mvnw clean test-compile -Dexec.classpathScope=test -Dexec.mainClass=com.alibaba.ttl.log4j2.Log4j2Demo exec:java

./mvnw clean test-compile -Dexec.classpathScope=test -Dexec.mainClass=com.alibaba.ttl.log4j2.Slf4jMdcDemo exec:java
```

- [Log4j2Demo.java](src/test/java/com/alibaba/ttl/log4j2/Log4j2Demo.java)
- [Slf4jMdcDemo.java](src/test/java/com/alibaba/ttl/log4j2/Slf4jMdcDemo.java)
- [`TtlThreadContextMap` implementation class: TtlThreadContextMap.java](src/main/java/com/alibaba/ttl/log4j2/TtlThreadContextMap.java).

# 🍪 Dependency

```xml
<!--
    log4j2 runtime extension is SPI implementation,
    it will never be used by biz code, so set scope to runtime. 
-->
<dependency>
    <groupId>com.alibaba</groupId>
    <artifactId>log4j2-ttl-thread-context-map</artifactId>
    <version>1.3.0</version>
    <scope>runtime</scope>
</dependency>
```

Find available versions at [search.maven.org](http://search.maven.org/#search%7Cgav%7C1%7Cg%3A%22com.alibaba%22%20AND%20a%3A%22log4j2-ttl-thread-context-map%22).

# 📚 Related resources

- log4j2 documentation
    - [log4j 2 Thread Context](https://logging.apache.org/log4j/2.x/manual/thread-context.html)
    - [Changelog](https://logging.apache.org/log4j/2.x/changelog.html)
- [Mapped Diagnostic Context (MDC) support - slf4j official documentation](https://www.slf4j.org/manual.html#mdc)
- [Transmittable ThreadLocal(TTL)](https://github.com/alibaba/transmittable-thread-local), 📌 The missing std Java™ lib(simple & 0-dependency) for framework/middleware, transmitting ThreadLocal value between threads even using thread pool like components.
