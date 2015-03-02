package main;

import java.awt.Dimension;
import java.awt.KeyboardFocusManager;
import java.awt.Toolkit;
import java.awt.event.*;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.File;
import java.util.ArrayList;
import javax.swing.JComponent;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;
import javax.swing.event.*;
import javax.swing.text.*;
import javax.xml.parsers.ParserConfigurationException;

import observer.*;
import customFileChooser.IFileChooseController;
import customFileChooser.impl.FileChooseController;
import searchComponent.ISearchController;
import searchComponent.impl.SearchController;
import utils.*;
import autoComplete.Matcher;
import autoComplete.MatcherGui;
import autoComplete.advanced.*;


public class MainController implements Observer<StringWithType>{
	private XQueryGui gui;
	private XQueries xQueries;
	private MatcherGui<StringWithType> matcherGui;
	private Matcher<StringWithType,org.w3c.dom.Document> matcher =  new AutoCompleteAdv(); 
	private WordFinder wordFinder = new WordFinder();
	private ISearchController searchController = new SearchController();
	private IFileChooseController fileController = new FileChooseController();
		

	public MainController() throws ParserConfigurationException  {
		intializeObjects();
		SwingUtilities.invokeLater(new Runnable() {

			public void run() {
				gui.setVisible(true);

				//Open in middle of the screen
				Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
				int w = gui.getSize().width;
				int h = gui.getSize().height;
				int x = (dim.width-w)/2;
				int y = (dim.height-h)/2;
				gui.setLocation(x, y);
			}
		});
	}

	class SelectFilesButtonListener implements ActionListener{

		public void actionPerformed(ActionEvent e) {
			String directory;
			int action = fileController.getFileChooser().showOpenDialog(gui);
			if (action == JFileChooser.APPROVE_OPTION){
				File[] files = fileController.getFileChooser().getSelectedFiles();
				if (files != null){
					directory = files[0].getParent();
					fileController.setProperty(CustomProperties.DEFAULTFCDIR, directory);
					matcher.clear();
					WaitDialog wd = new WaitDialog(gui);
					new LoadDocumentsWorker(matcher, xQueries, gui, files, wd).execute();
				}
			}
		}
	}

	class EvaluateButtonListener implements ActionListener {

		public void actionPerformed(ActionEvent e) {
			//if the user has not selected any files and does not want to use the file viewed
			//the variable is false and no evaluation of the query is done.
			boolean displayResults = false;
			//check if a file is chosen. If not ask if the file viewed is the one to open.
			if (xQueries.getFiles()== null || xQueries.getFiles().size()==0){
				File[] files = fileController.getFileChooser().getSelectedFiles();
				if (files != null && files.length == 1  && !files[0].isDirectory()){
					int result = gui.popNoFileSelected();
					if (result == JOptionPane.OK_OPTION){
						ArrayList<File> arrFile = new ArrayList<File>();
						arrFile.add(files[0]);
						xQueries.setFiles(arrFile);
						displayResults = true;
					}
				}
				else {
					gui.displayNoFileMsg();
				}
			}else {
				displayResults = true;
			}

			if (displayResults){
				WaitDialog wd = new WaitDialog(gui);
				new EvaluateQueryWorker(xQueries, gui, wd).execute();
			}
		}
	}

	class ViewFileButtonListener implements ActionListener{

		public void actionPerformed(ActionEvent e) {
			int action = fileController.getFileChooser().showOpenDialog(gui);
			if (action == JFileChooser.APPROVE_OPTION){
				File[] files = fileController.getFileChooser().getSelectedFiles();
				if (files != null && files.length == 1  && !files[0].isDirectory()){
					String contents = CustomUtils.getContents(files[0]);
					gui.addTab(null);
					gui.getDisplayArea().setText(contents);
					gui.getDisplayArea().setCaretPosition(0);
					matcher.add(xQueries.createDocuments(files));
				}
			}
		}
	}




	//Every time another tab is selected, the search is going to be performed 
	//in this tab and also the system will monitor the text of that tab
	//for any changes and update the text of the search engine accordingly.
	private class TabbedPaneChangeListener implements ChangeListener{

		@Override
		public void stateChanged(ChangeEvent e) {
			final JTextComponent displayArea = gui.getDisplayArea();
			if (displayArea==null)return;
			searchController.setSearchArea(displayArea);
			displayArea.addPropertyChangeListener(new PropertyChangeListener() {

				@Override
				public void propertyChange(PropertyChangeEvent evt) {
					if (evt.getPropertyName().equals("document")){
						searchController.setSearchArea(displayArea);
					}

				}
			});
			displayArea.getDocument().addDocumentListener(new DocumentListener() {

				private void update(){
					searchController.setSearchArea(displayArea);
				}
				@Override
				public void removeUpdate(DocumentEvent e) {
					update();
				}
				@Override
				public void insertUpdate(DocumentEvent e) {
					update();
				}
				@Override
				public void changedUpdate(DocumentEvent e) {
					update();
				}
			});
		}
	}



	

	private class QueryAreaKeyListener extends KeyAdapter {

		@Override
		public void keyPressed(KeyEvent e) {
			if (e.getKeyCode()==KeyEvent.VK_SPACE &&
					e.getModifiers()==InputEvent.CTRL_MASK){
				matcherGui.setVisible(true);
				matcherGui.relocateMatcher();
				String queryText;
				try {
					queryText = gui.getQueryAreaDocument().getText(0, gui.getQueryCaretPosition());
					System.out.println(queryText);
					System.out.println(queryText.length());
					StringWithType word = wordFinder.getCurrentWord(queryText,queryText.length());
					matcherGui.displayMatches(matcher.match(word));
					gui.getQueryArea().requestFocus();
				} catch (BadLocationException e1) {
					e1.printStackTrace();
				}
			}
			if (e.getKeyCode()==KeyEvent.VK_ESCAPE){
				matcherGui.setVisible(false);
			}
			if (e.getKeyCode()==KeyEvent.VK_DOWN){
				JComponent focus = matcherGui.requestTheFocus();
				KeyboardFocusManager.getCurrentKeyboardFocusManager().dispatchKeyEvent(new KeyEvent(
						focus, e.getID(), e.getWhen(), e.getModifiers(), e.getKeyCode(), e.getKeyChar()));

			}
			if(e.getKeyCode()==KeyEvent.VK_ENTER){
				if (matcherGui.isVisible()){
					matcherGui.requestFocus();
					matcherGui.notifyObservers(matcherGui.getSelectedValue());
					e.consume();
				}else {
					if (e.getModifiers()==InputEvent.CTRL_MASK){
						new EvaluateButtonListener().actionPerformed(null);		
						e.consume();
					}
				}
			}
		}
	}

	private class QueryAreaAutoCompleteDocumentListener implements DocumentListener {
		private void documentChanged(DocumentEvent e,int changeLength) {
			if (matcherGui.isVisible()){
				try {
					String docInString = e.getDocument().
					getText(0, e.getDocument().getLength());
					StringWithType word = wordFinder.getCurrentWord(docInString, e.getOffset()+changeLength);
					matcherGui.displayMatches(matcher.match(word));
				}catch (Exception ex) {
					ex.printStackTrace();
				}	
			}
		}

		@Override
		public void insertUpdate(DocumentEvent e) {
			documentChanged(e,e.getLength());
		}

		@Override
		public void removeUpdate(DocumentEvent e) {
			documentChanged(e,0);
		}

		@Override
		public void changedUpdate(DocumentEvent e) {}
			
	}

	private void intializeObjects() throws ParserConfigurationException{
		xQueries = new XQueries();
		gui = new XQueryGui(new matchingBracket.BracketMathingTextPane(), searchController.getSearchGui(), fileController.getFileChooser());
		matcherGui = new AutoCompleteAdvGui(gui);
		matcherGui.addObserver(this);
		gui.addSearchFileListener(new SelectFilesButtonListener());
		gui.addExecuteListener(new EvaluateButtonListener());
		gui.addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent e) {
				fileController.saveProperties();
			}
		});
		gui.addOpenFileListener(new ViewFileButtonListener());
		gui.addTabbedPaneChangeListener(new TabbedPaneChangeListener());

		gui.getQueryArea().addKeyListener(new QueryAreaKeyListener());
		gui.getQueryArea().getDocument().addDocumentListener(new QueryAreaAutoCompleteDocumentListener());
		gui.getQueryArea().getDocument().addUndoableEditListener((new QueryAreaUndoableListener(gui.getQueryArea())));
	}	


	@Override
	public void update(IObservable<StringWithType> observable, StringWithType obj) {
		String word = obj.getString();
		matcherGui.setVisible(false);
		Document doc = gui.getQueryAreaDocument();
		try {
			doc.remove(gui.getQueryArea().getCaretPosition()-matcher.getToMatch().length(), matcher.getToMatch().length());
			doc.insertString(gui.getQueryArea().getCaretPosition(), word, null);
		}catch (BadLocationException e) {
			System.err.println(e.offsetRequested());
		}
	}

	public static void main(String[] args) throws ParserConfigurationException  {
		new MainController();	}



}
