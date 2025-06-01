package persistencia;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.swing.DefaultListModel;

import modelo.Contacto;

public class GuardadorTxt implements IGuardador {
   
   
    @Override
    public void guardar(DefaultListModel<Contacto> contactos, String nombreUsuario) throws IOException {
        String nombreArchivo = nombreUsuario + ".txt";
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(nombreArchivo))) {
            for (Contacto c : contactos) {
                writer.write("Contacto: " + c.getNickname() + "\n");
                for (String mensaje : c.getMensajes()) {
                    writer.write(mensaje + "\n");
                }
            }
        }
    }

}
