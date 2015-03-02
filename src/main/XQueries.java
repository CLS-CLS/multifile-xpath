package main;

import java.io.File;
import java.util.ArrayList;

import javax.swing.text.StyledDocument;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpression;
import javax.xml.xpath.XPathFactory;

import observer.IObservable;
import observer.Observer;
import observer.impl.Observable;

import org.w3c.dom.Document;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;




public class XQueries implements IObservable<LoadDocumentsWorker> {
	
	private static final int TABSPACE = 8;
	
	boolean interrupted = false;
	private Observable<LoadDocumentsWorker> observable = new Observable<LoadDocumentsWorker>();
	ArrayList<File> files = new ArrayList<File>();
	XPath xPath;
	DocumentBuilder docBuilder;
	

	public XQueries() throws ParserConfigurationException  {
		xPath = XPathFactory.newInstance().newXPath();
		DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
		dbf.setFeature("http://apache.org/xml/features/nonvalidating/load-external-dtd", false);
		docBuilder = dbf.newDocumentBuilder();
	}
	
	public Document createDocument(File file){
		Document doc = null;
		try {
			doc = docBuilder.parse(file);
		}catch (Exception e){}
		
		return doc;
	}
	
	public ArrayList<Document> createDocuments(File[] files){
		ArrayList<Document> docs = new ArrayList<Document>();
		for (File file: files){
			try {
				Document doc = docBuilder.parse(file);
				docs.add(doc);
			}catch (Exception e){}
		}
		return docs;
	}
	
		
	/**
	 * Compliles and executes the xpath command.
	 * @param expression the xPath command to be executed
	 * @return the result of the xPath query as a string also containing the names in xml format 
	 */
	public StyledDocument evaluate(String expression) throws Exception{
		DocumentPainter dc = new DocumentPainter();
		XPathExpression compExpression = xPath.compile(expression);
		for (File file : files){
			dc.append(" \n\n**************            FILE : "  +file.getName() + "               *********************\n",
					DocumentPainter.TEXT);
			if (interrupted)break;
			try { 
				Document doc = docBuilder.parse(file);
				try{

					NodeList nodeList = (NodeList) compExpression.evaluate(doc,XPathConstants.NODESET);
					for (int i = 0 ; i <nodeList.getLength(); i++){
						XQueries.elementToString(nodeList.item(i),0,dc);
						dc.append("\n", DocumentPainter.TEXT);
					}

				}catch (Exception e) {
					//the error may have caused because the return result is a string and not a nodeset. Try again 
					//with return value as a string.
					try {
						dc.append((String)compExpression.evaluate(doc,XPathConstants.STRING),0);
					}catch (Exception ex) {
						//if there is an error again print the first error and not this one.
						throw e;
					}
				}
			}
			catch (Exception ex){
				dc.append("\n"+ ex.toString() + "\n",0);
			}
		}

		return dc.getStyledDoc();

	}




	public void setFiles(ArrayList<File> files){
		this.files = files;
	}
	
	public ArrayList<File> getFiles(){
		return this.files;
	}
	
	
	public void addFilesFromDirectory(File[] files) {
		this.files = new ArrayList<File>();
		for (File f : files){
			if (f.isDirectory())addFilesFromDirectory(f);
			else {
				this.files.add(f);
				notifyObservers();
			}
		}
		
	}
	
	private void addFilesFromDirectory(File directory) {
		for (File f :directory.listFiles()){
			if (f.isDirectory())addFilesFromDirectory(f);
			else {
				files.add(f);
				notifyObservers();
			}
		}
	}


	/**
	 * 
	 * @param n
	 * @param tab
	 * @param dc
	 */
	private static void elementToString(Node n,int tab, DocumentPainter dc) {
		String name = n.getNodeName();
		String space = "";
		boolean isCustonTag = true;
		boolean isOpen = true;
		
		StringBuffer tabsb = new StringBuffer();
		for (int i =0;i <tab* TABSPACE;i++){
			tabsb.append(" ");
		}
		space = tabsb.toString();
		
		if (name.charAt(0)=='#')isCustonTag = false;

		if (isCustonTag)dc.append(space,0).append("<",1).append(name,1);

		NamedNodeMap attrs = n.getAttributes();
		if (attrs != null) {
			for (int i = 0; i < attrs.getLength(); i++) {
				Node attr = attrs.item(i);
				dc.append(" ",0).append(attr.getNodeName(),2).append("=\"",3).append(attr.getNodeValue(),3).append(
				"\"",3);
			}
		}

		String textContent = null;
		NodeList children = n.getChildNodes();

		if (children.getLength() == 0) {
			if ((textContent = n.getNodeValue()) != null && !"".equals(textContent.trim())) {
				dc.append(space,0).append(textContent,0).append("\n",0);
				if (isCustonTag)dc.append(space,0).append("</",1).append(name,1).append(">",1).append("\n",0);
			} else {
				if(isCustonTag)dc.append("/>",1).append("\n",0);
				isOpen = false;
			}
		} else {
			dc.append(">",1).append("\n",0);
			for (int i = 0; i < children.getLength(); i++) {
				elementToString(children.item(i),tab+1,dc);
			}
		}

		if (isCustonTag &&isOpen)dc.append(space,0).append("</",1).append(name,1).append(">",1).append("\n",0);
		
	}
	
	

	@Override
	public void addObserver(Observer<LoadDocumentsWorker> obs) {
		observable.addObserver(obs);
		
	}

	
	
	public void setInterrupted(boolean interrupted){
		this.interrupted = interrupted;
	}

	@Override
	public int countObservers() {
		return observable.countObservers();
	}

	@Override
	public void deleteObserver(Observer<LoadDocumentsWorker> o) {
		observable.deleteObserver(o);
		
	}

	@Override
	public void deleteObservers() {
		observable.deleteObservers();
		
	}

	@Override
	public void notifyObservers() {
		observable.notifyObservers();
		
	}

	@Override
	public void notifyObservers(LoadDocumentsWorker arg) {
		observable.notifyObservers(arg);
		
	}


		


}
