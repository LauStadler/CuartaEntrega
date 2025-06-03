package persistencia;

import javax.swing.DefaultListModel;

public class FactoryJson implements IFactoryPersistencia {

    @Override
    public GuardadorJson creaGuardador(DefaultListModel contactos){
        return new GuardadorJson();
    }

    @Override
    public LectorJson creaLector(DefaultListModel contactos){
        return new LectorJson();
    }

    

}
