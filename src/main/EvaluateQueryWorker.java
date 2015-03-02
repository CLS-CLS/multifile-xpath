package main;

import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.JDialog;
import javax.swing.JTextPane;
import javax.swing.SwingUtilities;
import javax.swing.SwingWorker;
import javax.swing.text.StyledDocument;

public class EvaluateQueryWorker extends SwingWorker<StyledDocument,Void>{
	
	XQueries xQueries;
	XQueryGui gui;
	JDialog dialog;
	boolean isCancelled = false;
	
	
	public EvaluateQueryWorker(XQueries xQueries, XQueryGui gui, JDialog dialog) {
		this.xQueries = xQueries;
		this.gui = gui;
		this.dialog = dialog;
		if (dialog!=null){
			dialog.setTitle("Executing Query");
			((WaitDialog)dialog).setText("<html>Evaluation in progress..<br>please wait</html>");
			dialog.addWindowListener(new WindowAdapter() {
				
				@Override
				public void windowClosing(WindowEvent we) {
					isCancelled = true;
					EvaluateQueryWorker.this.xQueries.setInterrupted(true);
					cancel(true);
				}
			});
		}
			
	}
	@Override
	protected StyledDocument doInBackground(){
		if (dialog!=null){
			SwingUtilities.invokeLater(new Runnable() {
				
				@Override
				public void run() {
					dialog.setVisible(true);
				}
			});
		}
		((WaitDialog)dialog).setText("<html>Evaluation done..<br>Creating Document</html>");
		StyledDocument doc = null;
		try {
			doc = xQueries.evaluate(gui.getQuery());
			if (doc!=null){
				JTextPane textPane = new JTextPane();
				textPane.setStyledDocument(doc);
				gui.addTab(textPane);
			}
		} catch (Exception ex) {
			gui.addTab(null);
			gui.getDisplayArea().setText(ex.getCause().toString());
		}
		return doc;
	
	}
	
	@Override
	protected void done() {
		if (!isCancelled){
			dialog.setVisible(false);
		}	
		
	}
	

}
