package autoComplete.advanced;


import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import autoComplete.Matcher;


public class AutoCompleteAdv implements Matcher<StringWithType, Document>{
	
	HashSet<StringWithType> lib = new HashSet<StringWithType>();
	StringWithType toMatch;

	
	@Override
	public void clear() {
		lib.clear();
	}

	@Override
	public StringWithType getToMatch() {
		return toMatch;
	}

	@Override
	public ArrayList<StringWithType> match(StringWithType toMatch){
		this.toMatch = toMatch;
		if (toMatch.getType()==null)return matchWithNoType(toMatch);
		else return matchWithType(toMatch);
	}

	private ArrayList<StringWithType> matchWithType(StringWithType toMatch) {
		//same type same caps
		ArrayList<StringWithType> suggestions1 = new ArrayList<StringWithType>();
		
		//different type
		ArrayList<StringWithType> suggestions2 = new ArrayList<StringWithType>();
		
		//same type different caps
		ArrayList<StringWithType> suggestions3 = new ArrayList<StringWithType>();
		Iterator<StringWithType> it = lib.iterator();
		while (it.hasNext()){
			StringWithType sugg = it.next();
			if (sugg.getString().length() >= toMatch.getString().length()){
				boolean isExtend = true;
				boolean isCaseEqual = true;
				for (int i= 0; i < toMatch.length(); i++){
					if (toMatch.getString().charAt(i) != sugg.getString().charAt(i)){
						isCaseEqual = false;
						if (Character.toLowerCase(toMatch.getString().charAt(i)) != Character.toLowerCase(sugg.getString().charAt(i))){
							isExtend = false;
							break;
						}
					}
				}
				if (isExtend){
					if (toMatch.getType().equals(sugg.getType())){
						if(isCaseEqual){
							suggestions1.add(sugg);
						}
						if(!isCaseEqual){
							suggestions3.add(sugg);
						}
					}else {
						suggestions2.add(sugg);
					}
				}
			}
		}
		suggestions1.addAll(suggestions3);
		suggestions1.addAll(suggestions2);
		
		return suggestions1;
	}

	
	private ArrayList<StringWithType> matchWithNoType(StringWithType toMatch) {
		
		ArrayList<StringWithType> suggestions1 = new ArrayList<StringWithType>();
		ArrayList<StringWithType> suggestions2 = new ArrayList<StringWithType>();
		Iterator<StringWithType> it = lib.iterator();
		while (it.hasNext()){
			StringWithType sugg = it.next();
			if (sugg.getString().length() >= toMatch.getString().length()){
				boolean isExtend = true;
				boolean isCaseEqual = true;
				for (int i= 0; i < toMatch.length(); i++){
					if (toMatch.getString().charAt(i) != sugg.getString().charAt(i)){
						isCaseEqual = false;
						if (Character.toLowerCase(toMatch.getString().charAt(i)) != Character.toLowerCase(sugg.getString().charAt(i))){
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
	public boolean remove(StringWithType toRemove) {
		return lib.remove(toRemove);
	}
	
	public  void add(ArrayList<Document> docs){
		if (docs==null)return;
		for (Document doc : docs){
			Element root = doc.getDocumentElement();
			add(root);
		}
	}
	
	public  void add(Document doc){
		if (doc ==null)return;
		Element root = doc.getDocumentElement();
		add(root);
		
	}
	
	private  void add(Node el){
		String nodeName = el.getNodeName();
		if (nodeName!="#text"){
			lib.add(new StringWithType(nodeName, StringWithType.TAG));
		}
		NamedNodeMap attributes = el.getAttributes();
		if (attributes!=null){
			for (int i = 0; i < attributes.getLength();i++){
				String attrName = attributes.item(i).getNodeName();
				String attrVal = attributes.item(i).getNodeValue();
				lib.add(new StringWithType(attrName, StringWithType.ATTRIBUTE));
				lib.add(new StringWithType(attrVal, StringWithType.ATTRIBUTEVALUE));
			}
		}
		NodeList childs = el.getChildNodes();
		for (int i=0;i <childs.getLength();i++){
			add(childs.item(i));
		}
		
	}


}
