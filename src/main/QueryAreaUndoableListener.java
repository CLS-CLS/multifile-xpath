package main;

import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

import javax.swing.event.UndoableEditEvent;
import javax.swing.event.UndoableEditListener;
import javax.swing.text.JTextComponent;
import javax.swing.undo.CannotRedoException;
import javax.swing.undo.CannotUndoException;
import javax.swing.undo.UndoManager;
import javax.swing.undo.UndoableEdit;

public class QueryAreaUndoableListener implements UndoableEditListener{

	UndoManager undoManager = new UndoManager();

	public QueryAreaUndoableListener(JTextComponent queryArea) {

		queryArea.addKeyListener(new KeyAdapter() {
			public void keyPressed(KeyEvent e) {
				if (e.getKeyCode()==KeyEvent.VK_Z && e.isControlDown()){
					e.consume();
					try{
						undoManager.undo();
					}catch(CannotUndoException ex){
						ex.printStackTrace();
					}
				}
				if (e.getKeyCode()==KeyEvent.VK_Y && e.isControlDown()){
					e.consume();
					try{
						undoManager.redo();
					}catch(CannotRedoException ex){
						ex.printStackTrace();
					}
				}
			}
		});
	}

	@Override
	public void undoableEditHappened(UndoableEditEvent e) {
		UndoableEdit ue = e.getEdit();
		if (ue.getPresentationName().equals("style change")){
			return;
		}
		undoManager.addEdit(ue);
	}

}