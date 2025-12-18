package com.edu.unlu.generala.modelos;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Vaso implements Serializable {
    private Dado dados[];
    private static final int CANTIDAD_DADOS = 5;


    public Vaso() {
        dados = new Dado[CANTIDAD_DADOS];
        for (int i = 0; i < CANTIDAD_DADOS; i++) {
            dados[i] = new Dado();
        }
    }

    //metodos
    public void lanzarDados(){
        for (Dado dado : dados) {
            dado.tirar();
        }
    }
    public void lanzarSeleccionados(List<Integer> indices) {
        for (Integer indice : indices) {
            if (indice >= 0 && indice < dados.length) {
                dados[indice].tirar();
            }
        }
    }
    public List<Dado> getDados() {
        return new ArrayList<>(Arrays.asList(dados));
    }
    public int verDado(int dado){
        if (dado > 0 && dado <= dados.length){
            return dados[dado-1].getCara();
        }
        else{
            return -1;
        }
    }

    public int[] getValores() {
        int[] valores = new int[dados.length];
        for (int i = 0; i < dados.length; i++) {
            valores[i] = dados[i].getCara();
        }
        return valores;
    }




}
