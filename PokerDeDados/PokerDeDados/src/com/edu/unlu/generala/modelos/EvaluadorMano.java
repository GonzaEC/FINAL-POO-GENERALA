package com.edu.unlu.generala.modelos;

import com.sun.source.tree.BreakTree;

public abstract class EvaluadorMano {
    protected EvaluadorMano siguiente;

    public void setSiguiente(EvaluadorMano siguiente){
        this.siguiente = siguiente;
    }

    public ResultadoMano procesar(Vaso vaso){
        int[]valores = vaso.getValores();

        if (esEstaMano(valores)) {
            return new ResultadoMano(getNombre(), getJerarquia(), sumar(valores));
        }

        else if (siguiente != null) {
            return siguiente.procesar(vaso);
        }

        return new ResultadoMano("Nada", 0, 0);
    }


    protected abstract boolean esEstaMano(int[] valores);
    protected abstract String getNombre();
    protected abstract int getJerarquia();

    protected int sumar(int[] dados) {
        int suma = 0;
        for (int i : dados) suma += i;
        return suma;
    }

    protected int[] getRecuento(int[] dados) {
        int[] recuento = new int[6];
        for (int valor : dados) {
            if (valor >= 1 && valor <= 6) {
                recuento[valor - 1]++;
            }
        }
        return recuento;
    }


}


