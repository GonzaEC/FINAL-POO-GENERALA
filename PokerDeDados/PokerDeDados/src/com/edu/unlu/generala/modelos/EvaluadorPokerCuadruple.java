package com.edu.unlu.generala.modelos;

public class EvaluadorPokerCuadruple extends EvaluadorMano {
    @Override
    protected boolean esEstaMano(int[] dados) {
        int[] recuento = getRecuento(dados);
        for (int c : recuento) {
            if (c == 4) return true;
        }
        return false;
    }
    @Override protected String getNombre() { return "Poker"; }
    @Override protected int getJerarquia() { return 6; }
}