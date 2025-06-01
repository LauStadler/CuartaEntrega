package persistencia;

import javax.swing.DefaultListModel;
import modelo.Contacto;

public interface ILector {
   public DefaultListModel<Contacto> cargar(String nombreUsuario);
}
