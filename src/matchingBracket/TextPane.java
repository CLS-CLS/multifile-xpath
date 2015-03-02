package matchingBracket;

import java.awt.Color;
import java.awt.Font;

import javax.swing.JTextPane;
import javax.swing.SwingUtilities;
import javax.swing.event.CaretEvent;
import javax.swing.event.CaretListener;
import javax.swing.text.Style;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyledDocument;

@SuppressWarnings("serial")
public class TextPane extends JTextPane implements CaretListener{
	private static enum Styles{
		DEFAULT(Font.MONOSPACED,Color.BLACK,14), RED(Font.MONOSPACED,Color.RED,14);
		Color color;
		String font;
		int size;
		Styles(String font, Color color,int size){
			this.font = font;
			this.color = color;
			this.size = size;
		}
	}


	BracketMatch bm;
	StyledDocument sdoc;

	public TextPane() {
		super();
		bm = new BracketMatch(this);
		sdoc = getStyledDocument();
		Style st = sdoc.addStyle(Styles.DEFAULT.toString(), null);
		StyleConstants.setForeground(st,Styles.DEFAULT.color);
		StyleConstants.setFontFamily(st, Styles.DEFAULT.font);
		StyleConstants.setFontSize(st, Styles.DEFAULT.size);
		st = sdoc.addStyle(Styles.RED.toString(), null);
		StyleConstants.setForeground(st,Styles.RED.color);
		StyleConstants.setFontFamily(st, Styles.RED.font);
		StyleConstants.setFontSize(st, Styles.RED.size);
		addCaretListener(this);
		setCharacterAttributes(getStyle(Styles.DEFAULT.name()), true);
	}

	@Override
	public void caretUpdate(CaretEvent e) {
		SwingUtilities.invokeLater(new Runnable() {
			@Override
			public void run() {
				StyledDocument st = getStyledDocument();
				st.setCharacterAttributes(0, st.getEndPosition().getOffset(), st.getStyle(Styles.DEFAULT.name()), false);
				int position = bm.matchBracket();
				if (position != -1){
					st.setCharacterAttributes(position, 1, st.getStyle(Styles.RED.name()), true);
					st.setCharacterAttributes(getCaretPosition()-1, 1, st.getStyle(Styles.RED.name()), true);
				}
			}
		});
	}

}
