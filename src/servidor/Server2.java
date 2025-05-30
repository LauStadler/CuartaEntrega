package servidor;

import monitor.Monitor;

import java.net.ServerSocket;

public class Server2 {
    public static void main(String[] args) {
        try {
            Monitor monitor;
            ServerSocket serverSocket = new ServerSocket(1002);
            serverSocket.close();
            Server servidor = new Server(1002);
            // ver si ya hay uh principal o si soy yo
            if (monitor.noExistePrincipal()){
                servidor.setSoyPrincipal(true);
                monitor.setServerPrincipal(1002);
            }
            servidor.start();
            servidor.escuchaMonitor();

        } catch (Exception e1) {
            e1.printStackTrace();
        }
    }
}
