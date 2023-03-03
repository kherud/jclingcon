![Clingo Version 5.6.2](https://img.shields.io/badge/clingo-5.6.2-informational)
![ClingCon Version 5.2.0](https://img.shields.io/badge/clingcon-5.2.0-informational)

# Java Bindings for ClingCon: An extension of Clingo to handle constraints over integers

**This non-official repository contains Java bindings for Clingcon.**

Clingcon is an answer set solver for constraint logic programs building upon
the answer set solver [clingo]. It extends the high-level modeling language of
ASP with constraints over finite domain integer variables.

The latest source release is available under the [releases][release] tab.
Binary packages can be installed using on of the following package managers:

This Java binding makes use of [JClingo](https://github.com/kherud/jclingo).

# Installation

First, make sure to install the Clingo shared library (see the [official Clingo repo](https://github.com/potassco/clingo/blob/master/INSTALL.md)).
- linux: libclingo.so (pre-compiled x86-64 provided)
- macos: libclingo.dylib (pre-compiled arm64 provided)
- windows: clingo.dll

Second, install the ClingCon shared library (see [official ClingCon repo](https://github.com/potassco/clingcon)).
- linux: libclingcon.so (pre-compiled x86-64 provided)
- macos: libclingcon.dylib (pre-compiled arm64 provided)
- windows: clingcon.dll

You can then use this API via Maven:

```
<dependencies>
    <dependency>
        <groupId>org.potassco</groupId>
        <artifactId>clingcon</artifactId>
        <version>1.0-des-rc1</version>
    </dependency>
</dependencies>

<repositories>
    <repository>
        <id>des-releases-public</id>
        <name>denkbares Public Releases Repository</name>
        <url>https://repo.denkbares.com/releases-public/</url>
    </repository>
</repositories>
```

[clingo]: https://potassco.org/clingo/
[Potassco]: https://potassco.org/
[release]: https://github.com/potassco/clingcon/releases/