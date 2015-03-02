package searchComponent;

import javax.swing.text.JTextComponent;

public interface IHighlight {

	public abstract void highlightText(int startIndex, int endIndex);

	public abstract boolean isAlreadyHighlighted(int currentPosition);

	public abstract boolean isAlreadyHighlighted(int currentPosition,
			int[] startinghighlightPosition);

	public abstract void highlight(int position);
	
	public abstract void setSearchArea(JTextComponent textComponent);
	
	public abstract JTextComponent getSearchArea();

}