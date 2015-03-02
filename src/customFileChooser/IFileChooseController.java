package customFileChooser;

import javax.swing.JFileChooser;

public interface IFileChooseController {

	public abstract void saveProperties();
	
	public abstract void setProperty(String propertyName, String value);

	public abstract JFileChooser getFileChooser();

}