package persistencia;

import javax.swing.DefaultListModel;

public interface IFactoryPersistencia {

    public IGuardador creaGuardador(DefaultListModel contactos);
    public ILector creaLector(DefaultListModel contactos);
}
