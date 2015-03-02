package autoComplete;

import java.util.ArrayList;

public interface Matcher<E,T> {
	
	public void add(ArrayList<T> fromDocs);
	public void add(T doc);
	public boolean remove(E toRemove);
	public void clear();
	public ArrayList<E> match(E toMatch);
	public E getToMatch();
		
	
}
