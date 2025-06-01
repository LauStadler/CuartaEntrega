package persistencia;

import java.beans.XMLDecoder;
import java.io.BufferedInputStream;
import java.io.FileInputStream;
import javax.swing.DefaultListModel;
import modelo.Contacto;

public class LectorXml implements ILector {

    public String nombreArchivo(String nombreUsuario){
        return nombreUsuario + ".xml";
    }

    @Override
    public DefaultListModel<Contacto> cargar(String nombreUsuario) {
        
        DefaultListModel<Contacto> contactos = new DefaultListModel();
        XMLDecoder decoder = new XMLDecoder(new BufferedInputStream(new FileInputStream(nombreArchivo(nombreUsuario))));
        DefaultListModel<Contacto> contactos = (DefaultListModel<Contacto>) decoder.readObject();
        decoder.close();
        return contactos;
    }
}
