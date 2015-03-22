package structure;

import java.util.List;

/**
 * String node is a class to represent the tree structure of {@link String}.
 * 
 * This class has the following information: 
 * data - a String representation of what is stored at this node in
 * the tree.
 * parent - the StringNode that this is a child off - this may be null if it is
 * the root node.
 * children - the {@link List} of {@link StringNode} children that are the branches
 * off this node.  This can be null.
 * 
 * @author Pat Ruppel
 */
public class StringNode {
	public String data;
	public StringNode parent;
	public List<StringNode> children;
	
	// Constructors.
	public StringNode(String data, StringNode parent) {
		this.data = data;
		this.parent = parent;
	}
	
	// Getters and setters.
	public String getData() {
		return data;
	}
	
	public StringNode getParent() {
		return parent;
	}
	
	public List<StringNode> getChildren() {
		return children;
	}
	public void setChildren(List<StringNode> children) {
		this.children = children;
	}
}
