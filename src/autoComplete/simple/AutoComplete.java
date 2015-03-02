package autoComplete.simple;


import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import autoComplete.Matcher;

public class AutoComplete implements Matcher<String, Document>{
	private HashSet<String> lib = new HashSet<String>();
	private String toMatch;
	
	public AutoComplete() {}
	
	

	@Override
	public boolean remove(String toRemove) {
		return lib.remove(toRemove);
	}

	@Override
	public void clear() {
		lib.clear();
		
	}

	@Override
	public ArrayList<String> match(String toMatch) {
		if (toMatch ==null || toMatch.equals(""))return null;
		this.toMatch = toMatch;
		ArrayList<String> suggestions1 = new ArrayList<String>();
		ArrayList<String> suggestions2 = new ArrayList<String>();
		Iterator<String> it = lib.iterator();
		while (it.hasNext()){
			String sugg = it.next();
			if (sugg.length() >= toMatch.length()){
				boolean isExtend = true;
				boolean isCaseEqual = true;
				for (int i= 0; i < toMatch.length(); i++){
					if (toMatch.charAt(i) != sugg.charAt(i)){
						isCaseEqual = false;
						if (Character.toLowerCase(toMatch.charAt(i)) != Character.toLowerCase(sugg.charAt(i))){
							isExtend = false;
							break;
						}
					}
				}
				if (isExtend && isCaseEqual){
					suggestions1.add(sugg);
				}
				if (isExtend && !isCaseEqual){
					suggestions2.add(sugg);
				}
			}
		}
		suggestions1.addAll(suggestions2);
		
		return suggestions1;
	}

	@Override
	public String getToMatch() {
		return toMatch;
	}
	
	public void add(ArrayList<Document> docs){
		if (docs==null)return;
		for (Document doc : docs){
			Element root = doc.getDocumentElement();
			add(root);
		}
	}
	
	public void add(Document doc){
		if (doc ==null)return;
		Element root = doc.getDocumentElement();
		add(root);
		
	}
	
	private void add(Node el){
		String nodeName = el.getNodeName();
		if (nodeName!="#text")lib.add(nodeName);
		NamedNodeMap attributes = el.getAttributes();
		if (attributes!=null){
			for (int i = 0; i < attributes.getLength();i++){
				String attrName = attributes.item(i).getNodeName();
				String attrVal = attributes.item(i).getNodeValue();
				lib.add(attrName);
				lib.add(attrVal);
			}
		}
		NodeList childs = el.getChildNodes();
		for (int i=0;i <childs.getLength();i++){
			add(childs.item(i));
		}
		
	}



	

}
