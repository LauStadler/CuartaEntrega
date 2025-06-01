package persistencia;
import java.beans.XMLEncoder;
import java.swing.DefaultListModel;
import modelo.Contacto;

public class GuardadorXml implements IGuardador {

    public String nombreArchivo(String nombreUsuario){
        return nombreUsuario + ".xml";
    }
    
    public void guardar(DefaultListModel<Contacto> contactos, String nombreUsuario){
        
        XMLEncoder encoder = new XMLEncoder(new BufferedOutputStream(new FileOutputStream(nombreArchivo(nombreUser))));
        for (Contacto c: contactos){
         encoder.writeObject(c);
        }
        encoder.close();
    }
}
