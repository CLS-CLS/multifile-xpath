package searchComponent.impl;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemListener;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JTextField;
import javax.swing.JToolBar;
import javax.swing.event.DocumentListener;


@SuppressWarnings("serial")
public class SearchGui extends JToolBar {
	public static final Color errorColor = Color.pink;
	public static final Color okColor = Color.white;
	public static final String FINDNEXT = "FINDNEXT";
	public static final String FINDPREVIOUS = "FINDPREVIOUS";
	
	private static final String xImageName = "xred.png" ;
	private static final String upArrowImg = "up.png";
	private static final String downArrowImg = "down.png";
	private JButton closeButton;
	private JButton upButton;
	private JButton downButton;
	private JTextField textField;
	private JCheckBox capsButton;
	private ColorFader colorFader;
	
	public SearchGui() {
		super("Find");
		this.setLayout(new FlowLayout());
		setFloatable(false);
		upButton = new JButton(new ImageIcon(ClassLoader.getSystemResource(upArrowImg)));
		upButton.setPreferredSize(new Dimension(30,30));
		upButton.setFocusable(false);
		upButton.setActionCommand(FINDPREVIOUS);
		downButton = new JButton(new ImageIcon(ClassLoader.getSystemResource(downArrowImg)));
		downButton.setPreferredSize(new Dimension(30,30));
		downButton.setFocusable(false);
		downButton.setActionCommand(FINDNEXT);
		closeButton = createCloseButton();
		textField = new JTextField(25);
		JLabel ignoreLabel = new JLabel("Ignore Capslock");
		capsButton = new JCheckBox();
		capsButton.setSelected(true);
		capsButton.setOpaque(false);
		capsButton.setFocusable(false);
		this.add(closeButton);
		this.add(textField);
		this.add(upButton);
		this.add(downButton);
		this.add(ignoreLabel);
		this.add(capsButton);
		colorFader = new ColorFader(textField,300,errorColor);
		
	}
	
	
	public void setBackgroundColor(Color bg){
		textField.setBackground(bg);
	}

	private JButton createCloseButton() {
		JButton closeButton = new JButton(new ImageIcon(ClassLoader.getSystemResource(xImageName)));
		closeButton.setOpaque(false);
		closeButton.setBorderPainted(true);
		closeButton.setFocusable(false);
		closeButton.setRequestFocusEnabled(false);
		closeButton.setFocusPainted(false);
		closeButton.setPreferredSize(new Dimension(30,30));
		closeButton.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				setVisible(false);
				
			}
		});
		return closeButton;
	}
	
	
	public void addNextAcionListener(ActionListener al){
		downButton.addActionListener(al);
	}
	
	
	public void addPreviousActionListener(ActionListener al){
		upButton.addActionListener(al);
	}
	
	
	public void addCloseButtonActionListener(ActionListener al){
		closeButton.addActionListener(al);
	}
	
	
	public void addTextListener(DocumentListener dl){
		textField.getDocument().addDocumentListener(dl);
	}
	
	
	public String getSearchString(){
		return textField.getText();
	}
	
	
	public void addIgnoreCaseItemListener(ItemListener il){
		capsButton.addItemListener(il);
	}
	
		
	@Override
	public void requestFocus() {
		super.requestFocus();
		textField.requestFocus();
	}

	public Color getBackgroundTextFieldColor() {
		return 	textField.getBackground();
	}

	public void startFading() {
		colorFader.startFading();
		
	}
		
}
