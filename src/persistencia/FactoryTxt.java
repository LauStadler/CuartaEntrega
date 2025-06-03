package persistencia;

import javax.swing.DefaultListModel;

public class FactoryTxt implements IFactoryPersistencia {

     @Override
    public GuardadorTxt creaGuardador(DefaultListModel contactos){
        return new GuardadorTxt();
    }
    @Override
    public LectorTxt creaLector(DefaultListModel contactos){
        return new LectorTxt();
    }
}