package customFileChooser.impl;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JFileChooser;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.ListSelectionModel;

import customFileChooser.IBookmarks;



@SuppressWarnings("serial")
public class CustomFileChooser extends JFileChooser{

	private IBookmarks bookMarks;
	private JList list;
	private JButton addBookmarkButton;
	private JButton removeBookmarkButton;
	
		
	public CustomFileChooser(IBookmarks bookmarks) {
		super();
		initialize(bookmarks);
	}
	public CustomFileChooser(String currentDirectoryPath, IBookmarks bookmarks){
		super(currentDirectoryPath);
		initialize(bookmarks);
		
	}
	
	private void initialize(IBookmarks bookmarks){
		this.bookMarks = bookmarks;
		JComponent accessoryPanel  = createAccessoryPanel();
		setAccessory(accessoryPanel);
		
	}
	
	
	
	private JComponent createAccessoryPanel() {
		list = createList();
		JPanel acessoryPanel = new JPanel();
		JPanel buttonPanel =  new JPanel();
		acessoryPanel.setLayout(new BoxLayout(acessoryPanel,BoxLayout.Y_AXIS));
		JScrollPane listScroller = new JScrollPane(list);
		listScroller.setPreferredSize(new Dimension(50,150));
		buttonPanel.setLayout(new BorderLayout());
		addBookmarkButton = new JButton("Add bookmark");
		removeBookmarkButton = new JButton("Delete bookmark");
		buttonPanel.add(addBookmarkButton,BorderLayout.NORTH);
		buttonPanel.add(removeBookmarkButton, BorderLayout.SOUTH);
		addListenerToButtons();
		acessoryPanel.add(listScroller);
		acessoryPanel.add(buttonPanel);
		return acessoryPanel;
	}
	
	
	
	private void addListenerToButtons() {
		addBookmarkButton.addActionListener(new ActionListener() {
			
		
			public void actionPerformed(ActionEvent e) {
				String path = getCurrentDirectory().getPath();
				String name = createDialog();
				if (name!=null){
					bookMarks.addBookmark(name, path);
					list.setListData(bookMarks.getBookmarks().keySet().toArray());
				}
				
			}
		});
		
		removeBookmarkButton.addActionListener(new ActionListener() {
			
			
			public void actionPerformed(ActionEvent e) {
				String bookmark = (String)list.getSelectedValue();
				bookMarks.removeBookmark(bookmark);
				list.setListData(bookMarks.getBookmarks().keySet().toArray());
			}
		});
	}


	private JList createList() {
		JList list = new JList();
		CustomListSelectionListener lsl = new CustomListSelectionListener();
		list.addMouseListener(lsl);
		list.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		list.setBackground(Color.lightGray);
		return list;
	}

	
	public void setBookMarks(IBookmarks bookMarks) {
		this.bookMarks = bookMarks;
		list.setListData(bookMarks.getBookmarks().keySet().toArray());
	}
	
	public void setBookMarks(String bookmarks){
		bookMarks.setBookmarks(bookmarks);
		list.setListData(bookMarks.getBookmarks().keySet().toArray());
	}
	
	private String createDialog() {
		String name = JOptionPane.showInputDialog(this, "Please specify the name\n of the new Bookmark");
		//Check for valid name?
		return name;
	}
	
	
	private class CustomListSelectionListener extends MouseAdapter {

		@Override
		public void mouseClicked(MouseEvent me) {
			if (list.getSelectedIndex()!= -1){
				File file = new File(bookMarks.getBookmarks().get(list.getSelectedValue()).getPath());
				setSelectedFile(file);
			}
		}
	}
	
	public IBookmarks getBookmarks(){
		return bookMarks;
	}

	

}
