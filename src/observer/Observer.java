package observer;


public interface Observer<E> {

	void update(IObservable<E> observable, E arg);

}
