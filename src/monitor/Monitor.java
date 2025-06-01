package monitor;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

public class Monitor extends Thread {

    private int server1 = 1001;
    private int server2 = 1002;
    private int serverPrincipal;

    private ServerSocket monitorSocket;

    private long lastHeartbeatServer1 = System.currentTimeMillis();
    private long lastHeartbeatServer2 = System.currentTimeMillis();
    private final long TIMEOUT = 5000; // 5 segundos de tolerancia

    public static void main(String[] args){
        Monitor monitor = new Monitor();
        monitor.start();
    }
    //CAMBIOS
    // cambiamos el constructor de monitor, inicializa serverPrincipal con valor imposible
    public Monitor() {
        try {
            this.monitorSocket = new ServerSocket(1003);
        } catch (IOException ex) {
        }
        this.serverPrincipal = -1;

        System.out.println("Monitor iniciado en puerto 1003");

        checkFailures();      // inicia el hilo que monitorea fallos
    }

    public void setServerPrincipal(int server){
        this.serverPrincipal = server;
    }
    
    @Override
    public void run() {
        while (true) {
            try {
                String mensaje;
                Socket socket = monitorSocket.accept();
                BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                String  line = in.readLine();
                int serverId = Integer.parseInt(line.trim());
                if(noExistePrincipal()){
                    mensaje = "PRINCIPAL";
                    setServerPrincipal(serverId); // define quien es el principal, el primero que se conecta al monitor
                    System.out.println("Puerto del principal "+this.serverPrincipal);
                }
                else
                    mensaje = "SECUNDARIO";
                PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
                out.println(mensaje);
                System.out.println("Le mande su rol");
                new Thread(() -> handleConnection(socket)).start();
            } catch (IOException e) {
                System.err.println("Error al aceptar conexión: " + e.getMessage());
            }
        }
    }

    private void handleConnection(Socket socket) {
        try (BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()))) {
            String line;
            while (socket.isConnected()) {
                line = in.readLine();
                int serverId = Integer.parseInt(line.trim());

                if (serverId == server1) {
                    synchronized (this) {
                        lastHeartbeatServer1 = System.currentTimeMillis();
                    }
                    System.out.println("Heartbeat recibido de server1");
                } else if (serverId == server2) {
                    synchronized (this) {
                        lastHeartbeatServer2 = System.currentTimeMillis();
                    }
                    System.out.println("Heartbeat recibido de server2");
                } else {
                    System.out.println("Heartbeat recibido de servidor desconocido: " + serverId);
                }
            }
        } catch (IOException e) {
            System.out.println("Conexión cerrada con un servidor.");
        }
    }

    private void checkFailures() {
        new Thread(() -> {
            while (true) {
                try {
                    Thread.sleep(1000); // cada 1 segundo
                    long now = System.currentTimeMillis();

                    boolean alive1, alive2;
                    synchronized (this) {
                        alive1 = (now - lastHeartbeatServer1) < TIMEOUT;
                        alive2 = (now - lastHeartbeatServer2) < TIMEOUT;
                    }

                    if (serverPrincipal == server1 && !alive1 && alive2) {
                        System.out.println("Servidor 1 caído. Promoviendo al servidor 2 como principal.");
                        serverPrincipal = server2;
                        
                        // mensaje para que el server 2 se setee como principal
                        // crear conexion con serverSocket para monitor de server (3001 o 3002)
                        try {
                            ServerSocket serverSocket = new ServerSocket(1001);
                            serverSocket.close();
                            Socket socket = new Socket("localhost", 3002);
                            PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
                            out.println("AHORA SOS PRINCIPAL");
                        } catch (IOException e) {
                            System.err.println("No se pudo notificar a servidor 2");
                        }
                    } else if (serverPrincipal == server2 && !alive2 && alive1) {
                        System.out.println("Servidor 2 caído. Promoviendo al servidor 1 como principal.");
                        serverPrincipal = server1;
                        
                        // mensaje para que el server 1 se setee como principal
                        try {
                            ServerSocket serverSocket = new ServerSocket(1002);
                            serverSocket.close();
                            Socket socket = new Socket("localhost", 3001);
                            PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
                            out.println("AHORA SOS PRINCIPAL");
                        } catch (IOException e) {
                            System.err.println("No se pudo notificar a servidor 1");
                        }
                    }
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    public boolean noExistePrincipal(){
        return this.serverPrincipal ==-1 ;
    }
}
