package servidor;

import java.net.ServerSocket;

public class MainServer2 {

    public static void main(String[] args) {
        try {
            ServerSocket serverSocket = new ServerSocket(1002);
            serverSocket.close();
            Server servidor = new Server(1002);
            servidor.start();
            servidor.escuchaMonitor();
        } catch (Exception e1) {
            e1.printStackTrace();
        }
    }
}
