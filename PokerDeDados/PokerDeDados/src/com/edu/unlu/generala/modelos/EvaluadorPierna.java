package com.edu.unlu.generala.modelos;

public class EvaluadorPierna extends EvaluadorMano {
    @Override
    protected boolean esEstaMano(int[] dados) {
        int[] recuento = getRecuento(dados);
        for (int c : recuento) if (c == 3) return true;
        return false;
    }
    @Override protected String getNombre() { return "Pierna"; }
    @Override protected int getJerarquia() { return 2; }
}