How To Use XML Utils 
=====================
Read An XML Document 
---------------------
read an XML document and create a [TreeDocument](/src/main/java/de/unirostock/sems/xmlutils/ds//TreeDocument.java):
```
#!java
// provide a file
File file = new File ("/path/to/doc");
TreeDocument document = new TreeDocument (XmlTools.readDocument (file), file.toURI ());
// or a string containing the XML code
TreeDocument document = new TreeDocument (XmlTools.readDocument ("<xml> [...] </xml>"), null);
```
The second parameter of the constructor is an URI, which will be used to resolve relative links to resources defined from within the document.

Object Structure 
-----------------
### !TreeDocument 
The main object you'll deal with is the [TreeDocument](/src/main/java/de/unirostock/sems/xmlutils/ds//TreeDocument.java), see [Java Doc](http://jdoc.sems.uni-rostock.de/xmlutils/de/unirostock/sems/xmlutils/ds///TreeDocument.html)

The !TreeDocument maintains several maps to easily access certain nodes in the tree. To get the keys and the corresponding nodes you can use the following methods

 Identifiers::
   use [get/OccuringIds](http://jdoc.sems.uni-rostock.de/xmlutils/de/unirostock/sems/xmlutils/ds///TreeDocument.html//#getOccurringIds()) to get all known ids in the document and [get/NodeById](http://jdoc.sems.uni-rostock.de/xmlutils/de/unirostock/sems/xmlutils/ds///TreeDocument.html//#getNodeById(java.lang.String)) to get the node having a certain id. This is **only available if the ids of the document are unique**. Use [uniqueIds](http://jdoc.sems.uni-rostock.de/xmlutils/de/unirostock/sems/xmlutils/ds///TreeDocument.html//#uniqueIds()) to see whether the ids are unique.
 XPaths::
   use [getOccurringXPaths](http://jdoc.sems.uni-rostock.de/xmlutils/de/unirostock/sems/xmlutils/ds///TreeDocument.html//#getOccurringXPaths()) to get all known XPaths in the document and [get/NodeByPath](http://jdoc.sems.uni-rostock.de/xmlutils/de/unirostock/sems/xmlutils/ds///TreeDocument.html//#getNodeByPath(java.lang.String)) to get the node having a certain XPath. Currently, we just support XPaths produced by us (it's a simple map). [Learn more about XPaths](https://en.wikipedia.org/wiki/XPath).
 Tag Names::
   use [get/OccurringTags](http://jdoc.sems.uni-rostock.de/xmlutils/de/unirostock/sems/xmlutils/ds///TreeDocument.html//#getOccurringTags()) to get all known tag names in the document and [get/NodesByTag](http://jdoc.sems.uni-rostock.de/xmlutils/de/unirostock/sems/xmlutils/ds///TreeDocument.html//#getNodesByTag(java.lang.String)) to get all nodes sharing a certain tag name.
 Hashes::
   use [get/OccurringHashes](http://jdoc.sems.uni-rostock.de/xmlutils/de/unirostock/sems/xmlutils/ds///TreeDocument.html//#getOccurringHashes()) to get all known hashes in the document and [get/NodesByHash](http://jdoc.sems.uni-rostock.de/xmlutils/de/unirostock/sems/xmlutils/ds///TreeDocument.html//#getNodesByHash(java.lang.String)) to get all nodes sharing a certain hash. See [None](#//NodeHashes) for further informations on hashes.

Small example to see the usage of !TreeDocuments:

```
#!java
File file = new File ("/path/to/doc");
TreeDocument document = new TreeDocument (XmlTools.readDocument (file), file.toURI ());
System.out.println ("There are " + document.getNumNodes () + " nodes in this tree");

// get all subtrees (i.e. nodes rooting these trees) ordered by size, biggest first:
TreeNode[] subTrees = document.getSubtreesBySize ();

// get the root of the tree
TreeNode root = document.getRoot ();

// get the node with id="sems"
// and print the number of nodes below this node
DocumentNode semsNode = document.getNodeById ("sems");
System.out.println ("#nodes below sems-node: " + semsNode.getSizeSubtree ());

// get the first node having a tag name of "example"
// and print the level of its parent
DocumentNode node = document.getNodesByTag ("example").get (0);
System.out.println ("level of parent of first <example> node: " + node.getParent ().getLevel ());

// compare two trees
TreeDocument document2 = new TreeDocument (XmlTools.readDocument (new File ("/path/to/doc")), null);
document2.equals (document); // same document -> true

TreeDocument document3 = new TreeDocument (XmlTools.readDocument (new File ("/path/to/other/doc")), null);
document3.equals (document); // different document -> most likely false

```

Find the full example in /src/main/java/de/unirostock/sems/xmlutils/eg/TreeUsageExample.java 

The objects housed in a tree document are of type [None](#//TreeNode).

### !TreeNode 
A [TreeNode](/src/main/java/de/unirostock/sems/xmlutils/ds//TreeNode.java) ([JavaDoc](http://jdoc.sems.uni-rostock.de/xmlutils/de/unirostock/sems/xmlutils/ds///TreeNode.html)) represents a node in a document. There are two different types of nodes, [TextNode](https://sems.uni-rostock.de/trac/xmlutils/browser/src/main/java/de/unirostock/sems/xmlutils/ds///TextNode.java)s ([JavaDoc](http://jdoc.sems.uni-rostock.de/xmlutils/de/unirostock/sems/xmlutils/ds///TextNode.html)) represents textual content in documents and [DocumentNode](/src/main/java/de/unirostock/sems/xmlutils/ds//DocumentNode.java)s ([JavaDoc](http://jdoc.sems.uni-rostock.de/xmlutils/de/unirostock/sems/xmlutils/ds///DocumentNode.html)) represent XML nodes. These classes define some getters and setters, just have a look the the corresponding java doc. However, here are some more details for uncommon usecases:

#### Node Hashes 
Each node in the document has two hash values unique for this node and it's subtree (note: not necessarily unique in the document). You can access this hash values using [get/OwnHash](http://jdoc.sems.uni-rostock.de/xmlutils/de/unirostock/sems/xmlutils/ds///TreeNode.html//#getOwnHash()) and [get/SubTreeHash](http://jdoc.sems.uni-rostock.de/xmlutils/de/unirostock/sems/xmlutils/ds///TreeNode.html//#getSubTreeHash()). The !OwnHash is an identifier for the !TreeNode itself, thus ```<node>` and `<node>` have the same !OwnHash, but the !OwnHash of `<node attr='value'>``` is different. In a similar fashion the !Sub/TreeHash identifies a subtree rooted in the corresponding node.

#### Node Weights 
The nodes in the document have weights depending on their subtrees. That is, weight ~ size subtree.
The objects that computes the weight of a node can be defined when creating the tree document (see [extra constructor](http://jdoc.sems.uni-rostock.de/xmlutils/de/unirostock/sems/xmlutils/ds///TreeDocument.html/#/TreeDocument(org.w3c.dom.Document,%20de.unirostock.sems.xmlutils.alg.Weighter,%20java.net.URI))). It needs to extend the [Weighter](/src/main/java/de/unirostock/sems/xmlutils/alg/Weighter.java) class ([JavaDoc](http://jdoc.sems.uni-rostock.de/xmlutils/de/unirostock/sems/xmlutils/alg/Weighter.html)) and defaults to the [SemsWeighter](/src/main/java/de/unirostock/sems/xmlutils/alg//SemsWeighter.java) ([JavaDoc](http://jdoc.sems.uni-rostock.de/xmlutils/de/unirostock/sems/xmlutils/alg///SemsWeighter.html)).

See a small example to get an idea of nodes and their usage:
```
#!java
// get root node
DocumentNode root = document.getRoot ();
// root's children
Vector<TreeNode> firstLevel = root.getChildren ();
System.out.println ("There are " + firstLevel.size () + " children in " + root.getXPath () + " :");
for (TreeNode kid : firstLevel)
	System.out.println ("\t" + kid.getXPath () + " having " + ((DocumentNode) kid).getNumLeaves () + " leaves and a weight of " + kid.getWeight ());

// get first message node
DocumentNode message = document.getNodeById ("messageone");
// you can also get access to the same node using it's path:
TreeNode sameNode = document.getNodeByPath (message.getXPath ());
// let's test if it's really the same:
System.out.println ("found same node by id and by XPath? " + (sameNode == message));
// you can also get this node by it's signature (here i know it's the first node having this hash value)
sameNode = document.getNodesByHash (message.getSubTreeHash ()).get (0);
// test:
System.out.println ("found same node by id and by hash? " + (sameNode == message));

// let's print some information about this node
System.out.println ("Path to the message node: " + message.getXPath ());
System.out.println ("Path to its parent: " + message.getParent ().getXPath ());
System.out.println ("Weight of the message node: " + message.getWeight ());
System.out.println ("Signature of the message node: " + message.getOwnHash ());
System.out.println ("Signature of the subtree rooted in the message node: " + message.getSubTreeHash ());
System.out.println ("#number nodes in its subtree: " + (message.getSizeSubtree () + 1));
System.out.println ("number of direct children: " + message.getNumChildren ());
System.out.println ("id of the node: " + message.getId ());
System.out.println ("tag name: " + message.getTagName ());
System.out.println ("attributes in this node:");
for (String attr : message.getAttributes ())
	System.out.println ("\t" + attr + " => " + message.getAttribute (attr));


// remove the node from the tree
DocumentNode parent = message.getParent ();
parent.rmChild (message);
// and reinsert it
parent.addChild (message);

// note how the path ot the node has changed
System.out.println ("New path to the message node: " + message.getXPath ());
// but everything else is still the same
System.out.println ("Weight of the message node: " + message.getWeight ());
System.out.println ("Signature of the subtree rooted in the message node: " + message.getSubTreeHash ());
```
Find the full example in /src/main/java/de/unirostock/sems/xmlutils/eg/NodeUsageExample.java 

Extra 
------
### Printing 
The class [DocumentTools](/src/main/java/de/unirostock/sems/xmlutils/tools//DocumentTools.java) ([JavaDoc](http://jdoc.sems.uni-rostock.de/xmlutils/de/unirostock/sems/xmlutils/tools///DocumentTools.html)) provides some static functions to print (parts of) document. Just pass the node rooting the tree to print as an argument (e.g. ```document.getRoot ()` to print the whole `document```). Use [print/SubDoc](http://jdoc.sems.uni-rostock.de/xmlutils/de/unirostock/sems/xmlutils/tools///DocumentTools.html//#printSubDoc(de.unirostock.sems.xmlutils.ds.//DocumentNode)) to print a tree and [print/PrettySubDoc](http://jdoc.sems.uni-rostock.de/xmlutils/de/unirostock/sems/xmlutils/tools///DocumentTools.html//#printPrettySubDoc(de.unirostock.sems.xmlutils.ds.//DocumentNode)) to get the tree as pretty string (i.e. intended etc):

```
#!java
String prettyDoc = DocumentTools.printPrettySubDoc (document.getRoot ());
String justOneLineDoc = DocumentTools.printSubDoc (document.getRoot ());
```

### MathML conversion 
The [DocumentTools](/src/main/java/de/unirostock/sems/xmlutils/tools//DocumentTools.java) ([JavaDoc](http://jdoc.sems.uni-rostock.de/xmlutils/de/unirostock/sems/xmlutils/tools///DocumentTools.html)) contain a smart method to convert content MathML to presentation MathML: [transformMathML](http://jdoc.sems.uni-rostock.de/xmlutils/de/unirostock/sems/xmlutils/tools///DocumentTools.html//#transformMathML(de.unirostock.sems.xmlutils.ds.//DocumentNode)). Just pass the !DocumentNode which roots the MathML tree and get a string containing the presentation MathML, e.g.:

```
#!java
String presentationMathML = DocumentTools.transformMathML (contentMathMLFile.getRoot ());
```