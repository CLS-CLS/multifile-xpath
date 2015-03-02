package autoComplete.simple;


import java.awt.Color;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import javax.swing.BorderFactory;
import javax.swing.JComponent;
import javax.swing.JList;
import javax.swing.JScrollPane;
import javax.swing.ListSelectionModel;

import observer.Observer;
import observer.impl.Observable;

import autoComplete.MatcherGui;
import main.XQueryGui;


@SuppressWarnings("serial")
public class AutoCompleteGui extends MatcherGui<String>{
	
	private JList list;
	private JScrollPane spList;
	private Observable<String> observable;
		
	public <E> AutoCompleteGui(XQueryGui xq) {
		super("AutoCompleter",xq);
		list = createList();
		spList = new JScrollPane(list);
		add(spList);
		setUndecorated(true);
		pack();
		setAlwaysOnTop(true);
	}
	
	@Override
	public JComponent requestTheFocus() {
		super.requestFocus();
		list.requestFocus();
		try{ 
			list.setSelectedIndex(0);
		}catch (Exception e) {
			e.printStackTrace();
		}
		return list;
	}

	private JList createList() {
		final JList list = new JList();
//		list.setPreferredSize(new Dimension(200,150));
		list.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		list.setLayoutOrientation(JList.VERTICAL);
		list.setBorder(BorderFactory.createLoweredBevelBorder());
		list.setBackground(new Color(255, 254, 250));
		list.addKeyListener(new KeyAdapter() {
			@Override
			public void keyPressed(KeyEvent e) {
				if (e.getKeyCode()==KeyEvent.VK_ESCAPE){
					setVisible(false);
				}
				else if (e.getKeyCode()==KeyEvent.VK_ENTER){
					notifyObservers();
				}
				System.out.println(list.hasFocus());
			}
		});
	
		return list;
	}
	
	
	public void setListValues(ArrayList<String> values){
		list.setListData(values.toArray());
	}

	

	@Override
	public void addObserver(Observer<String> o) {
		observable.addObserver(o);
		
	}

	@Override
	public int countObservers() {
		return observable.countObservers();
	}

	@Override
	public void deleteObserver(Observer<String> o) {
		observable.deleteObserver(o);
		
	}

	@Override
	public void deleteObservers() {
		observable.deleteObservers();
		
	}

	
	@Override
	public void notifyObservers() {
		observable.notifyObservers();
		
	}

	@Override
	public void notifyObservers(String arg) {
		observable.notifyObservers(arg);
		
	}

	@Override
	public String getSelectedValue() {
		return (String)list.getSelectedValue();
	}
	
	

	
}
