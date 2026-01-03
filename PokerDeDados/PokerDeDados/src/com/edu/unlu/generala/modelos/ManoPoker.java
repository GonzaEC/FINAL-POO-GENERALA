package com.edu.unlu.generala.modelos;

import java.io.Serializable;

public class ManoPoker implements Serializable {
    private EvaluadorMano cadena;

    public ManoPoker() {
        crearCadena();
    }

    private void crearCadena() {
        EvaluadorMano pokerReal = new EvaluadorPokerReal();       // 7
        EvaluadorMano pokerCuad = new EvaluadorPokerCuadruple();  // 6
        EvaluadorMano full = new EvaluadorFull();                 // 5
        EvaluadorMano escMayor = new EvaluadorEscaleraMayor();    // 4
        EvaluadorMano escMenor = new EvaluadorEscaleraMenor();    // 3
        EvaluadorMano pierna = new EvaluadorPierna();             // 2
        EvaluadorMano parDoble = new EvaluadorParDoble();         // 1
        EvaluadorMano par = new EvaluadorPar();                   // 0

        pokerReal.setSiguiente(pokerCuad);
        pokerCuad.setSiguiente(full);
        full.setSiguiente(escMayor);
        escMayor.setSiguiente(escMenor);
        escMenor.setSiguiente(pierna);
        pierna.setSiguiente(parDoble);
        parDoble.setSiguiente(par);

        this.cadena = pokerReal;
    }

    // Este es el único método que usas desde afuera
    public ResultadoMano evaluar(Vaso vaso) {
        return cadena.procesar(vaso);
    }
}