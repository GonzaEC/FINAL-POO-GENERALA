package com.edu.unlu.generala.modelos;

public class EvaluadorFull extends EvaluadorMano {
    @Override
    protected boolean esEstaMano(int[] dados) {
        int[] recuento = getRecuento(dados);
        boolean hayTres = false;
        boolean hayDos = false;
        for (int c : recuento) {
            if (c == 3) hayTres = true;
            if (c == 2) hayDos = true;
        }
        return hayTres && hayDos;
    }
    @Override protected String getNombre() { return "Full"; }
    @Override protected int getJerarquia() { return 5; }
}