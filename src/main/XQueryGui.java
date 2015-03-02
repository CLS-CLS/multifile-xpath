package main;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.*;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSeparator;
import javax.swing.JTabbedPane;
import javax.swing.JTextArea;
import javax.swing.JTextPane;
import javax.swing.KeyStroke;
import javax.swing.border.BevelBorder;
import javax.swing.event.ChangeListener;
import javax.swing.text.DefaultHighlighter;
import javax.swing.text.Document;
import javax.swing.text.Highlighter;
import javax.swing.text.JTextComponent;

import main.MainController.EvaluateButtonListener;


@SuppressWarnings("serial")
public class XQueryGui extends JFrame{
	private static final String VERSION = "v5.0";
	private JFileChooser fileChooser;
	private String currentDirectoryPath = new String();
	private JButton searchFileButton;
	private JButton openFileButton;
	private JButton executeButton;
	private JTextComponent queryArea;
	private JScrollPane queryScrollBar;
	private JTabbedPane displayTabPane;
	private int tabCounter = 0;
	private ImageIcon defaultIcon  = new ImageIcon(ClassLoader.getSystemResource("x.png"));
	private ImageIcon hoverIcon  = new ImageIcon(ClassLoader.getSystemResource("xred.png"));
	private JMenuBar menubar;
	private JComponent searchGui;

	public XQueryGui(JTextComponent queryArea, JComponent searchGui, JFileChooser cfc) {
//		try {
//			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
//		} catch (Exception ex){
//			ex.printStackTrace();
//		}
		this.searchGui = searchGui;
		this.fileChooser = cfc;

		
		((JComponent)searchGui).setVisible(true);
		executeButton = new JButton("Execute Query");
		executeButton.setFocusable(false);
		searchFileButton = new JButton("Select Files");
		searchFileButton.setFocusable(false);
		openFileButton = new JButton("View XML \nFile");
		openFileButton.setFocusable(false);
		if (queryArea ==null){
			setQueryArea(createQueryArea());
		}else {
			setQueryArea(queryArea);
		}
		queryScrollBar = new JScrollPane(getQueryArea());
		fileChooser.setFileSelectionMode(JFileChooser.FILES_AND_DIRECTORIES);
		fileChooser.setMultiSelectionEnabled(true);
		displayTabPane = new JTabbedPane();
		displayTabPane.addMouseListener(new MouseTabPaneListner(displayTabPane));
		
		
		menubar = createMenuBar();
		JPanel queryPanel = createQuerryPanel();
		
		setLayout(new BorderLayout(0,0)); 
		add(queryPanel, BorderLayout.NORTH);
		add(displayTabPane, BorderLayout.CENTER);
		setJMenuBar(menubar);
		add((JComponent)searchGui,BorderLayout.SOUTH);
		
		setPreferredSize(new Dimension(1024,768));
		pack();
		setDefaultCloseOperation(EXIT_ON_CLOSE);
	}

	private JTextArea createQueryArea() {
		JTextArea query = new JTextArea(3,80);
		query.setBorder(BorderFactory.createBevelBorder(BevelBorder.RAISED));
		query.setLineWrap(true);
		query.setWrapStyleWord(true);
		return query;
	}

	private JPanel createQuerryPanel() {
		JPanel qPanel = new JPanel();
		JPanel qButtonPanel = createQuerryButtonPanel();
		qPanel.setLayout(new BorderLayout());
		qPanel.add(queryScrollBar,BorderLayout.CENTER);
		qPanel.add(qButtonPanel,BorderLayout.EAST);
		return qPanel;
	}

	private JPanel createQuerryButtonPanel() {
		JPanel queryButtonPanel = new JPanel();
		queryButtonPanel.setLayout(new GridLayout(3,1));
		queryButtonPanel.add(searchFileButton);
		queryButtonPanel.add(executeButton);
		queryButtonPanel.add(openFileButton);
		return queryButtonPanel;
	}

	private JMenuBar createMenuBar() {
		JMenuBar menuBar = new JMenuBar();
		JMenu menu = new JMenu("Menu");
		JMenuItem search = new JMenuItem("Search");
		search.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_F, ActionEvent.CTRL_MASK));
		if (searchGui!=null){
			search.addActionListener(new SearchActionListener());
		}
		JButton aboutButton = new JButton("About");
		aboutButton.setOpaque(false);
		aboutButton.setBackground(Color.red);
		aboutButton.setFocusable(false);
		aboutButton.setBorderPainted(false);
		aboutButton.setRolloverEnabled(true);
		aboutButton.setPreferredSize(new Dimension(15,20));
		aboutButton.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				JOptionPane.showMessageDialog(XQueryGui.this,"XQueries " + VERSION +
						" \nby Christos Lytsikas","About",JOptionPane.INFORMATION_MESSAGE);
			}
		});
		menu.add(search);
		menu.add(new JSeparator());
		menu.add(aboutButton);
		menuBar.add(menu);
		return menuBar;
	}
	
	public void addTab(JTextPane textPane){
		tabCounter++;
		JScrollPane sp2 = createDisplayScrollPane(textPane);
		displayTabPane.addTab(null, sp2);
		displayTabPane.setFocusable(false);
		JButton closeButton = createTabCloseButton();
		JPanel tabPanel = new JPanel();
		tabPanel.setOpaque(false);
		JLabel tabLabel = new JLabel(""+tabCounter);
		tabLabel.setOpaque(false);
		tabPanel.add(tabLabel);
		tabPanel.add(closeButton);
		int tabCounter = displayTabPane.getTabCount()-1;
		displayTabPane.setTabComponentAt(tabCounter, tabPanel);
		repaint();
		displayTabPane.setSelectedIndex(displayTabPane.getTabCount()-1);
	}

	private JButton createTabCloseButton() {
		JButton closeButton = new JButton(defaultIcon);
		closeButton.setOpaque(false);
		closeButton.setBackground(Color.red);
		closeButton.setBorderPainted(false);
		closeButton.setFocusable(false);
		closeButton.setRequestFocusEnabled(false);
		closeButton.setPreferredSize(new Dimension(13,13));
		closeButton.setFocusPainted(false);
		closeButton.addMouseListener(new MouseAdapter() {
									
			@Override
			public void mouseExited(MouseEvent me) {
				((JButton)me.getSource()).setIcon(defaultIcon);
				
			}
			
			@Override
			public void mouseEntered(MouseEvent me) {
				((JButton)me.getSource()).setIcon(hoverIcon);
				
			}
			
			@Override
			public void mouseClicked(MouseEvent me) {
				displayTabPane.removeTabAt(displayTabPane.indexOfTabComponent(me.getComponent().getParent()));
			}
		});
		return closeButton;
	}

	private JScrollPane createDisplayScrollPane(JTextPane textpane) {
		JTextPane displayArea;
		if (textpane == null) {
			displayArea = new JTextPane();
		}else {
			displayArea = textpane;
		}
		Highlighter highlighter = new DefaultHighlighter();
		displayArea.setHighlighter(highlighter);
		displayArea.setBorder(BorderFactory.createBevelBorder(BevelBorder.LOWERED));
		JScrollPane sp2 = new JScrollPane(displayArea,JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		return sp2;
	}
	
	
	
	public void addSearchFileListener(ActionListener al){
		searchFileButton.addActionListener(al);
	}
	
	
	
	public void addOpenFileListener(ActionListener al){
		openFileButton.addActionListener(al);
	}
	
	
	
	public void addExecuteListener(EvaluateButtonListener evaluateListener) {
		executeButton.addActionListener(evaluateListener);
	}
	
	
	public JFileChooser getFileChooser() {
		return fileChooser;
	}
	
	public String getQuery() {
		String query = getQueryArea().getSelectedText();
		if (query == null) query = getQueryArea().getText();
		return query;
	}
	
	public int getQueryCaretPosition(){
		return getQueryArea().getCaretPosition();
	}

	
	public String getCurrentDirectoryPath() {
		return currentDirectoryPath;
	}
	
	public JTextComponent getDisplayArea() {		
		JScrollPane sp = null;
		int selectedIndex = displayTabPane.getSelectedIndex();
		if (selectedIndex == -1)return null;
		try {
			sp = (JScrollPane)displayTabPane.getComponentAt(selectedIndex);
		}catch (Exception e) {
			e.printStackTrace();
		}
		return (JTextComponent)sp.getViewport().getView();
	}
	
	
	
	public int popNoFileSelected() {
		return JOptionPane.showConfirmDialog(this, "No file selected.\nUse the last viewed?", "No File Selected", JOptionPane.OK_CANCEL_OPTION);
	}
	
	public void displayNoFileMsg() {
		JOptionPane.showMessageDialog(this, "NO FILE SELECTED");
	}
	
	
	private class SearchActionListener implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent e) {
			if (!searchGui.isVisible()){
				searchGui.setVisible(true);
				searchGui.requestFocus();
			}else {
				searchGui.setVisible(false);
			}
			pack();
			repaint();
		}
	}
	
	public void addTabbedPaneChangeListener(ChangeListener cl){
		displayTabPane.addChangeListener(cl);
	}

	public JComponent getSearchGui() {
		return searchGui;
	}

	public Document getQueryAreaDocument(){
		return getQueryArea().getDocument();
	}

	public void setQueryArea(JTextComponent queryArea) {
		this.queryArea = queryArea;
	}

	public JTextComponent getQueryArea() {
		return queryArea;
	}
		
}
