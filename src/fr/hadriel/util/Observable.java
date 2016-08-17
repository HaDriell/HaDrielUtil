package fr.hadriel.util;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by glathuiliere on 16/08/2016.
 */
public class Observable {

    private List<IObserver> observers = new ArrayList<>();
    private IObserver[] cache = null;

    public synchronized void addObserver(IObserver observer) {
        if(!observers.contains(observer))
            observers.add(observer);
        cache = null;
    }

    public synchronized void removeObserver(IObserver observer) {
        observers.remove(observer);
        cache = null;
    }

    public void notifyObservers() {
        synchronized (observers) {
            if(cache == null) {
                cache = new IObserver[observers.size()];
                observers.toArray(cache);
            }
        }

        for(IObserver observer : cache) {
            observer.update(this);
        }
    }
}