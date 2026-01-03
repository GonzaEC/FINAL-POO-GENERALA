package com.edu.unlu.generala.modelos;

public class EvaluadorParDoble extends EvaluadorMano {
    @Override
    protected boolean esEstaMano(int[] dados) {
        int[] recuento = getRecuento(dados);
        int paresEncontrados = 0;
        for (int c : recuento) {
            if (c == 2) paresEncontrados++;
        }
        return paresEncontrados == 2;
    }
    @Override protected String getNombre() { return "Par Doble"; }
    @Override protected int getJerarquia() { return 1; }
}