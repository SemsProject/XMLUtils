/**
 * 
 */
package de.unirostock.sems.xmlutils.ds;

import java.net.URI;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.PriorityQueue;
import java.util.Set;
import java.util.Vector;

import org.w3c.dom.Document;

import de.unirostock.sems.xmlutils.alg.Weighter;
import de.unirostock.sems.xmlutils.alg.XyWeighter;
import de.unirostock.sems.xmlutils.ds.mappers.MultiNodeMapper;
import de.unirostock.sems.xmlutils.ds.mappers.NodeMapper;
import de.unirostock.sems.xmlutils.exception.XmlDocumentParseException;


/**
 * @author Martin Scharm
 *
 */
public class TreeDocument
{
	private DocumentNode root;
	private NodeMapper<DocumentNode> idMapper;
	private NodeMapper<TreeNode>  pathMapper;
	private MultiNodeMapper<TreeNode> hashMapper;
	private MultiNodeMapper<DocumentNode> tagMapper;
	private List<TextNode> textNodes;
	private boolean ordered;
	private Vector<TreeNode> subtreesBySize;
	private boolean uniqueIds;
	private URI baseUri;

	private void init ()
	{
		pathMapper = new NodeMapper<TreeNode> ();
		idMapper = new NodeMapper<DocumentNode> ();
		hashMapper = new MultiNodeMapper<TreeNode> ();
		tagMapper = new MultiNodeMapper<DocumentNode> ();
		subtreesBySize = new Vector<TreeNode> ();
		textNodes = new ArrayList<TextNode> ();
		
	}
	
	public TreeDocument (Document d, URI baseUri) throws XmlDocumentParseException
	{
		init ();
		Weighter w = new XyWeighter (); // default xy
		root = new DocumentNode (d.getDocumentElement (), null, this, w, 1, 0);//, pathMapper, idMapper, hashMapper, tagMapper, subtreesBySize);
		Collections.sort (subtreesBySize, new TreeNodeComparatorBySubtreeSize ());
		ordered = true;
		uniqueIds = true;
		this.baseUri = baseUri;
	}

	public TreeDocument (Document d, Weighter w, URI baseUri) throws XmlDocumentParseException
	{
		init ();
		if (w == null)
			w = new XyWeighter (); // default xy
		root = new DocumentNode (d.getDocumentElement (), null, this, w, 1, 0);//, pathMapper, idMapper, hashMapper, tagMapper, subtreesBySize);
		Collections.sort (subtreesBySize, new TreeNodeComparatorBySubtreeSize ());
		ordered = true;
		uniqueIds = true;
		this.baseUri = baseUri;
	}
	public TreeDocument (Document d, URI baseUri, boolean ordered) throws XmlDocumentParseException
	{
		init ();
		Weighter w = new XyWeighter (); // default xy
		root = new DocumentNode (d.getDocumentElement (), null, this, w, 1, 0);//, pathMapper, idMapper, hashMapper, tagMapper, subtreesBySize);
		Collections.sort (subtreesBySize, new TreeNodeComparatorBySubtreeSize ());
		this.ordered = ordered;
		uniqueIds = true;
	}
	public TreeDocument (Document d, Weighter w, URI baseUri, boolean ordered) throws XmlDocumentParseException
	{
		init ();
		if (w == null)
			w = new XyWeighter (); // default xy
		root = new DocumentNode (d.getDocumentElement (), null, this, w, 1, 0);//, pathMapper, idMapper, hashMapper, tagMapper, subtreesBySize);
		Collections.sort (subtreesBySize, new TreeNodeComparatorBySubtreeSize ());
		this.ordered = ordered;
		uniqueIds = true;
		this.baseUri = baseUri;
	}
	
	public void resortSubtrees ()
	{
		Collections.sort (subtreesBySize, new TreeNodeComparatorBySubtreeSize ());
	}
	
	public void integrate (TreeNode node)
	{
		pathMapper.putNode (node.getXPath (), node);
		subtreesBySize.add (node);
		if (node.getType () == TreeNode.DOC_NODE)
		{
			DocumentNode dnode = (DocumentNode) node;
			hashMapper.addNode (node.getSubTreeHash (), node);
			tagMapper.addNode (dnode.getTagName (), dnode);
			String id = dnode.getId ();
			if (id != null)
			{
				if (idMapper.getNode (id) != null)
					uniqueIds = false;
				else
					idMapper.putNode (id, dnode);
			}
		}
		else
		{
			hashMapper.addNode (node.getOwnHash (), node);
			textNodes.add ((TextNode) node);
		}
	}
	
	public void separate (TreeNode node)
	{
		pathMapper.rmNode (node.getXPath ());
		subtreesBySize.remove (node);

		if (node.getType () == TreeNode.DOC_NODE)
		{
			DocumentNode dnode = (DocumentNode) node;
			hashMapper.rmNode (dnode.getSubTreeHash (), dnode);
			tagMapper.rmNode (dnode.getTagName (), dnode);
			if (dnode.getId () != null)
				idMapper.rmNode (dnode.getId ());
		}
		else
		{
			hashMapper.rmNode (node.getOwnHash (), node);
			textNodes.remove (node);
		}
	}
	

	public URI getBaseUri ()
	{
		return baseUri;
	}
	
	/**
	 * Are occurring IDs unique?
	 *
	 * @return true, if IDs are unique
	 */
	public boolean uniqueIds ()
	{
		return uniqueIds;
	}
	
	public void setResetAllModifications ()
	{
		root.resetModifications ();
	}
	
	public DocumentNode getRoot ()
	{
		return root;
	}
	
	public int getNumNodes ()
	{
		return root.getSizeSubtree () + 1;
	}
	
	public double getTreeWeight ()
	{
		return root.getWeight ();
	}
	
	public List<TextNode> getTextNodes ()
	{
		return textNodes;
	}
	
	public List<DocumentNode> getNodesByTag (String tag)
	{
		List<DocumentNode> nodes = tagMapper.getNodes (tag);
		if (nodes == null)
			return new Vector<DocumentNode> ();
		return tagMapper.getNodes (tag);
	}
	
	public TreeNode [] getSubtreesBySize ()
	{
		TreeNode [] tmp = new TreeNode [subtreesBySize.size ()];
		subtreesBySize.toArray (tmp);
		return tmp;
	}
	
	public List<TreeNode> getNodesByHash (String hash)
	{
		return hashMapper.getNodes (hash);
	}
	
	public DocumentNode getNodeById (String id)
	{
		if (uniqueIds)
			return idMapper.getNode (id);
		return null;
	}
	
	public TreeNode getNodeByPath (String path)
	{
		return pathMapper.getNode (path);
	}
	
	public Set<String> getOccuringXPaths ()
	{
		return pathMapper.getIds ();
	}
	
	public Set<String> getOccuringIds ()
	{
		if (uniqueIds)
			return idMapper.getIds ();
		return null;
	}
	
	public Set<String> getOccuringTags ()
	{
		return tagMapper.getIds ();
	}
	
	public Set<String> getOccuringHashes ()
	{
		return hashMapper.getIds ();
	}
	
	public String dump ()
	{
		return root.dump ("");
	}
	
	public HashMap<String, Integer> getNodeStats ()
	{
		HashMap<String, Integer> tags = new HashMap<String, Integer> ();
		root.getNodeStats (tags);
		return tags;
	}
	
	public String toString ()
	{
		String s = root.toString ();
		s += "\n\n\n";
		//s += pathMapper.toString ();
		return s;
	}
}
