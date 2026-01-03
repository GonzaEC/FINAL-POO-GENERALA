package com.edu.unlu.generala.modelos;

public class EvaluadorEscaleraMenor extends EvaluadorMano {
    @Override
    protected boolean esEstaMano(int[] dados) {
        int[] recuento = getRecuento(dados);
        // Indices 0 al 4 deben tener 1 dado cada uno
        return recuento[0] == 1 && recuento[1] == 1 && recuento[2] == 1 &&
                recuento[3] == 1 && recuento[4] == 1;
    }
    @Override protected String getNombre() { return "Escalera Menor"; }
    @Override protected int getJerarquia() { return 3; }
}