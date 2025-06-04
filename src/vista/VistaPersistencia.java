package vista;
import javax.swing.*;
import java.awt.FlowLayout;
import java.awt.Color;
import java.awt.Component;

public class VistaPersistencia {
    public static void main(String[] args) {
        // Crear ventana
        JFrame frame = new JFrame("Configuración de Persistencia");
        frame.getContentPane().setBackground(new Color(255, 192, 203));
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(361, 200);
        frame.setLocationRelativeTo(null); // Centrar ventana

        // Panel principal con BoxLayout vertical
        JPanel panel = new JPanel();
        panel.setBackground(new Color(255, 192, 203));
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));

        // Título centrado
        JLabel titulo = new JLabel("Seleccione el medio de persistencia");
        titulo.setAlignmentX(JLabel.CENTER_ALIGNMENT);

        // Espacio
        panel.add(Box.createVerticalStrut(20));
        panel.add(titulo);
        panel.add(Box.createVerticalStrut(20));

        // ComboBox con opciones
        String[] opciones = {"JSON", "XML", "Texto"};
        frame.getContentPane().setLayout(new FlowLayout(FlowLayout.CENTER, 5, 5));
        JComboBox<String> comboBox = new JComboBox<>(opciones);
        comboBox.setAlignmentX(JComboBox.CENTER_ALIGNMENT);
        panel.add(comboBox);
        Component verticalStrut = Box.createVerticalStrut(20);
        verticalStrut.setForeground(new Color(255, 192, 203));
        panel.add(verticalStrut);

        // Botón aceptar
        JButton boton = new JButton("Aceptar");
        boton.setAlignmentX(JButton.CENTER_ALIGNMENT);
        panel.add(boton);

        // Agregar panel al frame y mostrar
        frame.getContentPane().add(panel);
        frame.setVisible(true);
    }
}