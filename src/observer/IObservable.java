package observer;


public interface IObservable<E> {

	
	public abstract void addObserver(Observer<E> o);

	
	public abstract void deleteObserver(Observer<E> o);

	
	public abstract void notifyObservers();

	
	
	public abstract void notifyObservers(E arg);

	
	public abstract void deleteObservers();
	
	
	public abstract int countObservers();

}