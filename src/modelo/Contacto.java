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


    @Override
    public String toString() {
        return "Contacto [nickname=" + nickname + ", tieneChat=" + tieneChat + ", mensajes=" + mensajes + "]";
    }    
}
