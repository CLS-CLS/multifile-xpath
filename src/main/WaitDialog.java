package main;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Image;
import javax.swing.ImageIcon;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;

import javax.swing.JPanel;
import javax.swing.SwingUtilities;

@SuppressWarnings("serial")
public class WaitDialog extends JDialog {
	private static final int WIDTH =250;
	private static final int HEIGHT = 85;
	private static final String MSG ="Loading Files";
	private static final Image loadingImg = new ImageIcon(ClassLoader.getSystemResource("loading.gif")).getImage().getScaledInstance(50, 50, Image.SCALE_REPLICATE);
	
	private JLabel textLabel = new JLabel(); 
	
	public WaitDialog(JFrame parent) {
		super(parent,MSG);
		JPanel panel = new JPanel();
		setLayout(new BorderLayout());
		setModalityType(ModalityType.APPLICATION_MODAL);
		setPreferredSize(new Dimension(WIDTH,HEIGHT));
		add(new JLabel(new ImageIcon(loadingImg)),BorderLayout.WEST);
		panel.add(textLabel);
		add(panel,BorderLayout.CENTER);
		setLocationRelativeTo(parent);
		pack();
		
	}
	
	
	public void setText(String text){
		textLabel.setText(text);
		repaint();
	}
	
	public static void main(String[] args) {
		final JFrame frame = new JFrame();
		
		frame.setPreferredSize(new Dimension(300,300));
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setLocation(300,	300);
		SwingUtilities.invokeLater(new Runnable() {
			
			@Override
			public void run() {
				frame.pack();
				frame.setVisible(true);
				final WaitDialog wd = new WaitDialog(frame);
				wd.setVisible(true);
			}
		});
		
	}

}
