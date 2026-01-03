package com.edu.unlu.generala.vista;

import com.edu.unlu.generala.modelos.RegistroPartida;
import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import java.awt.*;
import java.util.List;

public class VistaHistorial extends JDialog {

    public VistaHistorial(Frame parent, List<RegistroPartida> historial) {
        super(parent, "Salón de la Fama", true);
        setSize(600, 500);
        setLocationRelativeTo(parent);

        PanelFondo panel = new PanelFondo("src/com/edu/unlu/generala/utils/fondoMesa.jpg");
        panel.setLayout(new BorderLayout());
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        JLabel lblTitulo = new JLabel("🏆 HISTORIAL DE GANADORES 🏆", SwingConstants.CENTER);
        lblTitulo.setFont(new Font("Arial", Font.BOLD, 24));
        lblTitulo.setForeground(new Color(255, 215, 0));

        // Tabla
        String[] col = {"FECHA", "JUGADOR", "PREMIO", "JUGADA"};
        DefaultTableModel modelo = new DefaultTableModel(col, 0);

        for (RegistroPartida r : historial) {
            modelo.addRow(new Object[]{r.getFecha(), r.getNombre(), "$" + r.getPremio(), r.getJugada()});
        }

        JTable tabla = new JTable(modelo);
        tabla.setRowHeight(30);
        tabla.setEnabled(false);

        // Estilos
        JTableHeader header = tabla.getTableHeader();
        header.setBackground(new Color(107, 76, 59));
        header.setForeground(Color.WHITE);

        DefaultTableCellRenderer center = new DefaultTableCellRenderer();
        center.setHorizontalAlignment(JLabel.CENTER);
        for(int i=0; i<tabla.getColumnCount(); i++) tabla.getColumnModel().getColumn(i).setCellRenderer(center);

        JScrollPane scroll = new JScrollPane(tabla);
        scroll.getViewport().setBackground(new Color(255, 255, 255, 200));

        JButton btnCerrar = new BotonRedondeado("Cerrar", 40);
        btnCerrar.setBackground(new Color(178, 34, 34));
        btnCerrar.setForeground(Color.WHITE);
        btnCerrar.addActionListener(e -> dispose());
        JPanel pBtn = new JPanel(); pBtn.setOpaque(false); pBtn.add(btnCerrar);

        panel.add(lblTitulo, BorderLayout.NORTH);
        panel.add(scroll, BorderLayout.CENTER);
        panel.add(pBtn, BorderLayout.SOUTH);
        add(panel);
    }
}