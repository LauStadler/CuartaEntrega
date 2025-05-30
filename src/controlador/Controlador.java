/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package controlador;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.net.ServerSocket;
import modelo.Contacto;
import modelo.GestorConexion;
import modelo.Sistema;
import vista.Vista;
import vista.VistaLogIn;

/**()
 *
 * @author Usuario
 */
public class Controlador implements ActionListener{

    private Vista vista;
    private VistaLogIn vistaLogIn;
    private Sistema sistema;
    private GestorConexion gestionConexion;
    
    public Controlador(Vista vista, VistaLogIn vistaLogIn, Sistema sistema){
        this.vista = vista;
        this.vistaLogIn = vistaLogIn;
        this.sistema = sistema;      
        this.sistema.setControlador(this);
		
    }

    public void cambiaAVentanaChats(){
        this.vista.setVisible(true);
        this.vistaLogIn.setVisible(false);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getActionCommand().equals("LogIn") || e.getActionCommand().equals("Registrarse")){
            System.out.print("Entre al action command de Login!!");
            String nickUsuario = vistaLogIn.getNickUsuario();
            int puertoUsuario = vistaLogIn.getPuertoUsuario();

            if ((puertoUsuario == -1) || !puertoDisponible(puertoUsuario)){
                vistaLogIn.muestraVentanaEmergente("El puerto es invalido o esta en uso");
            }else{
                if (nickUsuario.equals("") || nickUsuario.equals(" nickname"))
                    vistaLogIn.muestraVentanaEmergente("Se debe ingresar un nombre de usuario");
                else{
                    sistema.ingresar(nickUsuario, puertoUsuario);
                    // llenar campo nickUsuario
                    vista.setNickUsuario(nickUsuario);
                    // llenar campo puertoUsuario
                    vista.setPuertoUsuario(puertoUsuario);
                    
                    
                    //avisarle al server que nos logueamos
                }
            }
        }
        
        if (e.getActionCommand().equals("Enviar")){
            System.out.print("Entre al action command de enviar mensaje!!");
            // 
            // Crear el string mensaje agarrando = lo que esta en el textField, nickUsuario, puertoUsuario, y hora de envio
            String mensaje = vista.getTextoMensaje();
            String nicknameReceptor = vista.getNicknameChatSeleccionado();
            String nickUsuario = vista.getNicknameUsuario();
            sistema.enviarMensaje(nickUsuario, nicknameReceptor, mensaje);
        }
        if (e.getActionCommand().equals("AgregarContacto")){
            System.out.print("Entre al action command de agregar contacto!!");
            //leer los campos de datos de contacto
            String nickname = vista.getNickContactoAgregado();
            sistema.agregarContacto(nickname);
            
            //manda el request al servidor
            
        }
    }
    

    public void cargaChat(Contacto contacto){
        vista.setTextoMensaje("");
        vista.limpiaChat();
        vista.cargaChat(contacto.getMensajes());
    }

    public String getNickName() {
    	return sistema.getNickUsuario();
    }

    // chequea si el puerto esta libre o no
    public boolean puertoDisponible(int puerto) {
    try (ServerSocket ss = new ServerSocket(puerto)) {//trata de abrir un socket para ese puerto
        ss.setReuseAddress(true); // para que despues puedas abrirlo
        return true;//no esta en uso -> pudo abrirlo
    } catch (IOException e) {
        return false;// el socket ya esta en uso -> no puede abrirlo, salta excepcion
    }
}


    public void listaContactosMouseClicked(java.awt.event.MouseEvent evt){
        vista.limpiaChat();
        String nickSeleccionado = vista.getListaContactosSeleccionado();
        Contacto contacto = sistema.buscaContactoPorNick(nickSeleccionado);
        vista.setNickChatSeleccionado(nickSeleccionado);
        if (contacto.getTieneChat()) {
            vista.cargaChat(contacto.getMensajes());
        } else {
            contacto.setTieneChat(true);
            sistema.getNicksChats().addElement(nickSeleccionado);
        }
        vista.cambiarAVentanaChat();
    }

    public void listaChatsMouseClicked(java.awt.event.MouseEvent evt){
        vista.limpiaChat();
        String nickSeleccionado = vista.getListaChatsSeleccionado();
         if (nickSeleccionado.endsWith("[!!!]")) {
            sistema.sacaNotificacion(nickSeleccionado);
            // actualizar la vista para que se vea sin Â¨!!!n '> cambia automaticamente cuando cambia la l.ista de chats'
            System.out.println("El nick que elegi tiene una notificiacion. Cortando ... ");
            nickSeleccionado = nickSeleccionado.substring(0, nickSeleccionado.length() - 5);
            System.out.println("El nick cortado es: " + nickSeleccionado);
        }
        Contacto contacto = sistema.buscaContactoPorNick(nickSeleccionado);
        System.out.println("Seleccione el chat de "+ nickSeleccionado);
        System.out.println("Sus mensajes:  " + contacto.getMensajes());
        vista.setNickChatSeleccionado(nickSeleccionado);
       // vista.setPuertoChatSeleccionado(contacto.getPuerto());
        vista.cargaChat(contacto.getMensajes());
    }

    public Sistema getSistema(){
        return this.sistema;
    }

    public void nuevoMensajeRecibido(String mensaje, Contacto contacto){
      
        // lo muestra
        if (vista.getNicknameChatSeleccionado().equals(contacto.getNickname())){
            System.out.println("estoy en el chat del contacto que me llego msj");
            vista.limpiaChat();
            vista.cargaChat(contacto.getMensajes());
        }
        else{
            sistema.poneNotificacion(contacto.getNickname());
        }
    }

    public Vista getVista() {
        return vista;
    }
}
