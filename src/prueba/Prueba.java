/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package prueba;
import controlador.Controlador;
import modelo.Sistema;
import vista.Vista;
import vista.VistaLogIn;
import vista.VistaPersistencia;

/**
 *
 * @author Usuario
 */
public class Prueba {
    // esta clase es un main para probar el funcionamiento de todo
    public static void main(String[] args){
        //Sistema sistema = new Sistema();
        Sistema sistema = Sistema.getInstance();
        
        VistaLogIn vistaLogIn = new VistaLogIn();
        Vista vista = new Vista();
        VistaPersistencia vistaPersistencia = new VistaPersistencia();
        Controlador controlador = new Controlador(vista, vistaLogIn, vistaPersistencia, sistema);

        vistaLogIn.setControlador(controlador);
        

        vistaLogIn.setVisible(true);
        vistaPersistencia.setVisible(false);
        // el problema es que setControlador en vista es el que carga las listas especiales
        // y no se puede hacer antes de que se carguen en el sistema
        // si se tienen que sacar de archivos del usuario
         vista.setControlador(controlador);
    }
}
