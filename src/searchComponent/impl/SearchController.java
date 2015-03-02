package searchComponent.impl;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import javax.swing.JComponent;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.text.JTextComponent;

import searchComponent.ISearchController;

public class SearchController implements  ISearchController {
	
	private SearchGui searchGui = new SearchGui();
	private TextHighlighter searchEngine =  TextHighlighter.getInstanse();
	
	public SearchController() {
		NextSearchButtonActionListener sal = new NextSearchButtonActionListener();
		searchGui.addTextListener(new SearchStringDocumentListener());
		searchGui.addIgnoreCaseItemListener(new IgnoreCapsCheckBoxItemListener());
		searchGui.addNextAcionListener(sal);
		searchGui.addPreviousActionListener(sal);
	}
	
	
	@Override
	public JComponent getSearchGui() {
		return searchGui;
	}

	
	private class NextSearchButtonActionListener implements ActionListener {
		
		@Override
		public void actionPerformed(ActionEvent e) {
			SearchWorker searchWorker = new SearchWorker(searchEngine, searchGui);
			if (e.getActionCommand().equals(SearchGui.FINDNEXT)){
				searchWorker.setStatus(SearchWorker.SEARCHNEXT);
				searchWorker.execute();
			}else {
				searchWorker.setStatus(SearchWorker.SEARCHPREVIOUS);
				searchWorker.execute();
			}
		}
	}
	
	private class IgnoreCapsCheckBoxItemListener implements ItemListener{

		@Override
		public void itemStateChanged(ItemEvent e) {
			searchEngine.setIgnoreCaps(e.getStateChange()==ItemEvent.SELECTED);				
			SearchWorker searchWorker = new SearchWorker(searchEngine, searchGui);
			searchWorker.setStatus(SearchWorker.CHANGEDSEARCHWORD);
			searchWorker.execute();
		}
	}
	
	private class SearchStringDocumentListener implements DocumentListener {

		private void search(DocumentEvent e) {
			
			String ss = searchGui.getSearchString();
			searchEngine.setSearchString(ss);
			SearchWorker searchWorker = new SearchWorker(searchEngine, searchGui);
			searchWorker.setStatus(SearchWorker.CHANGEDSEARCHWORD);
			searchWorker.execute();
		}
		
		@Override
		public void insertUpdate(DocumentEvent e) {
			search(e);
		}
		@Override
		public void removeUpdate(DocumentEvent e) {
			search(e);
		}
		@Override
		public void changedUpdate(DocumentEvent e) {
			search(e);
			
		}
	}

	@Override
	public void setSearchArea(JTextComponent textComponent) {
		searchEngine.setSearchArea(textComponent);
	}
	
	
	

}
