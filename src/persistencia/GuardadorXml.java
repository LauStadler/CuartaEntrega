package persistencia;
import java.beans.XMLEncoder;
import javax.swing.DefaultListModel;
import modelo.Contacto;
import java.io.BufferedOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;



public class GuardadorXml implements IGuardador {

    public String nombreArchivo(String nombreUsuario){
        return nombreUsuario + ".xml";
    }
    
    public void guardar(DefaultListModel<Contacto> contactos, String nombreUsuario) throws IOException{
        
        XMLEncoder encoder = new XMLEncoder(new BufferedOutputStream(new FileOutputStream(nombreArchivo(nombreUsuario))));
        for (int i = 0; i < contactos.size(); i++) {
            Contacto c = contactos.getElementAt(i);
            System.out.println(contactos.getElementAt(i).toString());
            // Write the Contacto object to the XML file
            encoder.writeObject(c);
        }
        encoder.close();
    }
}