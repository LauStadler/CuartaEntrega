package persistencia;

import java.beans.XMLDecoder;
import java.io.BufferedInputStream;
import java.io.FileInputStream;
import javax.swing.DefaultListModel;
import modelo.Contacto;
import java.io.IOException;
import java.io.File;

public class LectorXml extends AbstractLector implements ILector {

    public String nombreArchivo(String nombreUsuario){
        return nombreUsuario + ".xml";
    }

    @Override
    public DefaultListModel<Contacto> cargarContactos (String nombreUsuario) throws IOException{    
        DefaultListModel<Contacto> contactos = new DefaultListModel<Contacto>();
        File archivo = new File(nombreArchivo(nombreUsuario)); // ESTO NO CREA ARCHIVO, crea el objeto ruta a archivo a ver si existe
        
        if (!archivo.exists()){
            System.out.println("El archivo " + nombreArchivo(nombreUsuario) + " no existe.");
        }
        try ( XMLDecoder decoder = new XMLDecoder(new BufferedInputStream(new FileInputStream(nombreArchivo(nombreUsuario))))){
            contactos = (DefaultListModel<Contacto>) decoder.readObject();
            decoder.close();
        }
        catch (IOException e) {
            System.out.println("Error al leer el archivo " + nombreArchivo(nombreUsuario) + ": " + e.getMessage());
            
        }
        return contactos;
    }
}
