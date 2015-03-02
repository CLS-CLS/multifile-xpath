package customFileChooser.impl;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import customFileChooser.IBookmarks;


public class BookMarks implements IBookmarks{
	
	Map<String, Bookmark> bookmarks = new HashMap<String, Bookmark>();
	
		
	@Override
	public void addBookmark(String name, String path){
		bookmarks.put(name,new Bookmark(name, path));
	}
	
	@Override
	public void removeBookmark(String name){
		bookmarks.remove(name);
	}

	@Override
	public Map<String, Bookmark> getBookmarks() {
		return bookmarks;
	}
	
	@Override
	public String getBookmarksInString(){
		StringBuffer result = new StringBuffer();
		Iterator<String> iterator = bookmarks.keySet().iterator();
		while(iterator.hasNext()){
			String bookmarkname = iterator.next();
			Bookmark bk = bookmarks.get(bookmarkname);
			result.append(bk.getName()).append(",").append(bk.getPath()).append(",");
		}
		String resultString = result.toString();
		if (resultString.length()<=1)return "";
		return result.toString().substring(0, result.toString().length()-1);
	}
	
	@Override
	public void setBookmarks(String bookmarksInString){
		if (bookmarksInString==null)return;
		String[] bookmarks = bookmarksInString.split(",");
		for (int i=0; i<bookmarks.length-1;i++){
			if (i%2==0){
				this.bookmarks.put(bookmarks[i], new Bookmark(bookmarks[i], bookmarks[i+1]));
			}
		}
	
	}

	@Override
	public void setBookmarks(Map<String, Bookmark> bookmarks) {
		this.bookmarks = bookmarks;
	}
	
	

}
