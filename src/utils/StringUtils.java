package utils;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import structure.StringNode;

/**
 * This is a util class meant to hold varying methods to help transform string values.
 * 
 * The primary method in this class is the "transform" method.
 * 
 * @author Pat Ruppel
 */
public class StringUtils {
	private static StringUtils instance;
	// The dictionary of words that is being used for all methods.
	private ArrayList<String> wordDictonary = new ArrayList<String>();
	// The words we have seen up until now in a transformation.
	private ArrayList<String> seenWords = new ArrayList<String>();
	
	/**
	 * This class is a singleton, thus this ensures that only one instance
	 * is created, by returning the existing instance if it exists, otherwise 
	 * if there is no existence it creates a new instance and retuns that.
	 * 
	 * @return {@link StringUtils} singleton instance.
	 */
	public static StringUtils getInstance() {
		if (instance == null) {
			instance = new StringUtils();
		}
		return instance;
	}
	
	/**
	 * Method that takes an initial word, and end word and generates the
	 * shortest list possible transforming the initial word one letter at 
	 * a time into the end word.
	 * 
	 * @param initial {@link String} that is the word to be transformed.
	 * @param end {@link String} that is the word we are transforming into.
	 * @return {@link LinkedList} of {@link String} that is a list that 
	 * contains all the in between words of the transformation of the
	 * fewest steps from the initial word to the end word.
	 */
	public LinkedList<String> transform(String initial, String end) {
		seenWords = new ArrayList<String>();
		LinkedList<String> transformation = new LinkedList<String>();
		// Check to ensure the parameters are valid.
		if (!validTransformString(initial) || !validTransformString(end)) {
			Logger.getLogger(StringUtils.class.getName()).log(Level.SEVERE, 
					"Invalid parameter passed to transform");
			return transformation;
		}
		// Trim all the data to remove any potential extra spacing at either end.
		String trimedInitial = initial.trim();
		String trimedEnd = end.trim();
		// If the initial and end are the same value, the solution is just a list of the initial
		// word.
		if (trimedInitial.equalsIgnoreCase(trimedEnd)) {
			Logger.getLogger(StringUtils.class.getName()).log(Level.INFO, 
					"The initial word and end word were the same, returning list of the initial word.");
			transformation.add(trimedInitial);
			return transformation;
		}
		
		// Create the start of tree.
		StringNode rootWordNode = new StringNode(trimedInitial, null);
		// Add the initial word to the list of see words.
		seenWords.add(trimedInitial);
		// Add the next level of branches off the root word.
		StringNode endNode = createNextDepth(trimedEnd, rootWordNode);
		
		// Get a list of the children of the root word, and from there
		// loop over all children until the end node is found or the seen
		// list contains all words in the dictionary.
		List<StringNode> currentChildren = rootWordNode.getChildren();
		while (endNode == null) {
			if (wordDictonary.size() == seenWords.size()) {
				Logger.getLogger(StringUtils.class.getName()).log(Level.INFO, 
						"The list of seen words is the same size as dictionary, there is no transformation.");
				break;
			}
			List<StringNode> nextNodeChildren = new ArrayList<StringNode>();
			for (StringNode node : currentChildren) {
				endNode = createNextDepth(trimedEnd, node);
				if (endNode != null) {
					Logger.getLogger(StringUtils.class.getName()).log(Level.INFO, 
							"End node found - break out to process the transformation list.");
					break;
				}
				nextNodeChildren.addAll(node.getChildren());
			}
			currentChildren = nextNodeChildren;
		}
		
		// Check if we found an end point, if we did, generate the transformation word list.
		if (endNode != null) {
			transformation = getWordListToRoot(endNode, new LinkedList<String>());
		}
		// Reset the seen words back to being empty.
		seenWords = new ArrayList<String>();
		return transformation;
	}
	
	/**
	 * From the given end node generate the entire list of words up until the root.
	 * 
	 * @param endNode {@link StringNode} that is the current end node traversing
	 * back up to the root node.
	 * @param currentWord {@link LinkedList} of {@link String} that are all the 
	 * words found traversing the tree up from the first found end node. 
	 * @return a new {@link LinkedList} of {@link String} that is the list of words from
	 * the endNode all the way up until the root node.
	 */
	private LinkedList<String> getWordListToRoot(StringNode endNode, LinkedList<String> currentWord) {
		// Check end node value is not null.
		if (endNode == null) {
			Logger.getLogger(StringUtils.class.getName()).log(Level.SEVERE, 
					"Null endNode passed to getWordListRoot, return empty list.");
			return new LinkedList<String>();
		}
		// Check that end node has valid data.
		if (endNode.getData() == null || endNode.getData().isEmpty()) {
			Logger.getLogger(StringUtils.class.getName()).log(Level.SEVERE, 
					"Invalid endNode passed to getWordListRoot, return empty list.");
			return new LinkedList<String>();
		}
		// Check to make sure currentWord isn't null, if so instantiate it with the empty list.
		if (currentWord == null) {
			Logger.getLogger(StringUtils.class.getName()).log(Level.INFO, 
					"current word list was null, instantiate the list with the empty list.");
			currentWord = new LinkedList<String>();
		}
		
		// Add the data from this endNode to the list.
		currentWord.addFirst(endNode.getData());
		// Recursively traverse up the tree until we get to the root.
		getWordListToRoot(endNode.getParent(), currentWord);
		return currentWord;
	}
	
	
	/**
	 * Method to create the next level deep for a node and returns an end
	 * node if one was found or null otherwise.
	 * 
	 * @param end {@link String} that is the end point we are searching for.
	 * @param parent {@link StringNode} that is the parent we are creating 
	 * the branches off of.
	 * @return {@link StringNode} that is the first found endNode (has end 
	 * as it's data).  Returns null if there was no end node created from the 
	 * branches of the passed parent node.
	 */
	private StringNode createNextDepth(String end, StringNode parent) {
		// Check to make sure that the end parameter passed is valid.
		if (end == null || end.isEmpty()) {
			Logger.getLogger(StringUtils.class.getName()).log(Level.SEVERE, 
					"End string passed to createNextDepth was null or empty, returning null.");
			return null;
		}
		// Check to make sure that the parent is valid
		if (parent == null || parent.getData() == null || parent.getData().isEmpty()) {
			Logger.getLogger(StringUtils.class.getName()).log(Level.SEVERE, 
					"Parent node was invalid (null or no data) passed t createNextDepth, returning null.");
			return null;
		}
		
		// Generate all the branches.
		ArrayList<String> branchNodesData = getAllWordsOneCharacterOff(parent.getData().trim());
		ArrayList<StringNode> children = new ArrayList<StringNode>();
		StringNode endNode = null;
		for (String nodeData : branchNodesData) {
			StringNode branchNode = new StringNode(nodeData, parent);
			children.add(branchNode);
			if (nodeData.equalsIgnoreCase(end)) {
				endNode = branchNode;
			}
		}
		parent.setChildren(children);
		seenWords.addAll(branchNodesData);
		return endNode;
	}
	
	/**
	 * Method to find all words in the dictionary that are one character
	 * off from the current word that is passed to the method.
	 * 
	 * @param currentWord {@link String} that is to be used to find all other
	 * strings that are one letter off from it.
	 * @return a {@link ArrayList} of the words that are one letter off from the 
	 * word that is passed to the method.  It returns the empty string for
	 * invalid parameters.
	 */
	public ArrayList<String> getAllWordsOneCharacterOff(String currentWord) {
		ArrayList<String> oneCharacterOffWords = new ArrayList<String>();
		// Ensure that the word is not null or empty.
		if (currentWord == null || currentWord.isEmpty()) {
			Logger.getLogger(StringUtils.class.getName()).log(Level.SEVERE, 
					"Null or empty string word passed to getAllWordsOneCharacterOff");
			return oneCharacterOffWords;
		}
		// Make sure the word dictionary has been instantiated.
		if (wordDictonary == null || wordDictonary.isEmpty()) {
			Logger.getLogger(StringUtils.class.getName()).log(Level.INFO, 
					"Dictionary not already generated, generating dictionary");
			generateDictionary();
		}
		
		// Generate a list of words that are exactly one letter off and that haven't been seen.
		String trimedCurrentWord = currentWord.trim();
		for (String word : wordDictonary) {
			String trimedWord = word.trim();
			if (isOneCharacterOff(trimedCurrentWord, trimedWord) && !seenWords.contains(trimedWord)) {
				oneCharacterOffWords.add(trimedWord);
			}
		}
		
		return oneCharacterOffWords;
	}
	
	/**
	 * Method that checks to see if the current string is one off from another.
	 * 
	 * @param current {@link String} that is currently being checked.
	 * @param comparedTo {@link String} that we are comparing it against.
	 * @return true if the two strings are exactly one character apart, or
	 * false otherwise.
	 */
	public boolean isOneCharacterOff(String current, String comparedTo) {
		int charactersApart = 0;
		// Make sure neither of the parameters are null.
		if (current == null || comparedTo == null) {
			Logger.getLogger(StringUtils.class.getName()).log(Level.SEVERE, 
					"Invalid parameter passed to isOneChracterOff, one or both is null.");
			return false;
		}
		// Make sure that the length of each parameter is the same.
		if (current.length() != comparedTo.length()) {
			Logger.getLogger(StringUtils.class.getName()).log(Level.WARNING, 
					"Current lenght is " + current.length() + " but the compared to lengh is " + comparedTo.length());
			return false;
		}
		
		// Make both lower case and trimmed to avoid case issues.
		String lowerCurrent = current.trim().toLowerCase();
		String lowerComparedTo = comparedTo.trim().toLowerCase();
		
		// Check to see if these two strings are one character apart.
		for (int position = 0; position < lowerCurrent.length(); position++) {
			char currentChar = lowerCurrent.charAt(position);
			char comparedToChar = lowerComparedTo.charAt(position);
			if (currentChar != comparedToChar) {
				charactersApart++;
			}
		}
		return charactersApart == 1;
	}
	
	/**
	 * Method to ensure that the string passed is valid.  Valid is defined by
	 * being not null, not the empty string, and being contained in the 
	 * dictionary.  Case insensitive.
	 * 
	 * @param stringToValidate {@link String} to be validated as a word
	 * in the dictionary.
	 * @return true if the {@link String} is valid, or false otherwise.
	 */
	public boolean validTransformString(String stringToValidate) {
		// Make sure the word dictionary has been instantiated.
		if (wordDictonary == null || wordDictonary.isEmpty()) {
			Logger.getLogger(StringUtils.class.getName()).log(Level.INFO, 
					"Dictionary not already generated, generating dictionary");
			generateDictionary();
		}
		// Make sure the string we're validating isn't null.
		if (stringToValidate == null) {
			Logger.getLogger(StringUtils.class.getName()).log(Level.SEVERE, 
					"Invalid parameter to validTransformString, string is null");
			return false;
		}
		
		// Trim out extra white space on both ends and make sure it's not empty
		String trimmedString = stringToValidate.trim();
		if (trimmedString.isEmpty()) {
			Logger.getLogger(StringUtils.class.getName()).log(Level.SEVERE, 
					"Invalid parameter to validTransformString, string is empty.");
			return false;
		}
		
		// Check to make sure the word is in the dictionary.
		for (String word : wordDictonary) {
			if (word.equalsIgnoreCase(trimmedString)) {
				return true;
			}
		}
		return false; 
	}

	/**
	 * Method to generate the dictionary of five letter words to be used
	 * for the transformation.  This is done through reading in values from
	 * a text file.
	 */
	private void generateDictionary() {
		BufferedReader br = null;
		try {
			File currentDir = new File("assets");
			File dictionaryFile = new File(currentDir, "dictionary.txt");
			br = new BufferedReader(new FileReader(dictionaryFile));
	    
	        String line = br.readLine();

	        while (line != null) {
	            String[] words = line.split(" ");
	            for (String word : words) {
	            	wordDictonary.add(word.trim());
	            }
	            line = br.readLine();
	        }
	    } catch (Exception e) {
	    	e.printStackTrace();
	    } finally {
	    	if (br != null) {
	    		try {
					br.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
	    	}
	    }
	}
}
