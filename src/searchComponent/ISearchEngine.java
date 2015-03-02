package searchComponent;

import java.util.ArrayList;

import javax.swing.text.JTextComponent;

public interface ISearchEngine {

	public static final int IGNORECAPS = 1;
	public static final int ACCOUNTCAPS = 0;

	public abstract ArrayList<Integer> searchOccurances(String searchString);

	public abstract int findNextOccurance();

	public abstract int searchDown(int currentPosition);

	public abstract int findPreviousOccurance();

	public abstract int searchUp(int currentPosition);

	public abstract void reset();

	public abstract void setSearchString(String ss);

	public abstract int getCaretPosition();

	public abstract boolean isIgnoreCaps();

	public abstract void setIgnoreCaps(boolean isIgnoreCaps);

	public abstract int searchDown(int startPosition, int endPosition);

	public abstract String getPreviousSearchString();

	public abstract int getDocumentSize();
	
	/**
	 * Allows the caller to set the document that the search engine is going to use.
	 * The search engine can modify the document of the text component. 
	 * @param textComponent
	 */
	public abstract void setSearchArea(JTextComponent textComponent);
	
	public abstract JTextComponent getSearchArea();


}