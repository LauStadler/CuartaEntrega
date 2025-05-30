/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package modelo;

import controlador.Controlador;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Arrays;

/**
 *
 * @author Usuario
 */
public class GestorConexion extends Thread {
    private String ipServer;
    private int[] puertoServer = {1001,1002};
    private PrintWriter out;
    private Controlador controlador;
    private Sistema sistema;
    private String user;
    // esta clase se encarga de enviar mensajes

    public GestorConexion(Sistema sistema, Controlador controlador, String user) {
        this.controlador = controlador;
        this.sistema = sistema;
        this.ipServer = "localhost";
        this.user = user;
        this.out = null;
    }

    public void run() {
        Socket socket = null;
        BufferedReader in = null;
        System.out.println("voy a iniciar la conexion al servidor");

        while (true) {

            int i = 0, n = this.puertoServer.length;
            try {
                System.out.println("lista de puertos "+ Arrays.toString(puertoServer));
                while ((socket == null || !socket.isConnected()) && i < n) {
                    try {
                        socket = new Socket(ipServer, puertoServer[i]);
                        System.out.println("puerto "+puertoServer[i]);
                    } catch (Exception e) {
                        System.out.println("no se puedo conectar al puerto "+puertoServer[i]);
                         i++;
                    }
                } 

                System.out.println("Se conecto al servidor " + socket.getPort());
                this.out = new PrintWriter(socket.getOutputStream(), true);
                System.out.println("ya hice el out!!! " + out.toString());
                in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                out.println(this.user);

                while (socket.isConnected()) {
                    System.out.println("Ya me loguee estor en while socket is connected");
                    String mensaje = in.readLine();
                    System.out.println("pase el readline");
                    procesaRequest(mensaje);
                    System.out.println("procese el request");
                }

                out.close();
                in.close();
                socket.close();

                System.out.println("Se cerro la conexion al servidor");

            } catch (Exception e) {
                System.out.println("Error en la conexión: " + e.getMessage());
                //controlador.getVista().muestraVentanaEmergente("El servidor se cayó. Reintentando conexión...");
                try {
                    out.close();
                    in.close();
                    socket.close();
                    socket = null;
                    Thread.sleep(10000); // Evitá reconexiones rápidas en bucle
                } catch (InterruptedException ex) {
                    ex.printStackTrace();
                } catch (IOException e1) {
                    // TODO Auto-generated catch block
                    e1.printStackTrace();
                }
            }
        }
    }

    public void enviaRequest(String mensaje, int nrorequest) {// yo me quiero comunicar con el servidor
        String request = String.valueOf(nrorequest) + "#" + mensaje;
        out.println(request);

    }

    public void procesaRequest(String request) { // cuando recibe comunicación del servidor

        String aux[] = request.split("#", 2); // 1#emisor#hora#texto ==  1 - emisor#hora#texto
        int nroRequest = Integer.parseInt(aux[0]);
        String mensaje;

        if (nroRequest == 1) {// 1--recibe mensaje
            System.out.println("Me llego un mensaje " + aux[1]);
            sistema.nuevoMensajeRecibido(aux[1]);
        } else if (nroRequest == 2) {
            String confirmacion = aux[1];

            if (confirmacion.equalsIgnoreCase("false")) {
                // el contacto no existe, mostrar ventana
                mensaje = "No existe el contacto";
            } else {
                // va a ser el nick del usuario
                mensaje = "Se agrego el contacto exitosamente: " + confirmacion;
                sistema.nuevoContacto(confirmacion);    
            }
            controlador.getVista().muestraVentanaEmergente(mensaje);
        }
        else if (nroRequest == 3){
            System.out.println("entre en la confirmacion de login");
            if (aux[1].equals("true")){
                System.out.println("Si me pude logguear");
                controlador.cambiaAVentanaChats();
            }
                
            else{
                System.out.println("No me pude logguear");
                controlador.getVista().muestraVentanaEmergente("Nombre usuario ya en uso");
            }
        }
        // 2#true / 2#false para comfirmar la agenda
        // si le llega un true se agenda directamente, meustra mensaje de contacto
        // exitoso
        // sino le muestra mje de co ntacto no existe
    }

    public PrintWriter getOut() {
        return out;
    }

}
