package main;


import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import java.util.ArrayList;
import javax.swing.SwingUtilities;
import javax.swing.SwingWorker;

import observer.IObservable;
import observer.Observer;

import org.w3c.dom.Document;

import autoComplete.Matcher;





public class LoadDocumentsWorker extends SwingWorker<Void, Void> implements Observer<LoadDocumentsWorker>{
	XQueries xq;
	XQueryGui xqGui;
	WaitDialog dialog;
	Matcher<?,Document> matcher;
	File[] files;
	private int counter = 0;
	
	
	public LoadDocumentsWorker(Matcher<?,Document> matcher,XQueries xq, XQueryGui xqGui,File[] files,WaitDialog dialog) {
		this.files = files;
		this.matcher = matcher;
		this.xq = xq;
		this.xqGui = xqGui;
		this.dialog = dialog;
		xq.addObserver(this);
		if (dialog != null){
			dialog.addWindowListener(new WindowAdapter() {	
					
				@Override
				public void windowClosing(WindowEvent e) {
					LoadDocumentsWorker.this.cancel(true);
					LoadDocumentsWorker.this.matcher.clear();
					LoadDocumentsWorker.this.xq.setFiles(new ArrayList<File>());
					LoadDocumentsWorker.this.xqGui.getFileChooser().setSelectedFiles(null);
					LoadDocumentsWorker.this.xq.deleteObserver(LoadDocumentsWorker.this);
				}
			});
		}
	}
	
		
	@Override
	protected void done() {
		if (dialog!=null){
			dialog.setVisible(false);
		}
		xq.deleteObserver(this);
	}
	
	
	@Override
	protected void process(java.util.List<Void> arg0) {
		dialog.setText("" + counter+  "/ " + xq.getFiles().size());
	}
		
	
	@Override
	protected Void doInBackground() throws Exception {
		if (dialog!=null){
			SwingUtilities.invokeLater(new Runnable() {
				@Override
				public void run() {
					dialog.setVisible(true);
				}
			});
		}
		xq.addFilesFromDirectory(files);
		for (int i=0;i<xq.getFiles().size();i++){
			matcher.add(xq.createDocument(xq.getFiles().get(i)));
			counter = i;
			publish();
		}
		return null;
	}


	

	@Override
	public void update(IObservable<LoadDocumentsWorker> observable,
			LoadDocumentsWorker arg) {
		publish();
		
	}
}
