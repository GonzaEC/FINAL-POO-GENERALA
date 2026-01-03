package com.edu.unlu.generala.modelos;

public class EvaluadorPokerReal extends EvaluadorMano {
    @Override
    protected boolean esEstaMano(int[] dados) {
        int[] recuento = getRecuento(dados);
        for (int c : recuento) {
            if (c == 5) return true;
        }
        return false;
    }
    @Override protected String getNombre() { return "Generala / Poker Real"; }
    @Override protected int getJerarquia() { return 7; }
}