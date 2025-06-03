package persistencia;

import javax.swing.DefaultListModel;
import modelo.Contacto;
import java.io.IOException;

public interface ILector {
   public DefaultListModel<Contacto> cargar(String nombreUsuario) throws IOException;
}
