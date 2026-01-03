package com.edu.unlu.generala.utils;

import com.edu.unlu.generala.modelos.RegistroPartida;
import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class Serializador {
    private static final String ARCHIVO = "historial.dat";

    public static void guardar(List<RegistroPartida> historial) {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(ARCHIVO))) {
            oos.writeObject(historial);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static List<RegistroPartida> cargar() {
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(ARCHIVO))) {
            return (List<RegistroPartida>) ois.readObject();
        } catch (Exception e) {
            return new ArrayList<>();
        }
    }
}