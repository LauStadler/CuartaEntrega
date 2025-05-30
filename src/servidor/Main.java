package servidor;

import java.net.ServerSocket;

public class Main {
	
	public static void main(String[] args){
       
		 try {
            ServerSocket serverSocket = new ServerSocket(1001);
            serverSocket.close();
            Server servidor = new Server(1001);
            servidor.start();
            servidor.setSoyPrincipal(true);
        }
        catch( Exception e){//Si ya hay un server instanciado -> entro con el puerto secundario
            try {
                ServerSocket serverSocket = new ServerSocket(1002);
                serverSocket.close();
                Server servidor = new Server(1002);
                servidor.start();
            }
            catch(Exception e1){
                System.out.printf("------ ERROR!! NO SE PUDO ESTABLECER EL SERVIDOR -------");
            }
        }  
    }
}
