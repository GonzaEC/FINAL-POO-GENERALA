package com.edu.unlu.generala.modelos;

import java.io.Serializable;

public class ManoPoker implements Serializable {
    private Vaso vaso;

    public ManoPoker(Vaso vaso) {
        this.vaso = vaso;
    }

    public int[] construirRecuento(int[] valores){
        int[] recuento = new int [6];

        for (int valor : valores){
            recuento[valor-1] ++;
        }
        return recuento;
    }

    public int verificarMano(int[] valores) {
        int[] recuento = construirRecuento(valores);

        if (esPokerReal(recuento)) return 7;
        if (esPokerCuadruple(recuento)) return 6;
        if (esFull(recuento)) return 5;
        if (esEscaleraMayor(recuento)) return 4;
        if (esEscaleraMenor(recuento)) return 3;
        if (esPierna(recuento)) return 2;
        if (esParDoble(recuento)) return 1;
        if (esPar(recuento)) return 0;

        return -1;
    }
    public int desempatar(int[] valores1, int[] valores2) {
        int[] recuento1 = construirRecuento(valores1);
        int[] recuento2 = construirRecuento(valores2);


        for (int i = recuento1.length - 1; i >= 0; i--) {
            if (recuento1[i] > recuento2[i]) {
                return 1;
            } else if (recuento1[i] < recuento2[i]) {
                return -1;
            }
        }
        return 0;
    }
    private boolean esPar(int[] recuento) {
        for(int recuentos : recuento) {
            if (recuentos == 2){
                return true;
            }
        }
        return false;
    }

    private boolean esParDoble(int[] recuento) {
        int pares = 0;
        for(int recuentos : recuento) {
            if (recuentos == 2){
                pares++;
            }
        }
        return pares == 2;
    }

    private boolean esPierna(int[] recuento) {
        for(int recuentos : recuento) {
            if (recuentos == 3){
                return true;
            }
        }
        return false;
    }

    private boolean esEscaleraMenor(int[] recuento) {
        return recuento[0] == 1 && recuento[1] == 1 && recuento[2] == 1 && recuento[3] == 1 && recuento[4] == 1;
    }

    private boolean esEscaleraMayor(int[] recuento) {
        return recuento[1] == 1 && recuento[2] == 1 && recuento[3] == 1 && recuento[4] == 1 && recuento[5] == 1;
    }

    private boolean esFull(int[] recuento) {
        boolean resultado1 = false;
        boolean resultado2 = false;
        for(int recuentos : recuento) {
            if (recuentos == 3){
                resultado1 = true;
            }
            if(recuentos==2){
                resultado2 = true;
            }
        }
        return resultado1 && resultado2;
    }

    private boolean esPokerCuadruple(int[] recuento) {
        for(int recuentos : recuento) {
            if (recuentos == 4){
                return true;
            }
        }
        return false;
    }

    private boolean esPokerReal(int[] recuento) {
        for(int recuentos : recuento) {
            if (recuentos == 5){
                return true;
            }
        }
        return false;
    }
}
