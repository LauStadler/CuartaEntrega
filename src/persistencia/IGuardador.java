package persistencia;

import javax.swing.DefaultListModel;
import modelo.Contacto;

public interface IGuardador {
    public void guardar(DefaultListModel<Contacto> contactos, String nombreUsuario);
}
