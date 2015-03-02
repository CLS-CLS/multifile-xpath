package customFileChooser.impl;

import java.io.File;
import javax.swing.JFileChooser;

import customFileChooser.IFileChooseController;
import utils.CustomProperties;

public class FileChooseController implements IFileChooseController {
	
	private CustomFileChooser customFileChooser = new CustomFileChooser(new BookMarks());
	private CustomProperties customProperties = new CustomProperties();
	boolean propertiesLoaded = customProperties.loadProperties();
	
	
	public FileChooseController() {
		if (propertiesLoaded){
			String defaultDirectory = customProperties.getProperty(
					CustomProperties.DEFAULTFCDIR);
			if (defaultDirectory!=null){
				customFileChooser.setCurrentDirectory(new File(defaultDirectory));
			}
			customFileChooser.setBookMarks(customProperties.getProperty(CustomProperties.BOOKMARKS));
		}
	}
	
	
	
	public void saveProperties(){
			try{
				customProperties.setProperty(CustomProperties.DEFAULTFCDIR, (customFileChooser.getCurrentDirectory().getAbsolutePath()));
				customProperties.setProperty(CustomProperties.BOOKMARKS, 
						customFileChooser.getBookmarks().getBookmarksInString());
				customProperties.saveProperties();
			}catch(Exception ex){
				ex.printStackTrace();
			}
	}
	
	

	
	public JFileChooser getFileChooser(){
		return customFileChooser;
	}



	@Override
	public void setProperty(String propertyName, String value) {
		customProperties.setProperty(propertyName, value);
		
	}


}

