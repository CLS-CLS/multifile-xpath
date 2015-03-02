package autoComplete;

import java.awt.Point;
import java.util.ArrayList;
import javax.swing.JComponent;
import javax.swing.JDialog;

import observer.IObservable;

import main.XQueryGui;


@SuppressWarnings("serial")
public abstract class MatcherGui<E> extends JDialog implements IObservable<E>{
	
	XQueryGui parent;
	
	public MatcherGui(String title, XQueryGui parent) {
		super(parent);
		setModalityType(ModalityType.MODELESS);
		this.parent = parent;
	}
	
	public abstract void setListValues(ArrayList<E> values);
	
	public void relocateMatcher() {
		Point caretCoords = parent.getQueryArea().getCaret().getMagicCaretPosition();
		if (caretCoords == null)return;
		Point position = new Point(caretCoords.x + parent.getQueryArea().getLocationOnScreen().x ,
				caretCoords.y + parent.getQueryArea().getLocationOnScreen().y + 20);
		setLocation(position);
	}
	
	public void displayMatches(ArrayList<E> match) {
		if (match==null || match.size()==0){
			setVisible(false);
			return;
		}
		setListValues(match);
		if (!isVisible())setVisible(true);
	}
	
	public abstract JComponent requestTheFocus();
	
	public abstract E getSelectedValue();
		
	
	

}
