package persistencia;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

import javax.swing.DefaultListModel;

import modelo.Contacto;

public class LectorTxt implements ILector{
    @Override
    public DefaultListModel<Contacto> cargar (String nombreUsuario) throws IOException {
        String nombreArchivo = nombreUsuario + ".txt";
        DefaultListModel<Contacto> contactos = new DefaultListModel<Contacto>();
        BufferedReader reader = new BufferedReader(new FileReader(nombreArchivo));

        String linea;
        Contacto contacto = null;

        while ((linea = reader.readLine()) != null) {
            if (linea.startsWith("Contacto: ")) {
               
                contacto = new Contacto(linea.substring(10).trim());
                contacto.setTieneChat(false);
                contactos.addElement(contacto);

            } else {
                contacto.setTieneChat(true);
                contacto.getMensajes().add(linea);
            }
        }
        reader.close();
        return contactos;
    }
}
