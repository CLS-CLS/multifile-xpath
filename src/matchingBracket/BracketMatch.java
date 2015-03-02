package matchingBracket;



import javax.swing.JTextPane;
import javax.swing.text.BadLocationException;

public class BracketMatch {
	
	
	JTextPane textPane;
	public BracketMatch(JTextPane textPane) {
		this.textPane = textPane;
	}
	
	public int matchBracket(){
		int position = -1;
		try {
			int counter = 0;
			int caretPosition = textPane.getCaretPosition();
			if (caretPosition > 0){
				String ch = textPane.getText(textPane.getCaretPosition()-1,1);
				if ("[".equals(ch)){
					for (int i = textPane.getCaretPosition(); 
							i < textPane.getText().length(); i++){
						if (textPane.getText(i, 1).equals("[")){
							counter++;
						}
						if (textPane.getText(i,1).equals("]")){
							if (counter == 0){
								position = i;
								break;
							}
							else {
								counter--;
							}
						}
					}
				}else if ("]".equals(ch)){
					for (int i = textPane.getCaretPosition()-2; 
					i >= 0; i--){
						if (textPane.getText(i, 1).equals("]")){
							counter++;
						}
						if (textPane.getText(i,1).equals("[")){
							if (counter == 0){
								position = i;
								break;
							}
							else {
								counter--;
							}
						}
					}
				}
			}
		} catch (BadLocationException e) {
			e.printStackTrace();
		}
		return position;
	}

	

}
