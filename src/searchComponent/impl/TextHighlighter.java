package searchComponent.impl;

import java.awt.Color;
import java.util.ArrayList;
import javax.swing.text.BadLocationException;
import javax.swing.text.DefaultHighlighter;
import javax.swing.text.Highlighter;
import javax.swing.text.JTextComponent;
import javax.swing.text.Highlighter.Highlight;
import javax.swing.text.Highlighter.HighlightPainter;

import searchComponent.IHighlight;
import searchComponent.ISearchEngine;

public class TextHighlighter implements ISearchEngine, IHighlight{
	private static TextHighlighter instance = new TextHighlighter();
	private static HighlightPainter painter = new DefaultHighlighter.DefaultHighlightPainter(Color.GREEN);
	private Highlighter highlighter;
	private JTextComponent searchArea;
	private String text;
	private String searchString = "";
	private boolean isIgnoreCaps;
	private String previousSearchString = "";
	
	public static TextHighlighter getInstanse(){
		return instance;
	}
	
	private TextHighlighter(){
		this.isIgnoreCaps = true;
	}
	
	public void highlightText(int startIndex, int endIndex){
		try{
			highlighter.addHighlight(startIndex, endIndex, painter);
		}catch (BadLocationException e) {
			e.printStackTrace();
		}
		catch(NullPointerException e){
			e.printStackTrace();
		}
	}
	
	public ArrayList<Integer> searchOccurances(String searchString){
		ArrayList<Integer> positions = new ArrayList<Integer>();
		int startPosition = 0;
		while (startPosition != -1){
			startPosition = text.indexOf(searchString, startPosition + 1);
			positions.add(startPosition);
		}
		positions.remove(positions.size()-1);
		return positions;
	}
	
	protected  boolean canPerformSearch(){
		if (searchArea == null)return false;
		if (searchString == null || searchString.equals(""))return false;
		return true;
	}
	

	public int findNextOccurance(){
		if (!canPerformSearch())return -1;
		int currentPosition = searchArea.getCaretPosition();
		int position  = searchDown(currentPosition);
		highlight(position);	
		
		return position;
	}
	

	public int searchDown(int currentPosition){
		
		int position;
		
		if (isIgnoreCaps) {
			position =  text.toLowerCase().indexOf(searchString.toLowerCase(), currentPosition);
		}else{
			position =  text.indexOf(searchString, currentPosition);
		}
		return position;
	}

	public int findPreviousOccurance(){
		if(!canPerformSearch())return -1;
		int currentPosition = searchArea.getCaretPosition();
		boolean isHighlighted = isAlreadyHighlighted(currentPosition);
		if (isHighlighted){
			currentPosition = currentPosition - searchString.length() *2 ;
		}
		int position = searchUp(currentPosition);
		highlight(position);
		return position;
	}

	
	public int searchUp(int currentPosition) {
		int position ;
		boolean isHighlighted = isAlreadyHighlighted(currentPosition);
		if (isHighlighted){
			currentPosition = currentPosition - searchString.length() *2 ;
		}
		if (isIgnoreCaps){
			position = text.toLowerCase().lastIndexOf(searchString.toLowerCase(), currentPosition);
		}
		else {
			position = text.lastIndexOf(searchString, currentPosition);
		}
		
		return position;
	}
	public boolean isAlreadyHighlighted(int currentPosition){
		return isAlreadyHighlighted(currentPosition, null);
	}
	
	public boolean isAlreadyHighlighted(int currentPosition, int[] startinghighlightPosition){
		boolean isHighlighted = false;
		Highlight[] highlights = highlighter.getHighlights();
		int hlStartoffset = 0;
		for (int i = 0; i <highlights.length ;i++){
			if (highlights[i].getEndOffset() == currentPosition){
				isHighlighted = true;
				hlStartoffset = highlights[i].getStartOffset();
				break;
			}
			if (isHighlighted && startinghighlightPosition!=null){
				startinghighlightPosition[0] = hlStartoffset;
			}
		}
		return isHighlighted;
	}
	
	public void highlight(int position){
		if (position==-1)return;
		try{
			searchArea.setCaretPosition(position + searchString.length());
			highlighter.removeAllHighlights();
			int endPosition = position + searchString.length();
			highlighter.addHighlight(position, endPosition, painter);
		}
		catch (Exception e) {
			e.printStackTrace();
		}
	}
	
				
	public void reset(){
		if (highlighter!=null){
			highlighter.removeAllHighlights();
		}
	}
	
	
	public void setSearchString(String ss){
		previousSearchString = new String(searchString);
		searchString = ss;
		reset();
	}
	
	
	public int getCaretPosition(){
		return searchArea.getCaretPosition();
	}
	
	
	public void setSearchArea(JTextComponent component){
		reset();
		if (component==null)return;
		this.searchArea = component;
		this.highlighter = component.getHighlighter();
		try {
			this.text = component.getDocument().getText(0, component.getDocument().getLength());
		}catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	
	public boolean isIgnoreCaps() {
		return isIgnoreCaps;
	}

	
	public void setIgnoreCaps(boolean isIgnoreCaps) {
		this.isIgnoreCaps = isIgnoreCaps;
		reset();
	}

	
	public int searchDown(int startPosition, int endPosition) {
		int position;	
		
		if (isIgnoreCaps) {
			position =  text.substring(startPosition,endPosition).toLowerCase().indexOf(searchString.toLowerCase(), startPosition);
		}else{
			position =  text.substring(startPosition, endPosition).indexOf(searchString, startPosition);
		}
		return position;
	}
	
	
	public String getPreviousSearchString(){
		return previousSearchString;
	}

	public int getDocumentSize() {
		return searchArea.getDocument().getLength();
	}

	@Override
	public JTextComponent getSearchArea() {
		return searchArea;
		
	}
	
	

}
