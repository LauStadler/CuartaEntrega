/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package modelo;

import java.util.ArrayList;

/**
 *
 * @author Usuario
 */
public class Contacto {
    private String nickname;
    private boolean tieneChat;
    private ArrayList<String> mensajes = new ArrayList<String>();

    // para que funcione xml tenemos que tener un constructor vacio de las clasese que queremos persistir
    public Contacto() {
    }
    
    public Contacto(String nickname) {
        super();
        this.nickname = nickname;
        this.tieneChat = false;
    }  


    public String getNickname() {
        return nickname;
    }

    public boolean getTieneChat(){
        return tieneChat;
    }
    
    public void setTieneChat(boolean tieneChat){
        this.tieneChat = tieneChat;
    }

    public ArrayList<String> getMensajes(){
        return this.mensajes;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }
    
    
    // esto es para que funcione la persistencia en xml
    public void setMensajes(ArrayList<String> mensajes){
        this.mensajes = mensajes;
    }

    @Override
    public String toString() {
        return "Contacto [nickname=" + nickname + ", tieneChat=" + tieneChat + ", mensajes=" + mensajes + "]";
    }    
}
