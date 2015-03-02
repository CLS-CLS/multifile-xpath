package main;

import javax.swing.text.BadLocationException;
import javax.swing.text.DefaultStyledDocument;
import javax.swing.text.Style;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyledDocument;

import autoComplete.advanced.MyColors;

public class DocumentPainter {
	public static final int TEXT = 0;
	public static final int TAG = 1;
	public static final int ATTRIBUTE = 2;
	public static final int ATTRIBUTEVALUE = 3;
	
	private StyledDocument styledDoc;

	public DocumentPainter() {
		styledDoc = new DefaultStyledDocument();
		addStyles();
	}

	private void addStyles() {
		
		for (MyColors color : MyColors.values()){
			Style style = styledDoc.addStyle(color.name(), null);
			StyleConstants.setForeground(style, color.getColor());
		}
	}
	
	
	public DocumentPainter append(String string, int type){
		Style styleToUse = styledDoc.getStyle(MyColors.Text.name());
		switch (type) {
		case TAG:
			styleToUse = styledDoc.getStyle(MyColors.Tag.name());
			break;
		case ATTRIBUTE:
			styleToUse = styledDoc.getStyle(MyColors.Attribute.name());
			break;
		case TEXT:
			styleToUse = styledDoc.getStyle(MyColors.Text.name());
			break;
		case ATTRIBUTEVALUE:
			styleToUse = styledDoc.getStyle(MyColors.AttributeValue.name());
			break;
		default:
			break;
		}
		try {
			styledDoc.insertString(styledDoc.getLength(), string, styleToUse);
		} catch (BadLocationException e) {
			e.printStackTrace();
		}
		return this;
		
	}
	
	public StyledDocument getStyledDoc(){
		return styledDoc;
	}
	
	
	
	
	
	

}
