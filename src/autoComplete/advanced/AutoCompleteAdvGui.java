package autoComplete.advanced;

import java.awt.Color;
import java.awt.Component;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JScrollPane;
import javax.swing.ListCellRenderer;
import javax.swing.ListSelectionModel;

import observer.Observer;
import observer.impl.Observable;

import autoComplete.MatcherGui;
import main.XQueryGui;


@SuppressWarnings("serial")
public class AutoCompleteAdvGui extends MatcherGui<StringWithType> {


	private JList list;
	private JScrollPane spList;
	private Observable<StringWithType> observable = new Observable<StringWithType>();
	
	public <E> AutoCompleteAdvGui(XQueryGui xq) {
		super("AutoCompleter", xq);
		setFocusable(false);
		list = createList();
		list.setFocusable(true);
		spList = new JScrollPane(list);
		add(spList);
		setUndecorated(true);
		pack();
		setAlwaysOnTop(true);
	}

	@Override
	public JList requestTheFocus() {
		super.requestFocus();
		list.requestFocus();
		return list;
		
	}
		

	private JList createList() {
		final JList list = new JList();
		list.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		list.setLayoutOrientation(JList.VERTICAL);
		list.setBorder(BorderFactory.createLoweredBevelBorder());
		list.setBackground(new Color(255, 254, 250));
		list.addKeyListener(new KeyAdapter() {
			@Override
			public void keyPressed(KeyEvent e) {
				System.out.println(list.isFocusOwner());
				if (e.getKeyCode()==KeyEvent.VK_ESCAPE){
					setVisible(false);
				}
				else if (e.getKeyCode()==KeyEvent.VK_ENTER){
					notifyObservers(getSelectedValue());
				}
			}
		});
		
		list.setCellRenderer(new CustomCellRenderer());
		return list;
	}


	public void setListValues(ArrayList<StringWithType> values){
		list.setListData(values.toArray());
		try{ 
			list.setSelectedIndex(0);
		}catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	

	

	@Override
	public void addObserver(Observer<StringWithType> obs) {
		observable.addObserver(obs);

	}

	@Override
	public void deleteObserver(Observer<StringWithType> obs) {
		observable.deleteObserver(obs);
	}

	

	@Override
	public void notifyObservers() {
		observable.notifyObservers();
	}
	
	
	private class CustomCellRenderer extends JLabel implements ListCellRenderer{
		
		public CustomCellRenderer() {
			setOpaque(true);
		}
		@Override
		public Component getListCellRendererComponent(JList list, Object value,
				int index, boolean isSelected, boolean cellHasFocus) {
			StringWithType swt = (StringWithType)value;
			setText(swt.getString());
			setForeground(MyColors.valueOf(swt.getType()).getColor());
			if (isSelected){
				setBackground(Color.LIGHT_GRAY);
			}else {
				setBackground(Color.WHITE);
				
			}
			return this;
		}}


	
	@Override
	public int countObservers() {
		return observable.countObservers();
	}

	

	@Override
	public void deleteObservers() {
		observable.deleteObservers();
		
	}

	

	@Override
	public void notifyObservers(StringWithType arg) {
		observable.notifyObservers(arg);
		
	}

	@Override
	public StringWithType getSelectedValue() {
		return (StringWithType)list.getSelectedValue();
	}

	

}


