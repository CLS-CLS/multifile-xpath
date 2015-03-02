package observer.impl;

import java.util.ArrayList;
import java.util.List;

import observer.IObservable;
import observer.Observer;





public class Observable<E> implements IObservable<E> {
	
	private List<Observer<E>> obs;

	/** Construct an Observable with zero Observers. */

	public Observable() {
		obs = new ArrayList<Observer<E>>();
	}

	/* (non-Javadoc)
	 * @see Observer.impl.Observerable#addObserver(Observer.Observer)
	 */
	public synchronized void addObserver(Observer<E> o) {
		if (o == null)
			throw new NullPointerException();
		if (!obs.contains(o)) {
			obs.add(o);
		}
	}

	/* (non-Javadoc)
	 * @see Observer.impl.Observerable#deleteObserver(Observer.Observer)
	 */
	public synchronized void deleteObserver(Observer<E> o) {
		obs.remove(o);
	}

	/* (non-Javadoc)
	 * @see Observer.impl.Observerable#notifyObservers()
	 */
	public void notifyObservers() {
		notifyObservers(null);
	}

	/* (non-Javadoc)
	 * @see Observer.impl.Observerable#notifyObservers(E)
	 */
	@SuppressWarnings("unchecked")
	public void notifyObservers(E arg) {
		/*
		 * a temporary array buffer, used as a snapshot of the state of
		 * current Observers.
		 */
		Object[] arrLocal;

		synchronized (this) {
			/* We don't want the Observer doing callbacks into
			 * arbitrary code while holding its own Monitor.
			 * The code where we extract each Observable from 
			 * the Vector and store the state of the Observer
			 * needs synchronization, but notifying observers
			 * does not (should not).  The worst result of any 
			 * potential race-condition here is that:
			 * 1) a newly-added Observer will miss a
			 *   notification in progress
			 * 2) a recently unregistered Observer will be
			 *   wrongly notified when it doesn't care
			 */
			
			arrLocal = obs.toArray();
		}

		for (int i = arrLocal.length-1; i>=0; i--)
			((Observer<E>)arrLocal[i]).update(this, arg);
	}

	/* (non-Javadoc)
	 * @see Observer.impl.Observerable#deleteObservers()
	 */
	public synchronized void deleteObservers() {
		obs.clear();
	}

	
	/* (non-Javadoc)
	 * @see Observer.impl.Observerable#countObservers()
	 */
	public synchronized int countObservers() {
		return obs.size();
	}
}
