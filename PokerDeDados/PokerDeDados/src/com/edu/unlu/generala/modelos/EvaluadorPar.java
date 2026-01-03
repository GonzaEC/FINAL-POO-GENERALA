package com.edu.unlu.generala.modelos;

public class EvaluadorPar extends EvaluadorMano {
    @Override
    protected boolean esEstaMano(int[] dados) {
        int[] recuento = getRecuento(dados);
        for (int c : recuento) if (c == 2) return true;
        return false;
    }
    @Override protected String getNombre() { return "Par"; }
    @Override protected int getJerarquia() { return 0; }
}