package com.edu.unlu.generala.observer;

public interface Observable {
    public void notificar(Object objecto);
    public void agregarObservador(Observable observador);
}
