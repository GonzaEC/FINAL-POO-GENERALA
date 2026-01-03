package com.edu.unlu.generala.modelos;

public class EvaluadorEscaleraMayor extends EvaluadorMano {
    @Override
    protected boolean esEstaMano(int[] dados) {
        int[] recuento = getRecuento(dados);
        // Indices 1 al 5 deben tener 1 dado cada uno
        return recuento[1] == 1 && recuento[2] == 1 && recuento[3] == 1 &&
                recuento[4] == 1 && recuento[5] == 1;
    }
    @Override protected String getNombre() { return "Escalera Mayor"; }
    @Override protected int getJerarquia() { return 4; }
}