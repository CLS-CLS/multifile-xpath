package main;

import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;
import javax.swing.JTabbedPane;


public class MouseTabPaneListner extends MouseAdapter {
	
	private JTabbedPane displayTabPane;
	private CustomPopUp customPopUp = new CustomPopUp();
	private Component selected;

	

	public MouseTabPaneListner(JTabbedPane displayTabPane) {
		this.displayTabPane = displayTabPane;
		this.displayTabPane.setComponentPopupMenu(customPopUp);
	}

	@Override
	public void mouseClicked(MouseEvent e) {
		selected = displayTabPane.getTabComponentAt(displayTabPane.getSelectedIndex());
		if (e.getButton()==MouseEvent.BUTTON2){
			displayTabPane.remove(displayTabPane.getSelectedIndex());
		}
    }
	
	private void closeOtherTabs(){
		int index = 0;
		selected = displayTabPane.getTabComponentAt(displayTabPane.getSelectedIndex());
		while (displayTabPane.getTabCount() >1){
			Component comp = displayTabPane.getTabComponentAt(index);
			if (comp == selected){
				index = 1;
				continue;
			}
			if (comp!=null){
				displayTabPane.remove(index);
			}
		}
	}
	
	
	@SuppressWarnings("serial")
	private class CustomPopUp extends JPopupMenu implements ActionListener{
		JMenuItem cutMenuItem = new JMenuItem("Close all unselected tabs");
	   		
		public CustomPopUp() {
			this.add(cutMenuItem);	
			cutMenuItem.addActionListener(this);
		}

		@Override
		public void actionPerformed(ActionEvent e) {
			closeOtherTabs();			
		}
		
		
	}

	
}
