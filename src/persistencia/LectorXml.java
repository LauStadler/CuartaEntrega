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
        try (XMLDecoder decoder = new XMLDecoder(new BufferedInputStream(new FileInputStream(archivo)))) {
        while (true) {
            try {
                Contacto c = (Contacto) decoder.readObject();
                System.out.println("Cargue el contacto "+ c);
                contactos.addElement(c);
            } catch (ArrayIndexOutOfBoundsException | java.util.NoSuchElementException e) {
                // Fin del archivo XML
                break;
            } catch (Exception e) {
                System.out.println("Error al leer un contacto: " + e.getMessage());
                break;
            }
        }
    }
        System.out.println("Se cargaron " + contactos.size() + " contactos desde el archivo XML.");
        return contactos;
        
    }
}

/*

    try (XMLDecoder decoder = new XMLDecoder(new BufferedInputStream(new FileInputStream(archivo)))) {
        while (true) {
            try {
                Contacto c = (Contacto) decoder.readObject();
                contactos.addElement(c);
            } catch (ArrayIndexOutOfBoundsException | java.util.NoSuchElementException e) {
                // Fin del archivo XML
                break;
            } catch (Exception e) {
                System.out.println("Error al leer un contacto: " + e.getMessage());
                break;
            }
        }
    }

    return contactos;
}

 */