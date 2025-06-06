package servidor;

import java.net.ServerSocket;

public class MainServer1 {

    public static void main(String[] args) {
        try {
            
            ServerSocket serverSocket = new ServerSocket(1001);
            serverSocket.close();
            Server servidor = new Server(1001);
            servidor.start();
            servidor.escuchaMonitor();

        } catch (Exception e1) {
            e1.printStackTrace();
        }
    }
}