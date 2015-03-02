package searchComponent.impl;

import java.awt.Color;

import javax.swing.SwingWorker;


public class SearchWorker extends SwingWorker<Void, Void>{
	
	public static final int SEARCHNEXT = 0;
	public static final int SEARCHPREVIOUS = 1;
	public static final int CHANGEDSEARCHWORD = 2;
	private TextHighlighter searchEngine;
	private SearchGui searchGui;
	private int status;
	private int position;
	private boolean canPerformSearch;
	
	
	public SearchWorker(TextHighlighter searchEngine, SearchGui searchGui) {
		this.searchEngine = searchEngine;
		this.searchGui = searchGui;
	}

	@Override
	protected Void doInBackground() throws Exception {
		canPerformSearch = searchEngine.canPerformSearch();
		if (!canPerformSearch)return null;
		
		int currentPosition = searchEngine.getCaretPosition();
		switch (status) {
		case SEARCHNEXT:
			position  = searchEngine.searchDown(currentPosition);
			break;
		case CHANGEDSEARCHWORD:
			String previousSearch = searchEngine.getPreviousSearchString();
			if (previousSearch!=null){
				currentPosition = Math.max(currentPosition-previousSearch.length(),
						searchEngine.getDocumentSize());
			}
			position = searchEngine.searchDown(currentPosition);
			if (position ==-1){
				position = searchEngine.searchDown(0,
						Math.min(currentPosition + searchGui.getSearchString().length(),
								searchEngine.getDocumentSize()));
			}
			break;
			
		case SEARCHPREVIOUS:
			position = searchEngine.searchUp(currentPosition);
			break;
		}
		return null;
	}
	
	@Override
	protected void done() {
		switch (status){
		case SEARCHNEXT:
		case SEARCHPREVIOUS:
			if (canPerformSearch){
				searchEngine.highlight(position);
				Color status = searchGui.getBackgroundTextFieldColor();
				if (position==-1 && status.equals(SearchGui.okColor)){
					searchGui.startFading();
				}
				else searchGui.setBackgroundColor(SearchGui.okColor);
			}
			break;
		case CHANGEDSEARCHWORD:
			if (canPerformSearch){
				searchEngine.highlight(position);
				if (position==-1){
					searchGui.setBackgroundColor(SearchGui.errorColor);
				}
				else searchGui.setBackgroundColor(SearchGui.okColor);
			}
			else {
				searchGui.setBackgroundColor(SearchGui.okColor);
			}
			break;
		}
	}
	public int getStatus() {
		return status;
	}
	public void setStatus(int status) {
		this.status = status;
	}
	
	
 
	
		
	
	

}
