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
* include XMLUtils via Maven:

```xml
<dependency>
    <groupId>de.uni-rostock.sbi</groupId>
    <artifactId>xmlutils</artifactId>
    <version>$VERSION</version>
</dependency>
```

([find latest version at Maven Central](http://search.maven.org/#search%7Cgav%7C1%7Cg%3A%22de.uni-rostock.sbi%22%20AND%20a%3A%22xmlutils%22))


* learn [how to use the library](HowTo)

Build 
------

* learn how to [build XmlUtils](BuildXmlUtils)

Dev 
----
* see the sources of this package: [`/src/main/java/de/unirostock/sems/xmlutils`](https://github.com/SemsProject/XMLUtils/tree/master/src/main/java/de/unirostock/sems/xmlutils)
* see the [JavaDoc](http://jdoc.sems.uni-rostock.de/xmlutils/)
* clone the sources:

```sh
git clone git@github.com:SemsProject/XMLUtils.git
```

* find open [bugs/issues and requests](https://github.com/SemsProject/XMLUtils/issues)
* [file an issue or feature request](https://github.com/SemsProject/XMLUtils/issues/new)
* discover latest changes: [ChangeLog](ChangeLog)
