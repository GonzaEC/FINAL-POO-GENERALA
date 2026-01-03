package com.edu.unlu.generala.vista;

import com.edu.unlu.generala.controladores.ControladorGenerala;
import com.edu.unlu.generala.modelos.Jugador;
import com.edu.unlu.generala.modelos.RegistroPartida;

import javax.swing.*;
import javax.swing.text.DefaultCaret;
import java.awt.*;
import java.rmi.RemoteException;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;


public class InterfazConsola extends JFrame implements IVista {
    private JPanel panelPrincipal;
    private JTextArea txtSalida;
    private JTextField txtEntrada;
    private JButton btnEnter;

    private EstadoVistaConsola estadoActual;
    private ControladorGenerala controlador;

    public InterfazConsola(ControladorGenerala controlador) {
        super("Generala - Consola");
        this.controlador = controlador;

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(700, 600);
        setLocationRelativeTo(null);

        panelPrincipal = new JPanel(new BorderLayout());

        txtSalida = new JTextArea();
        txtSalida.setEditable(false);
        txtSalida.setFont(new Font("Monospaced", Font.PLAIN, 14)); // Fuente legible
        txtSalida.setBackground(Color.WHITE);
        txtSalida.setForeground(Color.BLACK);
        txtSalida.setMargin(new Insets(10,10,10,10));

        DefaultCaret caret = (DefaultCaret)txtSalida.getCaret();
        caret.setUpdatePolicy(DefaultCaret.ALWAYS_UPDATE);

        JScrollPane scrollPane = new JScrollPane(txtSalida);
        panelPrincipal.add(scrollPane, BorderLayout.CENTER);

        JPanel panelInferior = new JPanel(new BorderLayout());
        txtEntrada = new JTextField();
        txtEntrada.setFont(new Font("Monospaced", Font.PLAIN, 14));

        btnEnter = new JButton("Enviar");

        panelInferior.add(txtEntrada, BorderLayout.CENTER);
        panelInferior.add(btnEnter, BorderLayout.EAST);
        panelPrincipal.add(panelInferior, BorderLayout.SOUTH);

        setContentPane(panelPrincipal);

        btnEnter.addActionListener(e -> enviarTexto());
        txtEntrada.addActionListener(e -> enviarTexto());

        mostrarMenuPrincipal();
    }

    private void enviarTexto() {
        String texto = txtEntrada.getText().trim();
        mostrarMensaje("> " + texto);

        try {
            procesarEntrada(texto);
        } catch (RemoteException e) {
            mostrarMensaje("Error de conexión: " + e.getMessage());
        } catch (Exception e) {
            mostrarMensaje("Error: " + e.getMessage());
        }
        txtEntrada.setText("");
        txtEntrada.requestFocus();
    }

    private void procesarEntrada(String entrada) throws RemoteException {
        if (estadoActual == EstadoVistaConsola.ESPERANDO_TURNO) {
            mostrarMensaje("❌ No es tu turno. Por favor espera a que el otro jugador termine.");
            return;
        }

        switch (estadoActual) {
            case MENU_PRINCIPAL:
                procesarEntradaMenuPrincipal(entrada);
                break;
            case AGREGAR_JUGADOR:
                procesarAgregarJugador(entrada);
                break;
            case MENU_JUGADOR_DADOS:
                procesarEntradaMenuJugadorDados(entrada);
                break;
            case INGRESA_INDICES_RETIRO:
                procesarSeleccionDados(entrada);
                break;
            case MENU_JUGADOR_APUESTAS:
                procesarEntradaMenuApuestas(entrada);
                break;
            case SUBIR_APUESTA:
                procesarSubidaApuesta(entrada);
                break;
            case FIN_PARTIDA:
                mostrarMenuPrincipal();
                break;
        }
    }

    //menu
    private void mostrarMenuPrincipal() {
        estadoActual = EstadoVistaConsola.MENU_PRINCIPAL;
        limpiarPantalla();
        mostrarMensaje("=== GENERALA - MENÚ PRINCIPAL ===");
        mostrarMensaje("1. Agregar jugador");
        mostrarMensaje("2. Ver jugadores inscritos");
        mostrarMensaje("3. Iniciar Partida");
        mostrarMensaje("4. Ver Historial de Victorias (Salon de la Fama)");
        mostrarMensaje("0. Salir");
        mostrarMensaje("---------------------------------");
        mostrarMensaje("Seleccione una opción:");
    }

    private void procesarEntradaMenuPrincipal(String entrada) throws RemoteException {
        switch (entrada) {
            case "1":
                mostrarMensaje("Ingrese el nombre del nuevo jugador:");
                estadoActual = EstadoVistaConsola.AGREGAR_JUGADOR;
                break;
            case "2":
                mostrarMensaje("--- Jugadores ---");
                List<Jugador> lista = controlador.getJugadores();
                if(lista.isEmpty()) mostrarMensaje("(Nadie todavía)");
                for (Jugador j : lista) mostrarMensaje("- " + j.getNombre());
                mostrarMensaje("\n[Presione Enter para volver]");
                break;
            case "3":
                if (controlador.cantidadJugadores() < 2) {
                    mostrarMensaje("⚠️ Error: Se necesitan minimo 2 jugadores.");
                } else {
                    controlador.iniciar();
                }
                break;
            case "4":
                mostrarHistorial();
                break;
            case "0":
                System.exit(0);
                break;
            default:
                mostrarMenuPrincipal();
        }
    }

    private void mostrarHistorial() {
        mostrarMensaje("\n=== SALON DE LA FAMA ===");
        List<RegistroPartida> historial = controlador.getHistorial();

        if (historial.isEmpty()) {
            mostrarMensaje("No hay partidas registradas aun.");
        } else {
            for (RegistroPartida r : historial) {
                mostrarMensaje(String.format("📅 %s | 🏆 %s | 💰 $%d | 🎲 %s",
                        r.getFecha(),
                        r.getNombre(),
                        r.getPremio(),
                        r.getJugada()));
            }
        }
        mostrarMensaje("\n[Presione Enter para volver al menú]");
    }

    private void procesarAgregarJugador(String nombre) {
        if (!nombre.isEmpty()) {
            if (!controlador.existeJugador(nombre)) {
                controlador.agregarJugador(nombre);
                controlador.setNombreJugadorLocal(nombre);
                mostrarMensaje("✅ Jugador '" + nombre + "' agregado.");
                mostrarMenuPrincipal();
            } else {
                mostrarMensaje("⚠️ Nombre ocupado. Intente otro:");
            }
        } else {
            mostrarMenuPrincipal();
        }
    }

    // Dados

    @Override
    public void mostrarMenuJugadorDados() {
        SwingUtilities.invokeLater(() -> {
            limpiarPantalla();
            Jugador actual = controlador.getJugadorActual();

            if (!controlador.esMiTurno()) {
                estadoActual = EstadoVistaConsola.ESPERANDO_TURNO;
                mostrarMensaje("=======================================");
                mostrarMensaje("       TURNO DE: " + actual.getNombre().toUpperCase());
                mostrarMensaje("=======================================");
                mostrarMensaje("⏳ Esperando que el oponente juegue...");
                return;
            }

            estadoActual = EstadoVistaConsola.MENU_JUGADOR_DADOS;
            int tiradas = controlador.getTiradasRestantes();

            mostrarMensaje("=======================================");
            mostrarMensaje("       ES TU TURNO: " + actual.getNombre().toUpperCase());
            mostrarMensaje("=======================================");
            mostrarMensaje("Tiradas restantes: " + tiradas);


            if (tiradas == 2) {
                mostrarMensaje("Dados listos para tirar:");
                imprimirDadosBonitos(new int[]{1, 1, 1, 1, 1});
            } else {
                try {
                    int[] dados = controlador.getJugadorLocalObjeto().getVasoJugador().getValores();
                    mostrarMensaje("Tus dados actuales:");
                    imprimirDadosBonitos(dados);
                } catch (Exception e) {}
            }


            mostrarMensaje("");
            if (tiradas == 2) {
                mostrarMensaje("[1] Tirar Dados");
            } else {
                mostrarMensaje("[1] Tirar todos de nuevo");
                mostrarMensaje("[2] Elegir cuáles volver a tirar");
                mostrarMensaje("[3] Plantarse");
            }
            mostrarMensaje("Ingrese opcion:");
        });
    }

    private void procesarEntradaMenuJugadorDados(String entrada) throws RemoteException {
        if (!controlador.esMiTurno()) {
            mostrarMensaje("❌ No podés jugar, no es tu turno.");
            return;
        }

        int tiradas = controlador.getTiradasRestantes();

        if (tiradas == 2) {
            controlador.tirarTodosDados();
            return;
        }

        switch (entrada) {
            case "1": controlador.tirarTodosDados(); break;
            case "2":
                mostrarMensaje("Escriba las posiciones (1 al 5) de los dados que quiere VOLVER A TIRAR:");
                mostrarMensaje("(Ejemplo: 1 3 5)");
                estadoActual = EstadoVistaConsola.INGRESA_INDICES_RETIRO;
                break;
            case "3": controlador.plantarse(); break;
            default: mostrarMensaje("Opcion incorrecta.");
        }
    }

    private void procesarSeleccionDados(String entrada) throws RemoteException {
        try {
            List<Integer> indices = Arrays.stream(entrada.trim().split("\\s+"))
                    .map(Integer::parseInt)
                    .map(i -> i - 1)
                    .filter(i -> i >= 0 && i < 5)
                    .collect(Collectors.toList());

            if(indices.isEmpty()) {
                mostrarMensaje("Selección vacia. Intente de nuevo:");
            } else {
                controlador.tirarDadosSeleccionados(indices);
            }
        } catch (Exception e) {
            mostrarMensaje("Formato incorrecto. Use solo números y espacios.");
        }
    }

    //apuesta

    @Override
    public void irAVentanaMenuApuestas() { mostrarMenuApuestas(); }

    @Override
    public void mostrarMenuApuestas() {
        SwingUtilities.invokeLater(() -> {
            limpiarPantalla();
            Jugador actual = controlador.getJugadorActual();

            if (!controlador.esMiTurno()) {
                estadoActual = EstadoVistaConsola.ESPERANDO_TURNO;
                mostrarMensaje("$$$ FASE DE APUESTAS $$$");
                mostrarMensaje("Esperando que " + actual.getNombre() + " decida...");
                return;
            }

            estadoActual = EstadoVistaConsola.MENU_JUGADOR_APUESTAS;
            try {
                mostrarMensaje("$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$");
                mostrarMensaje("      R O N D A   D E   A P U E S T A S");
                mostrarMensaje("$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$");
                mostrarMensaje("Saldo: $" +controlador.getJugadorActual().getSaldo());
                mostrarMensaje("Pozo: $" + controlador.getPozo());
                mostrarMensaje("Apuesta Máxima: $" + controlador.getApuestaMaxima());
                mostrarMensaje("Tu apuesta: $" + actual.getApostado());
                mostrarMensaje("");
                mostrarMensaje("[1] Igualar / Pasar");
                mostrarMensaje("[2] Subir Apuesta");
                mostrarMensaje("[3] No ir (Fold) / Plantarse");
                mostrarMensaje("Opción:");
            } catch (RemoteException e) {
                mostrarMensaje("Error de datos.");
            }
        });
    }

    private void procesarEntradaMenuApuestas(String entrada) throws RemoteException {
        if (!controlador.esMiTurno()) return;

        switch (entrada) {
            case "1": controlador.igualarApuesta(); break;
            case "2":
                mostrarMensaje("Ingrese el MONTO a subir:");
                estadoActual = EstadoVistaConsola.SUBIR_APUESTA;
                break;
            case "3": controlador.plantarseApuesta(); break;
            default: mostrarMensaje("Opción incorrecta.");
        }
    }

    private void procesarSubidaApuesta(String entrada) throws RemoteException {
        try {
            int monto = Integer.parseInt(entrada);
            controlador.subirApuesta(String.valueOf(monto));
        } catch (NumberFormatException e) {
            mostrarMensaje("Debe ingresar un número.");
        }
    }

    //outputs
    @Override
    public void mostrarTirada(int[] valores) {
        SwingUtilities.invokeLater(() -> {
            mostrarMensaje("");
            mostrarMensaje("🎲 ¡Se han tirado los dados! Resultados:");
            imprimirDadosBonitos(valores);
            mostrarMensaje("");
            mostrarMenuJugadorDados();
        });
    }

    private void imprimirDadosBonitos(int[] valores) {
        StringBuilder sb = new StringBuilder();
        for (int v : valores) {
            sb.append("[").append(v).append("]  ");
        }
        mostrarMensaje(sb.toString());
    }

    @Override
    public void mostrarGanador() {
        SwingUtilities.invokeLater(() -> {
            try {
                limpiarPantalla();
                estadoActual = EstadoVistaConsola.FIN_PARTIDA;
                Jugador g = controlador.determinarGanador();
                String nombre = (g != null) ? g.getNombre() : "Nadie";

                mostrarMensaje("**********************************");
                mostrarMensaje("       ¡FIN DE LA PARTIDA!");
                mostrarMensaje("**********************************");
                mostrarMensaje("");
                mostrarMensaje("🏆 GANADOR: " + nombre);
                mostrarMensaje("💰 BOTE: $" + controlador.getPozo());
                mostrarMensaje("");
                mostrarMensaje("[Presione Enter para volver al Menú]");
            } catch (Exception e) { e.printStackTrace(); }
        });
    }

    @Override
    public void mostrarMensaje(String msg) {
        println(msg);
    }

    @Override
    public void actualizarVista() {
        mostrarMenuJugadorDados();
    }

    @Override
    public void limpiarMesa() {
        mostrarMensaje("\n--- Nueva Ronda ---\n");
    }

    @Override
    public void setControlador(ControladorGenerala controlador) {
        this.controlador = controlador;
    }

    @Override
    public void iniciar() {
        setVisible(true);
    }

    private void println(String s) {
        txtSalida.append(s + "\n");
        txtSalida.setCaretPosition(txtSalida.getDocument().getLength());
    }

    private void limpiarPantalla() {
        txtSalida.setText("");
    }
}