package de.unirostock.sems.xmlutils.tools;

import java.io.ByteArrayInputStream;
import java.io.InputStream;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;

import org.w3c.dom.Document;

import de.binfalse.bflog.LOGGER;
import de.binfalse.bfutils.SimpleOutputStream;
import de.unirostock.sems.xmlutils.ds.DocumentNode;
import de.unirostock.sems.xmlutils.ds.TreeDocument;


/**
 * Toolkit for Documents.
 *
 * @author Martin Scharm
 */
public class DocumentTools
{
	
	/** The transformer to convert content mathml to presentation mathml. */
	private static Transformer mathTransformer;
	
	/**
	 * Extracts the document from a given TreeDocument.
	 *
	 * @param treeDoc the tree document
	 * @return the document
	 */
	public static Document getDoc (TreeDocument treeDoc)
	{
		return getSubDoc (treeDoc.getRoot ());
	}

	/**
	 * Computes the document oft a subtree.
	 *
	 * @param node the node rooting the subtree
	 * @return the document representing the subtree
	 */
	public static Document getSubDoc (DocumentNode node)
	{
		try 
		{
			DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
			DocumentBuilder docBuilder = docFactory.newDocumentBuilder();
			
			Document d = docBuilder.newDocument ();
			node.getSubDoc (d, null);
			return d;
		}
		catch (Exception e)
		{
			LOGGER.error ("error creating subdoc", e);
			return null;
		}
	}
	

	/**
	 * Prints the sub doc.
	 *
	 * @param node the node
	 * @return the string
	 */
	public static String printSubDoc (DocumentNode node)
	{
		try 
		{
			DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
			DocumentBuilder docBuilder = docFactory.newDocumentBuilder();
			
			Document d = docBuilder.newDocument ();
			node.getSubDoc (d, null);
			return XmlTools.printDocument (d);
		}
		catch (Exception e)
		{
			LOGGER.error ("error creating subdoc", e);
			return "error creating doc: " + e.getMessage ();
		}
	}
	
	/**
	 * Prints the pretty sub doc.
	 *
	 * @param node the node
	 * @return the string
	 */
	public static String printPrettySubDoc (DocumentNode node)
	{
		try 
		{
			DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
			DocumentBuilder docBuilder = docFactory.newDocumentBuilder();
			
			Document d = docBuilder.newDocument ();
			node.getSubDoc (d, null);
			return XmlTools.prettyPrintDocument (d, new SimpleOutputStream ()).toString ();
		}
		catch (Exception e)
		{
			LOGGER.error ("error creating subdoc", e);
			return "error creating doc: " + e.getMessage ();
		}
	}
	
  /**
   * Transform content MathML to display MathML, e.g. to display the MathML in a browser.
   *
   * @param doc the document node rooting the MathML subtree
   * @return the string
   * @throws TransformerException the transformer exception
   */
  public static String transformMathML (DocumentNode doc) throws TransformerException
  {
  	if (mathTransformer == null)
  	{
			TransformerFactory tFactory = TransformerFactory.newInstance();
			
			InputStream input = DocumentTools.class.getResourceAsStream("/res/mmlctop2_0.xsl");
			mathTransformer = tFactory.newTransformer (new StreamSource(input));
  	}
  	
    SimpleOutputStream out = new SimpleOutputStream ();
    String math = printSubDoc (doc);
    // xslt cannot namespace
    math = math.replaceAll ("\\S+:\\S+\\s*=\\s*\"[^\"]*\"", "");
    
    mathTransformer.transform (new StreamSource(new ByteArrayInputStream(math.getBytes())), new StreamResult(out));
		return out.toString ();
  }
	
}