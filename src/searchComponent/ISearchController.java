package searchComponent;

import javax.swing.JComponent;
import javax.swing.text.JTextComponent;


public interface ISearchController {

	public abstract JComponent getSearchGui();
	
	public void setSearchArea(JTextComponent textComponent);

	

}