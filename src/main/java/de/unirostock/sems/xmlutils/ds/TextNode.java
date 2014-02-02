/**
 * 
 */
package de.unirostock.sems.xmlutils.ds;

import java.util.HashMap;
import java.util.Vector;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

import de.binfalse.bflog.LOGGER;
import de.binfalse.bfutils.GeneralTools;
import de.unirostock.sems.xmlutils.alg.Weighter;
import de.unirostock.sems.xmlutils.comparison.Connection;
import de.unirostock.sems.xmlutils.comparison.ConnectionManager;


/**
 * @author Martin Scharm
 *
 */
public class TextNode
	extends TreeNode
{
	/** The hash. */
	private String ownHash;
	private String text;
	
	private double weight;
	private Weighter weighter;
	
	private TreeDocument doc;
	
	public TextNode (TextNode toCopy)
	{
		super (TreeNode.TEXT_NODE, null, null, 0);
		this.text = toCopy.text;
		// create xpath
		xPath = null;
		ownHash = toCopy.ownHash;
		weight = toCopy.weight;
		weighter = toCopy.weighter;
	}
	
	public TextNode (String text, DocumentNode parent, TreeDocument doc, int numChild, Weighter w, int level)//, NodeMapper<TreeNode> pathMapper, MultiNodeMapper<TreeNode> hashMapper, MultiNodeMapper<DocumentNode> tagMapper, Vector<TreeNode> subtreesBySize)
	{
		super (TreeNode.TEXT_NODE, parent, doc, level);
		this.text = text;//element.getNodeValue ();
		// create xpath
		if (parent == null)
			xPath = "";
		else
			xPath = parent.getXPath ();
		xPath += "/"+TEXT_TAG+"[" + numChild + "]";
		//pathMapper.putNode (xPath, this);
		//tagMapper.addNode ("text()", this);
		
		ownHash = GeneralTools.hash (text);
		
		//hashMapper.addNode (ownHash, this);
		weighter = w;
		weight = w.getWeight (this);
		
		//subtreesBySize.add (this);
		doc.integrate (this);
	}
	


	@Override
	public boolean evaluate (ConnectionManager conMgmr)
	{
		LOGGER.debug ("evaluate " + xPath);
		
		setModification (UNCHANGED);
		
		Connection con = conMgmr.getConnectionForNode (this);
		if (con == null)
		{
			addModification (UNMAPPED | SUBTREEUNMAPPED);
			return true;
		}
		
		TreeNode partner = con.getPartnerOf (this);
		
		// changed?
		if (contentDiffers (partner))
			addModification (MODIFIED);
		// moved?
		if (networkDiffers (partner, conMgmr, con))
		{
			addModification (MOVED);
		}
		
		
		/*Vector<Connection> cons = conMgmr.getConnectionsForNode (this);
		if (cons == null || cons.size () == 0)
		{
			addModification (UNMAPPED);
			return true;
		}
		
		if (cons.size () == 1)
		{
			Connection c = cons.elementAt (0);
			TreeNode partner = c.getPartnerOf (this);
			
			// changed?
			if (contentDiffers (partner))
				addModification (MODIFIED);
			
			// mapped, glued?
			// must have a connection
			if (conMgmr.getConnectionsForNode (partner).size () > 1)
			{
				addModification (GLUED);
			}
			// moved?
			if (networkDiffers (partner, conMgmr, c))
			{
				addModification (MOVED);
			}
		}
		if (cons.size () > 1)
		{
			// check if each of them has only 1 connection, otherwise there's smth wrong
			for (Connection c : cons)
			{
				TreeNode partner = c.getPartnerOf (this);
				if (conMgmr.getConnectionsForNode (partner).size () != 1)
					throw new UnsupportedOperationException ("moved and glued!?");
				if ((modified & MODIFIED) == 0 && contentDiffers (partner))
					addModification (MODIFIED);
			}
			addModification (MOVED);
			addModification (COPIED);
		}*/

		LOGGER.debug ("mod: " + modified);
		return (modified & (MODIFIED | MOVED | UNMAPPED)) != 0;
	}
	
	protected boolean contentDiffers (TreeNode tn)
	{
		if (tn.type != type)
			return true;
		if (!text.equals (((TextNode)tn).text))
			return true;
		return false;
	}

	@Override
	public String dump (String prefix)
	{
		return prefix + xPath + " -> " + modified + "\n";
	}

	@Override
	public void getSubDoc (Document doc, Element parent)
	{
		parent.appendChild (doc.createTextNode (text));
	}

	@Override
	protected void reSetupStructureDown (TreeDocument doc, int numChild)
	{
		if (this.doc != null)
			this.doc.separate (this);
		this.doc = doc;
		this.xPath = parent.xPath + "/" + TEXT_TAG + "[" + numChild + "]";
		this.level = parent.level + 1;
		
		this.doc.integrate (this);
	}

	@Override
	protected void reSetupStructureUp ()
	{
		this.doc.separate (this);
		ownHash = GeneralTools.hash (text);
		this.doc.integrate (this);

		weight = weighter.getWeight (this);
		
		if (parent != null)
			parent.reSetupStructureUp ();
		
	}

	@Override
	public void getNodeStats (HashMap<String, Integer> map)
	{
		Integer i = map.get (TEXT_TAG);
		if (i == null)
			map.put (TEXT_TAG, 1);
		else
			map.put (TEXT_TAG, i + 1);
	}

	@Override
	public double getWeight ()
	{
		return weight;
	}

	@Override
	public String getOwnHash ()
	{
		return ownHash;
	}
	
	public String getText ()
	{
		return text;
	}
	
	@Override
	public String getSubTreeHash ()
	{
		return getOwnHash ();
	}
	
	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	public String toString ()
	{
		return "TEXT: " + weight + "\t(" + xPath + ")\t" + text;
	}
}
