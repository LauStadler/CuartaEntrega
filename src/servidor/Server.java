package servidor;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

public class Server extends Thread {

    private ServerSocket socketClientes;
    private GestionDeServer gestionDeServer;
    private ServerSocket socketMonitor;
    private ArrayList<Usuario> usuariosReg = new ArrayList<Usuario>();
    private ArrayList<String> mensajesPendientes = new ArrayList<String>();
    private boolean soyPrincipal;

    private int puertoClientes;
    private int puertoEntreServidores;
    private int puertoEscMonitor;

    public Server(int puerto) {
        super();
        this.soyPrincipal = false;
        this.puertoClientes = puerto;
        this.puertoEntreServidores = puerto + 1000;
        this.puertoEscMonitor = puerto + 2000;
        startHeartbeat();
    }

    public void run() {

        System.out.println("Inicie el Servidor");
        this.gestionDeServer = new GestionDeServer(this);
        gestionDeServer.start();

        try {
            while (true) {
                try {
                    this.sleep(1);
                } catch (InterruptedException ex) {
                }
                if (this.soyPrincipal) {
                    System.out.println("Soy principal");
                    System.out.println("Volvi a entrar al if de principal yeiy");
                    Socket soc = socketClientes.accept();
                    System.out.println("Acepte una conexion");
                    BufferedReader in = new BufferedReader(new InputStreamReader(soc.getInputStream()));
                    String user = in.readLine();
                    System.out.println("Se conecto el usuario: " + user);

                    AtencionCliente cliente = new AtencionCliente(soc, this);
                    cliente.start();
                    loggeaUser(user, cliente);
                }
            }
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            System.out.println(e.getMessage());
        }
    }

    public int getPuertoClientes() {
        return this.puertoClientes;
    }

    public int getPuertoServidores() {
        return this.puertoEntreServidores;
    }

    public int getPuertoMonitor() {
        return this.puertoEntreServidores;
    }

    public void setSoyPrincipal(boolean state) {

        if (state && (this.socketClientes == null || socketClientes.isClosed())) {
            try {
                this.socketClientes = new ServerSocket(puertoClientes);
                System.out.println("Yo soy el principal bitch");
                System.out.println("La lista de usuarios es: " + usuariosReg.toString());
            } catch (IOException ex) {
            }
        }
        this.soyPrincipal = state;
    }

    public boolean isSoyPrincipal() {
        return this.soyPrincipal;
    }

    public void buscaYEnviaPendientes(String user, AtencionCliente clientHandler) {
        // arraylist busca por user
        // corta cada string
        // lo manda
        // lo elimina
        // receptor#mensaje
        String nickname[] = user.split("#", 2);
        System.out.println("busco mensajes pendientes del usuario: " + nickname[0]);
        System.out.println("Mensajes pendiente: " + this.mensajesPendientes);
        for (String aux : this.mensajesPendientes) {
            System.out.println("mensaje: " + aux);
            String aux2[] = aux.split("#", 2);
            System.out.println("aux2[0] " + aux2[0] + " y el mensaje: " + aux2[1]);
            if (aux2[0].equalsIgnoreCase(nickname[0])) {
                System.out.println("Encontre mensajes pendietes para esa persona");
                clientHandler.recibeMensaje(aux2[1]);
                gestionDeServer.envioActualizacion(12 + "#" + aux);
                mensajesPendientes.remove(aux);
            }
        }
    }

    public void loggeaUser(String user, AtencionCliente clientHandler) {

        String usuario[] = user.split("#", 2);
        int i = 0, n;
        n = this.getUsuariosReg().size();

        while (i < n && !this.getUsuariosReg().get(i).getNickname().equals(usuario[0])) {
            i++;
        }

        if (i < n) {// el usuario ya se habia loggeado antes, lo pongo como conectado
            if (this.getUsuariosReg().get(i).isConectado()) { // alguien esta tratando de robar un nick ya usado por
                // otra persona
                // error, ya existe alguien con ese nombre de usuario
                System.out.println("Se esta tratando de meter en otro lado");
                String mensaje = "false";

                clientHandler.confirmaLogIn(mensaje);
            } else {
                // System.out.println("el usuario ya se habia loggeado antes, lo pongo como
                // conectado");
                this.getUsuariosReg().get(i).setConectado(true);
                this.getUsuariosReg().get(i).setClientHandler(clientHandler);
                String mensaje = "true";
                clientHandler.confirmaLogIn(mensaje);
                // busca mensajes pendientes para ese usuario y los envia
                buscaYEnviaPendientes(user, clientHandler);
            }
        } else {// es un usuario nuevo, lo registro en el sistema
            // System.out.println("es un usuario nuevo, lo registro en el sistema");
            Usuario nuevoUsuario = new Usuario(usuario[0]);
            nuevoUsuario.setClientHandler(clientHandler);
            nuevoUsuario.setConectado(true);
            this.getUsuariosReg().add(nuevoUsuario);
            String mensaje = "true";
            clientHandler.confirmaLogIn(mensaje);
            gestionDeServer.envioActualizacion(13 + "#" + user);

        }

    }

    public void logout(AtencionCliente clientHandler) {
        int i = 0, n = usuariosReg.size();
        while (i < n && this.getUsuariosReg().get(i).getClientHandler() != clientHandler) {
            i++;
        }
        this.getUsuariosReg().get(i).setConectado(false);
        this.getUsuariosReg().get(i).setClientHandler(null);
        gestionDeServer.envioActualizacion(15 + "#" + this.getUsuariosReg().get(i).getNickname());
    }

    public ArrayList<Usuario> getUsuariosReg() {
        return usuariosReg;
    }

    public void enviaMensaje(String receptor, String mensaje) { // un usurio quiere enviar un mensaje
        // busca el receptor en la lista y si esta conectado
        Usuario user = buscaUsuario(receptor);
        if (user.isConectado()) {
            System.out.println("El usuario esta conectado le paso el mensaje");
            // obtiene su clientHandler
            // llama a la funcion de enviaMensaje en clientHasssndler
            user.getClientHandler().recibeMensaje(mensaje);
        } else {
            System.out.println("El usuario no esta conectado lo dejo pendiente");
            System.out.println("mensaje pendiente:" + mensaje);
            System.out.println("User que no esta conectado: " + receptor);
            // vuelve a unir el string y lo agregva a mensajes pendientes
            String aux = receptor + "#" + mensaje;
            mensajesPendientes.add(aux);
            gestionDeServer.envioActualizacion(11 + "#" + aux);
        }
    }

    public Usuario buscaUsuario(String nickUsuario) {
        int i, n;
        i = 0;
        n = this.getUsuariosReg().size();
        while (i < n && !this.getUsuariosReg().get(i).getNickname().equals(nickUsuario)) {
            i++;
        }
        return this.getUsuariosReg().get(i);
    }

    // para el directorio, ver si existe o no
    public boolean usuarioExiste(String nickUsuario) {
        int i, n;
        i = 0;
        Usuario aux = null;
        n = this.getUsuariosReg().size();
        while (i < n && !this.getUsuariosReg().get(i).getNickname().equals(nickUsuario)) {
            i++;
        }
        return i < n;
    }

    public void agregaMensajePendiente(String mensaje) {
        this.mensajesPendientes.add(mensaje);
    }

    public void eliminaMensajePendiente(String mensaje) {
        this.mensajesPendientes.remove(mensaje);
    }

    public ArrayList<String> getMensajesPendientes() {
        return this.mensajesPendientes;
    }

    public void startHeartbeat() {
        try {
            Socket socket = new Socket("localhost", 1003);
            PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
            BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            Timer timer = new Timer();

            // Enviamos el número de servidor al conectar
            String nroServer = Integer.toString(getPuertoClientes());
            out.println(nroServer); // aqui le manda el server que es, 1000 o 1001, el monitor debe leerlo para
            // asignarle un
            System.out.println("Me conecte al monitor");
            // Leemos el rol que nos asigna el servidor (solo una vez)
            System.out.println("Esperando rol del monitor...");
            String rol = in.readLine(); // puede ser "principal" o "secundario"
            if (rol == null) {
                System.out.println("Error al recibir el rol del monitor");
                return;
            }
            if (rol.equals("PRINCIPAL")) {
                setSoyPrincipal(true);
            }
            System.out.println("Mi rol es " + rol);
            timer.schedule(new TimerTask() {
                @Override
                public void run() {
                    try {
                        out.println(nroServer); // seguimos enviando heartbeats
                    } catch (Exception e) {
                        e.printStackTrace();
                        timer.cancel(); // cancelamos el timer
                        try {
                            in.close();
                            out.close();
                            socket.close();
                        } catch (IOException ioException) {
                            ioException.printStackTrace();
                        }
                    }
                }
            }, 0, 1000);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void stopHeartbeat(Socket socket, PrintWriter out, Timer timer) {
        if (timer != null) {
            timer.cancel();
        }
        try {
            if (out != null) {
                out.close();
            }
            if (socket != null) {
                socket.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /*
     * public void startHeartbeat() {
     * new Timer().schedule(new TimerTask() {
     * 
     * @Override
     * public void run() {
     * try (Socket s = new Socket("localhost", 1003); PrintWriter out = new
     * PrintWriter(s.getOutputStream(), true)) {
     * String nroServer = Integer.toString(getPuertoClientes());
     * out.println(nroServer); // manda mensjes con "1000" o "1001"
     * } catch (IOException e) {
     * e.printStackTrace();
     * }
     * }
     * }, 0, 1000); // envía cada 1 segundo
     * }
     */
    public void escuchaMonitor() {
        Thread hilo = new Thread(() -> {
            try {
                this.socketMonitor = new ServerSocket(this.puertoEscMonitor);
                System.out.println("Servidor escuchando al monitor en puerto " + puertoEscMonitor);

                while (true) {
                    Socket soc = socketMonitor.accept();
                    System.out.println("El monitor se comunicó conmigo");

                    BufferedReader in = new BufferedReader(new InputStreamReader(soc.getInputStream()));
                    String mensaje = in.readLine();

                    if (mensaje != null && mensaje.equals("AHORA SOS PRINCIPAL")) {
                        System.out.println("Me convierto en servidor principal");
                        // y aca tenemos que cambiar los usuarios para que se conecten al que ahora es
                        // principal
                        setSoyPrincipal(true);
                    }

                    soc.close(); // Cierra el socket temporal con el monitor
                }
            } catch (IOException e) {
                System.out.println("Error en escuchaMonitor: " + e.getMessage());
                e.printStackTrace();
            }
        });
        hilo.start();
    }

}
