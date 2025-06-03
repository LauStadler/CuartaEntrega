package persistencia;

import javax.swing.DefaultListModel;

public class FactoryXml implements IFactoryPersistencia {
    
    @Override
    public GuardadorXml creaGuardador(DefaultListModel contactos){
        return new GuardadorXml();
    }
    @Override
    public LectorXml creaLector(DefaultListModel contactos){
        return new LectorXml();
    }

}
