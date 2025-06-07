package persistencia;

import java.io.IOException;

import javax.swing.DefaultListModel;

import modelo.Contacto;

public abstract class AbstractLector implements ILector{

    public abstract DefaultListModel<Contacto> cargarContactos(String nombreUsuario) throws IOException;

    public DefaultListModel<String> cargarNicksContactos(DefaultListModel<Contacto> contactos) {
        DefaultListModel<String> nicks = new DefaultListModel<>();
        for (int i = 0; i < contactos.size(); i++) {
            Contacto contacto = contactos.get(i);
            nicks.addElement(contacto.getNickname());
        }
        return nicks;
    }
    public DefaultListModel<String> cargarNicksChats(DefaultListModel<Contacto> contactos) {
        DefaultListModel<String> nicks = new DefaultListModel<>();
        for (int i = 0; i < contactos.size(); i++) {
            Contacto contacto = contactos.get(i);
            if (contacto.getTieneChat()) {
                nicks.addElement(contacto.getNickname());
            }
        }
        return nicks;
    }
    
}
