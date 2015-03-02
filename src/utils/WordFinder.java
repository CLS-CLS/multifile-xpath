package utils;

import java.util.ArrayList;

import autoComplete.advanced.StringWithType;



public class WordFinder {
	ArrayList<Character> seperators = new ArrayList<Character>();
	int position;
	char seperator =' ';
	
	public WordFinder() {
		seperators.add(' ');
		seperators.add('=');
		seperators.add(']');
		seperators.add('[');
		seperators.add(')');
		seperators.add('(');
		seperators.add('\"');
		seperators.add('/');
		seperators.add('@');
		seperators.add('\n');
		seperators.add('\r');
		seperators.add('\'');
		
	}
	
	/**
	 * Isolates a word from a document. The word is composed of all the letters found from the
	 * starting index and looking backwards until a separator is found. According to the found
	 * separator the appropriate attribute type is assigned.
	 * @param doc the document that will be searched
	 * @param position the position it will start searching backwards
	 * @return the word found with the type of attribute
	 */
	public StringWithType getCurrentWord(String doc,int position){
		
		StringBuffer sb = new StringBuffer();
		position--;
		while (position >=0 && !isSeperator(doc.charAt(position))){
			sb.append(doc.charAt(position));
			this.position = position;
			position--;
		}
		
		String result =  sb.reverse().toString();
		String type;
		if (seperator=='@')type=StringWithType.ATTRIBUTE;
		else if (seperator=='\"')type=StringWithType.ATTRIBUTEVALUE;
		else type=StringWithType.TAG;
		return new StringWithType(result, type);
	}
	
	
	/**
	 * checks if the given character is defined as separator
	 * @param chr
	 * @return true if the character is in the list of separators
	 */
	private boolean isSeperator(char chr){
		
		for (int i=0;i<seperators.size();i++){
			if (chr==seperators.get(i)){
				seperator = seperators.get(i);
				return true;
			}
		}
		seperator=' ';
		return false;
		
	}
	
	/**
	 * Used in order to get the position of the caret after the 
	 * getCurrentWord is called. The position corresponds the the position 
	 * the getCurrentWord method had stopped, that is the positio of the first
	 * found separator character
	 * @return
	 */
	public int getPosition(){
		return position;
	}

}
