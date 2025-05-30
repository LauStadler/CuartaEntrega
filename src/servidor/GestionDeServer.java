package servidor;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

public class GestionDeServer extends Thread {

    private Server server;
    private Socket mainServer;
    private PrintWriter out;
    private ServerSocket socketServidores;
    private Socket serverSecundario;

    public GestionDeServer(Server server) {

        this.server = server;
        //cuando soy el principal abro la conexion para que los demas servers secundarios se conecten a mi
        if (server.isSoyPrincipal()) {
            try {
                this.socketServidores = new ServerSocket(server.getPuertoServidores());
            } catch (IOException ex) {
                ex.printStackTrace();
                System.out.println(ex.getMessage());
            }
        } else {
            //cuando soy secundario me conecto al principal
            try {
                if (server.getPuertoClientes() == 1001) {
                    mainServer = new Socket("localhost", 2002); 
                }else {
                    mainServer = new Socket("localhost", 2001);
                }
                this.out = new PrintWriter(mainServer.getOutputStream(), true);
                String update = "UPDATE";
                this.out.println(update);
            } catch (Exception e) {
                e.printStackTrace();
                System.out.println(e.getMessage());
            }

        }

    }

       public void run(){
        System.out.println("Inicie el sistema de conexion");
		String actualizacion = null;	
        while (true) {
				try {
                    if (server.isSoyPrincipal()){
                        // si nunca fui ppal, en el constructor nunca abri mi server socket. Tengo que abrirlo aca
                        if (this.socketServidores == null)
                            this.socketServidores = new ServerSocket(server.getPuertoServidores());
                        this.serverSecundario = socketServidores.accept();
                        System.out.println("Acepte una conexion de servidor secundario");
                        
                        this.out = new PrintWriter(serverSecundario.getOutputStream(), true);
                        
                        BufferedReader in = new BufferedReader(new InputStreamReader(serverSecundario.getInputStream()));
                        actualizacion = in.readLine();
                        if(actualizacion != null){
                            System.out.println("Recibi request de actualizacion");
                            update(); // seteo server secundario    
                        }
                    }
                    else{
                        System.out.println("Entre al else de no soy principal");
                        BufferedReader in = new BufferedReader(new InputStreamReader(mainServer.getInputStream()));
                        actualizacion = in.readLine();
                        recibeActualizacion(actualizacion);
                    }				
				} catch (IOException e) {
					
					e.printStackTrace();
					System.out.println(e.getMessage());
                    try {
                        mainServer.close();
                        socketServidores.close();
                    } catch (IOException e1) {
                        // TODO Auto-generated catch block
                        e1.printStackTrace();
                    }
				}	
		}
    }

    /*
     * nuevo mensaje pendiente
     * se mando un mensaje que era pendiente
     * nuevo usuario registrado
     * log in de usuario -- pasa el boolean de concetado a true
     * cerro sesion usuario -- pasa el boolean de concetado a false
     * 
     */
    public void recibeActualizacion(String request) {

        String aux[] = request.split("#", 2);
        System.out.println(" me llego una actualizacion : request " + request);
        int nroRequest = Integer.parseInt(aux[0]);
        String rta;
        //este metodo envia las actualizaciones a los demas servidores
        if (nroRequest == 11) {//11--recibe mensaje pendientes para enviar a otro usuario
            // el request de mensaje llega como: #receptor#emisor#hora#texto 
            server.agregaMensajePendiente(aux[1]);
        } else if (nroRequest == 12) {//12--elimina el mensaje de la lista de pendientes
            server.eliminaMensajePendiente(aux[1]);
        } else if (nroRequest == 13)// 13-- usuario nuevo registrado
        {
            //nombre#puerto
            String datosUsuario[] = aux[1].split("#", 2);
            Usuario nuevoUser = new Usuario(datosUsuario[0]);
            nuevoUser.setConectado(false);
            server.getUsuariosReg().add(nuevoUser);

        }
        /*else if (nroRequest == 14)// 14-- se loggea un usuario registrado
		{
            //nombre#puerto
            String datosUsuario[] = aux[1].split("#", 2);
            server.buscaUsuario(datosUsuario[0]).setConectado(true);
        }*/
        else if(nroRequest == 15)// 15- usuario cerro sesion
       {   
           String usuario[] = aux[1].split("#",2);
           server.buscaUsuario(usuario[0]).setConectado(false);
       }
       
    }

    //el principal envia actualizaciones en tiempo real
    public void envioActualizacion(String actualizacion) {
        try {
            this.out.println(actualizacion);
        } catch (Exception e) {
            System.out.println(e.getLocalizedMessage());
        }
    }

    //manda el Update completo del estado
    // enviar todo tal cual esta en ese momento
    public void update() {
        // for para todos los usuarios registrados
        for (Usuario aux : this.getServer().getUsuariosReg()) {
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            System.out.println("User: " + aux.getNickname());
            envioActualizacion(13 + "#" + aux.getNickname());

        }

        // for viendo cuales estan desconectados
        for (Usuario aux : this.getServer().getUsuariosReg()) {
            if (!aux.isConectado()) {
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
                System.out.println("Se desconecto: " + aux.getNickname());
                envioActualizacion(15 + "#" + aux.getNickname());
            }
        }

        // for para lista de mensajes pendientes
        for (String aux : this.getServer().getMensajesPendientes()) {
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            System.out.println("Mensaje pendiente: " + aux);
            envioActualizacion(12 + "#" + aux);
        }

    }

    public Server getServer() {
        return this.server;
    }
}