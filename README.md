# <div align="center">🌳 Log4j2 TTL ThreadContextMap</div>

<p align="center">
<a href="https://github.com/oldratlee/log4j2-ttl-thread-context-map/actions/workflows/ci.yaml"><img src="https://img.shields.io/github/workflow/status/oldratlee/log4j2-ttl-thread-context-map/CI/master?logo=github&logoColor=white" alt="Github Workflow Build Status"></a>
<a href="https://codecov.io/gh/oldratlee/log4j2-ttl-thread-context-map/branch/master"><img src="https://img.shields.io/codecov/c/github/oldratlee/log4j2-ttl-thread-context-map/master?logo=codecov&logoColor=white" alt="Coverage Status"></a>
<a href="https://openjdk.java.net/"><img src="https://img.shields.io/badge/Java-6+-green?logo=openjdk&logoColor=white" alt="JDK support"></a>
<a href="https://www.apache.org/licenses/LICENSE-2.0.html"><img src="https://img.shields.io/github/license/oldratlee/log4j2-ttl-thread-context-map?color=4D7A97&logo=apache" alt="License"></a>
<a href="https://search.maven.org/artifact/com.alibaba/log4j2-ttl-thread-context-map"><img src="https://img.shields.io/maven-central/v/com.alibaba/log4j2-ttl-thread-context-map?color=2d545e&logo=apache-maven&logoColor=white" alt="Maven Central"></a>
<a href="https://github.com/oldratlee/log4j2-ttl-thread-context-map/releases"><img src="https://img.shields.io/github/release/oldratlee/log4j2-ttl-thread-context-map" alt="GitHub release"></a>
<a href="https://github.com/oldratlee/log4j2-ttl-thread-context-map/stargazers"><img src="https://img.shields.io/github/stars/oldratlee/log4j2-ttl-thread-context-map" alt="GitHub Stars"></a>
<a href="https://github.com/oldratlee/log4j2-ttl-thread-context-map/fork"><img src="https://img.shields.io/github/forks/oldratlee/log4j2-ttl-thread-context-map" alt="GitHub Forks"></a>
<a href="https://github.com/oldratlee/log4j2-ttl-thread-context-map/network/dependents"><img src="https://badgen.net/github/dependents-repo/oldratlee/log4j2-ttl-thread-context-map?label=user%20repos" alt="user repos"></a>
<a href="https://github.com/oldratlee/log4j2-ttl-thread-context-map"><img src="https://img.shields.io/github/repo-size/oldratlee/log4j2-ttl-thread-context-map" alt="GitHub repo size"></a>
<a href="https://gitpod.io/#https://github.com/oldratlee/log4j2-ttl-thread-context-map"><img src="https://img.shields.io/badge/Gitpod-ready--to--code-green?label=gitpod&logo=gitpod&logoColor=white" alt="gitpod: Ready to Code"></a>
</p>

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

👉 Enable the transmitting `Log4j2` `ThreadContext`(`ThreadLocal` value) between threads even using thread pooling components by [Transmittable ThreadLocal(`TTL`)](https://github.com/alibaba/transmittable-thread-local).

Tested and support all `log4j2` version(`2.0` ~ `2.14`) and `java` version 6 ~ 13.

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
    because this dependency is implemented by log4j2 runtime extension
    that will never be used by biz code,
    set scope to *runtime*.
-->
<dependency>
    <groupId>com.alibaba</groupId>
    <artifactId>log4j2-ttl-thread-context-map</artifactId>
    <version>1.3.3</version>
    <scope>runtime</scope>
</dependency>
```

Find available versions at [search.maven.org](http://search.maven.org/#search%7Cgav%7C1%7Cg%3A%22com.alibaba%22%20AND%20a%3A%22log4j2-ttl-thread-context-map%22).

# 📚 Related resources

- log4j2 documentation
    - [log4j 2 Thread Context](https://logging.apache.org/log4j/2.x/manual/thread-context.html)
    - [Changelog](https://logging.apache.org/log4j/2.x/changelog.html)
- [Mapped Diagnostic Context (MDC) support - slf4j official documentation](https://www.slf4j.org/manual.html#mdc)
- [Transmittable ThreadLocal(TTL)](https://github.com/alibaba/transmittable-thread-local), 📌 The missing Java™ std lib(simple & 0-dependency) for framework/middleware, provide an enhanced InheritableThreadLocal that transmits ThreadLocal value between threads even using thread pooling components.
