/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package modelo;

import controlador.Controlador;
import java.io.StringReader;
import java.util.List;
import java.util.stream.Stream;
import javax.swing.DefaultListModel;
import java.time.LocalDateTime;


/* Numero de Resquest
 * 1--Registrarse
 * 2--Log In
 * 3--Enviar Mesanje
 * 4--Agregar Contacto
 * 
 * */

/**
 *
 * @author Usuario
 */
public class Sistema {
    //private Receptor receptor;
    private String ipUsuario = "localhost";
    private int puertoUsuario;
    private String nickUsuario;
    private Controlador controlador;
    private GestorConexion gestionConexion;
    private static Sistema instance = null;

    private DefaultListModel<Contacto> contactos = new DefaultListModel<Contacto>();
    private DefaultListModel<String> nicksContactos = new DefaultListModel<String>();
    private DefaultListModel<String> nicksChats = new DefaultListModel<String>();


    private Sistema(){
        super();
    }

    public static Sistema getInstance(){
        if (this.instance == null)
            this.instance = new Sistema();
        return this.instance;
    }

    public void setNickUsuario(String nickUsuario){
        this.nickUsuario = nickUsuario;
    }
    public void setPuertoUsuario(int puertoUsuario){
        this.puertoUsuario = puertoUsuario;
    }
    public int getPuertoUsuario(){
        return this.puertoUsuario;
    }
    public String getNickUsuario(){
        return this.nickUsuario;
    }
    public String getIpUsuario(){
        return this.ipUsuario;
    }
    public DefaultListModel<Contacto> getContactos(){
        return this.contactos;
    }
    public DefaultListModel<String> getNicksContactos(){
        return this.nicksContactos;
    }
    public DefaultListModel<String> getNicksChats(){
        return this.nicksChats;
    }
    public void setControlador(Controlador controlador){
        this.controlador = controlador;
    }

    public void ingresar(String nickUsuario, int puertoUsuario){
        setNickUsuario(nickUsuario);
        setPuertoUsuario(puertoUsuario);
        String aux = nickUsuario + "#" + puertoUsuario; 
        gestionConexion = new GestorConexion(this, this.controlador, aux);
        gestionConexion.start();
    }


    public void agregarContacto(String nickContacto){
        gestionConexion.enviaRequest(nickContacto, 2);
    }

    public void nuevoContacto(String nickname){ //Crea el contacto
       
        Contacto contacto = new Contacto(nickname);
        this.contactos.addElement(contacto);
        this.nicksContactos.addElement(contacto.getNickname());
    }

    public Contacto buscaContactoPorNick(String nick){
        //busca en la lista de contactos
        int i, n;
        i=0;
        Contacto aux = null;
        n=this.getContactos().size();
        while (i<n && !this.getContactos().get(i).getNickname().equals(nick))
            i++;
        if (i<n){//siempre deberia encontrarlo
            aux = this.getContactos().get(i);
        }
        else{
            System.out.println("el Contacto buscado por nick NO existe");
        }
        return aux;
    }

    public void nuevoMensajeRecibido(String mensaje){
        String[] mensajeFormateado = mensaje.split("#", 3);
        String nickEmisor = mensajeFormateado[0];
        
        
        if (!esContactoExistente(nickEmisor)){
            System.out.println("me llego un mensaje de una persona que no tengo agendada..." + nickEmisor );
            nuevoContacto(nickEmisor);
            System.out.println("ya agende a la persona = " + nickEmisor);
            //sistema.getNicksChats().addElement(nickEmisor);
        }
        // fijarse si ya tiene un chat con esa persona; sino agregar chat
        Contacto contacto = buscaContactoPorNick(nickEmisor);
        if (contacto.getTieneChat() == false){
            contacto.setTieneChat(true);
            getNicksChats().addElement(contacto.getNickname());
        }

        // agregar el mensaje al arraylist de mensajes del contacto    
        contacto.getMensajes().add(mensaje);  
        System.out.println("ya agregue el mensaje a la lista de mensajes de ese contacto");
        
        // lo muestra   
        controlador.nuevoMensajeRecibido(mensaje, contacto);

    }

    public void enviarMensaje(String nickUsuario, String nicknameReceptor, String mensaje){
        
        mensaje = creaStringMensaje(mensaje, nickUsuario);
        Contacto contacto = buscaContactoPorNick(nicknameReceptor);
        contacto.getMensajes().add(mensaje);
        gestionConexion.enviaRequest(nicknameReceptor+ "#" + mensaje, 1);
        controlador.cargaChat(contacto);
    }

    public String creaStringMensaje(String texto, String nicknameUsuario){
        String mensaje,hora = LocalDateTime.now().toString();
        mensaje = nicknameUsuario + "#" + hora + "#" + texto;
        return mensaje;
    }

    public boolean esContactoExistente(String nickname){
       Boolean aux=false;
       Contacto contacto = buscaContactoPorNick(nickname);
        if (contacto!=null)
            aux=true; 
        return aux;
    }

   
    public int buscaPosListaChats(String nickname){
        int i = this.nicksChats.indexOf(nickname);
        return i;
    }

    public void poneNotificacion(String nickname){
        int i = this.nicksChats.indexOf(nickname);
        if (!nicksChats.getElementAt(i).endsWith("[!!!]"))
            nicksChats.set(i, nicksChats.get(i) + "[!!!]");
        //buscamos la posicion donde se encuentra nickname
        //copiamos ese string: nickname + [!!!]
        //guardamos ese string en la misma posicion que se encontraba el original
        //String string = buscaPosListaChats(nickname).append()
    }

    public void sacaNotificacion(String nickname){
        int i = this.nicksChats.indexOf(nickname);
        String nick = nicksChats.get(i);
        //nicksChats.set(i, nicksChats.get(i) + "[!!!]");
        if (nick.endsWith("[!!!]"))
            nicksChats.set(i, nick.substring(0, nick.length() - 5));
    }


}
    
    
   