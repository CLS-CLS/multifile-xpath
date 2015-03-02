package utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Properties;

public class CustomProperties {
	
	public static final String DEFAULTFCDIR = "fileChooserDirectory";
	public static final String BOOKMARKS = "bookmark";
	private static final String PROPSFILENAME = "properties.txt";
	private String path;
	
	
	
	private Properties properties = new Properties();
	private boolean propertiesLoaded = true;
	
	public CustomProperties() {
		File file = new File(PROPSFILENAME);
		if (!file.exists()){
			try{ 
				file.createNewFile();
			}catch (Exception e) {
				e.printStackTrace();
			}
		}
		path = file.getAbsolutePath();
			
	}
	
	
	public boolean loadProperties(){
		FileInputStream fis;
		try {
			fis = new FileInputStream(new File(path));
			try{
				properties.load(fis);
			}
			finally {
				fis.close();
			}
			
		} catch (FileNotFoundException e) {
			propertiesLoaded = false;
			e.printStackTrace();
		}
		catch (IOException e) {
			propertiesLoaded = false;
			e.printStackTrace();
		}
		
		return propertiesLoaded;
	}
	
	public boolean saveProperties(){
		boolean isSaved = false;
		try{
			FileOutputStream fos = new FileOutputStream(path);
			try{ 
				properties.store(fos, null);
				isSaved = true;
			}
			finally{
				fos.close();
			}
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		
		return isSaved;
	}
	
	public void setProperty(String property, String value){
		this.properties.setProperty(property, value);
	}
	
	public String getProperty (String property){
		return properties.getProperty(property);
	}
	
}
