package customFileChooser;

import java.util.Map;
import customFileChooser.impl.Bookmark;

public interface IBookmarks{

	public void addBookmark(String name, String path);

	public void removeBookmark(String name);

	public Map<String, Bookmark> getBookmarks();

	public String getBookmarksInString();

	public void setBookmarks(String bookmarksInString);

	void setBookmarks(Map<String, Bookmark> bookmarks);

}