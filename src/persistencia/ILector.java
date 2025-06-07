package persistencia;

import javax.swing.DefaultListModel;
import modelo.Contacto;
import java.io.IOException;

public interface ILector {
   public DefaultListModel<Contacto> cargarContactos(String nombreUsuario) throws IOException;
   public DefaultListModel<String> cargarNicksContactos(DefaultListModel<Contacto> contactos) throws IOException;
   public DefaultListModel<String> cargarNicksChats(DefaultListModel<Contacto> contactos) throws IOException;
}
