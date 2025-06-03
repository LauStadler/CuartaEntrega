package persistencia;

import javax.swing.DefaultListModel;
import modelo.Contacto;

import java.io.IOException;

public interface IGuardador {
    public void guardar(DefaultListModel<Contacto> contactos, String nombreUsuario) throws IOException;
}