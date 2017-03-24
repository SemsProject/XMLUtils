XML Utils 
==========

a library to ease the daily work on XML documents. This library is used by [BiVeS](https://semsproject.github.io/BiVeS/), [BiVeS-Core](https://semsproject.github.io./BiVeS-Core/), [BiVeS-CellML](https://semsproject.github.io./BiVeS-CellML), [BiVeS-SBML](https://semsproject.github.io./BiVeS-SBML/).

Features 
---------

* pretty print, see [how to about printing](HowTo#printing)
* hash sums => identification
* entails comparison, see bives:wiki

Usage 
------

* [download a binary](http://bin.sems.uni-rostock.de/xmlutils/)
* include XMLUtils via Maven: ([find latest version id](http://mvn.sems.uni-rostock.de/releases/de/unirostock/sems/xmlutils/), import the [SEMS Maven repository](https://sems.uni-rostock.de/2013/10/maven-repository/))

```xml
<dependency>
    <groupId>de.unirostock.sems</groupId>
    <artifactId>xmlutils</artifactId>
    <version>$VERSION</version>
</dependency>
```

* learn /HowTo use the library

Build 
------

* learn how to [build XmlUtils](BuildXmlUtils)

Dev 
----
* see the sources of this package: /src/main/java/de/unirostock/sems/xmlutils 
* see the [JavaDoc](http://jdoc.sems.uni-rostock.de/xmlutils/)
* clone the sources:

```sh
git clone git@github.com:SemsProject/XMLUtils.git
```

* find open [bugs/issues and requests](https://github.com/SemsProject/XMLUtils/issues)
* [file an issue or feature request](https://github.com/SemsProject/XMLUtils/issues/new)
* discover latest changes: [ChangeLog](ChangeLog)
