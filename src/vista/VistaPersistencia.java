package vista;
import controlador.Controlador;
import java.awt.Color;
import java.awt.Component;
import java.awt.FlowLayout;
import javax.swing.*;

public class VistaPersistencia extends javax.swing.JFrame  {
	
    private JButton boton;
    private JComboBox<String> comboBox;
	JFrame frame;
    JPanel panel;
    JLabel titulo;
    String[] opciones = {"JSON", "XML", "Texto"};
    
    public VistaPersistencia() {


    	
        // Crear ventana
        frame = new JFrame("Configuración de Persistencia");
        frame.getContentPane().setBackground(new Color(255, 192, 203));
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(361, 200);
        frame.setLocationRelativeTo(null); // Centrar ventana

        // Panel principal con BoxLayout vertical
        panel = new JPanel();
        panel.setBackground(new Color(255, 192, 203));
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));

        // Título centrado
        titulo = new JLabel("Seleccione el medio de persistencia");
        titulo.setAlignmentX(JLabel.CENTER_ALIGNMENT);

        // Espacio
        panel.add(Box.createVerticalStrut(20));
        panel.add(titulo);
        panel.add(Box.createVerticalStrut(20));

        // ComboBox con opciones
        
        frame.getContentPane().setLayout(new FlowLayout(FlowLayout.CENTER, 5, 5));
        comboBox = new JComboBox<>(opciones);
        comboBox.setAlignmentX(JComboBox.CENTER_ALIGNMENT);
        panel.add(comboBox);
        Component verticalStrut = Box.createVerticalStrut(20);
        verticalStrut.setForeground(new Color(255, 192, 203));
        panel.add(verticalStrut);

        // Botón aceptar
        boton = new JButton("Aceptar");
        boton.setActionCommand("Aceptar");
        boton.setAlignmentX(JButton.CENTER_ALIGNMENT);
        panel.add(boton);
        // Agregar panel al frame y mostrar
        frame.getContentPane().add(panel);
        //frame.setVisible(true);
    }

    public void setControlador(Controlador c){
		this.boton.addActionListener(c);
	}

    public String getModoSeleccionado(){
        
        String modoSeleccionado = (String) this.comboBox.getSelectedItem();
        System.out.println("Modo de persistencia seleccionado: " + modoSeleccionado);
        return modoSeleccionado;
    }

    public void setVisible(boolean visible) {
        this.frame.setVisible(visible);
    }
    public void dispose() {
        this.frame.dispose();
    }
}