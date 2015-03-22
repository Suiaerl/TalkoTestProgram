package tests;

import java.util.ArrayList;
import java.util.LinkedList;

import utils.StringUtils;
import static org.junit.Assert.*;


public class StringUtilsTest {
	@org.junit.Test
	public void testGetInstance() {
		assertNotNull(StringUtils.getInstance());
	}
	
	@org.junit.Test
	public void testValidTransformString() {
		// Test empty string
		boolean result = StringUtils.getInstance().validTransformString("");
		assertFalse(result);
		// Test longer variant of the empty string
		result = StringUtils.getInstance().validTransformString("     ");
		assertFalse(result);
		// Test null to check for NPEs.
		result = StringUtils.getInstance().validTransformString(null);
		assertFalse(result);
		// Test valid 5 letter word - wrong case
		result = StringUtils.getInstance().validTransformString("pixel");
		assertTrue(result);
		// Test valid 5 letter word - varied case.
		result = StringUtils.getInstance().validTransformString("PIxEL");
		assertTrue(result);
		// test invalid word - too long
		result = StringUtils.getInstance().validTransformString("eleven");
		assertFalse(result);
		// Test valid word with accidental spacing.
		result = StringUtils.getInstance().validTransformString(" pixel ");
		assertTrue(result);
	}
	
	@org.junit.Test
	public void testIsOneCharacterOff() {
		// Test null.
		boolean result = StringUtils.getInstance().isOneCharacterOff(null, null);
		assertFalse(result);
		// Test empty string.
		result = StringUtils.getInstance().isOneCharacterOff("", "");
		assertFalse(result);
		// Test invalid sizes (one larger than the other).
		result = StringUtils.getInstance().isOneCharacterOff("test", "tester");
		assertFalse(result);
		// Test valid one off.
		result = StringUtils.getInstance().isOneCharacterOff("piles", "piled");
		assertTrue(result);
		// Test case difference.
		result = StringUtils.getInstance().isOneCharacterOff("PiLes", "pIlEd");
		assertTrue(result);
		// Test spacing issues.
		result = StringUtils.getInstance().isOneCharacterOff("piles ", " piled");
		assertTrue(result);
	}
	
	@org.junit.Test
	public void testGetAllWordsOneCharacterOff() {
		// Get varied expected results.
		ArrayList<String> emptyList = new ArrayList<String>();
		ArrayList<String> aboutList = new ArrayList<String>();
		aboutList.add("ABORT");
		
		// Test null
		ArrayList<String> wordListResult = StringUtils.getInstance().getAllWordsOneCharacterOff(null);
		assertEquals(emptyList, wordListResult);
		// Test empty string
		wordListResult = StringUtils.getInstance().getAllWordsOneCharacterOff("");
		assertEquals(emptyList, wordListResult);
		// Test valid string
		wordListResult = StringUtils.getInstance().getAllWordsOneCharacterOff("about");
		assertEquals(aboutList, wordListResult);
		// Test valid string with spacing.
		wordListResult = StringUtils.getInstance().getAllWordsOneCharacterOff("about ");
		assertEquals(aboutList, wordListResult);
	}
	
	@org.junit.Test
	public void testTransformation() {
		// Get varied expected results.
		LinkedList<String> emptyList = new LinkedList<String>();
		
		LinkedList<String> singleItem = new LinkedList<String>();
		singleItem.add("pixel");
		
		LinkedList<String> smartToStartList = new LinkedList<String>();
		smartToStartList.add("SMART");
		smartToStartList.add("START");
		
		LinkedList<String> smartToStarkList = new LinkedList<String>();
		smartToStarkList.addAll(smartToStartList);
		smartToStarkList.add("STARK");
		
		LinkedList<String> smartToStackList = new LinkedList<String>();
		smartToStackList.addAll(smartToStarkList);
		smartToStackList.add("STACK");
		
		LinkedList<String> smartToBrainList = new LinkedList<String>();
		smartToBrainList.add("SMART");
		smartToBrainList.add("SPART");
		smartToBrainList.add("SPAIT");
		smartToBrainList.add("SPAIN");
		smartToBrainList.add("SLAIN");
		smartToBrainList.add("BLAIN");
		smartToBrainList.add("BRAIN");
		
		
		// Null test - to ensure we check both parameters.
		LinkedList<String> transformationResult = StringUtils.getInstance().transform(null, null);
		assertEquals(emptyList, transformationResult);
		
		// Empty string test.
		transformationResult = StringUtils.getInstance().transform("", "");
		assertEquals(emptyList, transformationResult);
		
		// Test valid result.
		transformationResult = StringUtils.getInstance().transform("pixel", "pixel");
		assertEquals(singleItem, transformationResult);
		
		// Test valid result with space.
		transformationResult = StringUtils.getInstance().transform("pixel ", " pixel");
		assertEquals(singleItem, transformationResult);
		
		// Test valid result one off.
		transformationResult = StringUtils.getInstance().transform("SMART", "START");
		assertEquals(smartToStartList.size(), transformationResult.size());
		assertEquals(smartToStartList, transformationResult);
		
		// Test valid result two off.
		transformationResult = StringUtils.getInstance().transform("SMART", "STARK");
		assertEquals(smartToStarkList.size(), transformationResult.size());
		assertEquals(smartToStarkList, transformationResult);
		
		// Test valid result two off.
		transformationResult = StringUtils.getInstance().transform("SMART", "STACK");
		assertEquals(smartToStackList.size(), transformationResult.size());
		assertEquals(smartToStackList, transformationResult);
		
		// Test valid result of smart to brain.
		transformationResult = StringUtils.getInstance().transform("SMART", "BRAIN");
		assertEquals(smartToBrainList.size(), transformationResult.size());
		assertEquals(smartToBrainList, transformationResult);
	}
}
