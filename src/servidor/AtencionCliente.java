package servidor;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class AtencionCliente extends Thread{

	private Socket cliente;
	private Server server;
	private PrintWriter out;
	
	public AtencionCliente(Socket cliente, Server server) throws IOException {
		super();
		this.cliente = cliente;
		this.server = server;
		this.out =  new PrintWriter(cliente.getOutputStream(), true);
	}
	
	public void run() {
		
		try {
			BufferedReader in = new BufferedReader(new InputStreamReader(cliente.getInputStream()));
			while(cliente.isConnected()) {
				String mensaje = in.readLine();
				procesaRequest(mensaje);
			}
			
			cliente.close();
			in.close();
			out.close();
			
		} catch (IOException e) {
			out.close();
			System.out.println("El usuario se desconecto");
			server.logout(this);
			e.printStackTrace();
		}
	}
	
	public void procesaRequest(String request) {

		String aux[] = request.split("#", 2);
		System.out.println("aux[0] "+ aux[0] + " aux[1] "+ aux[1]);
		int nroRequest = Integer.parseInt(aux[0]);
		String rta;
		//este metodo deriva a los demas segun que se le pida al servidor, se llama a los metodos del servidor porque el tiene toda la información para hacerlo.
		//1--Enviar Mensaje
		//2--AgregarContacto
		if (nroRequest == 1){//1--recibe mensaje para enviar a otro usuario
			
			// el request de mensaje llega como: 1#receptor#emisor#hora#texto 
			//hay que separar de nuevo en cuatro el aux[1](aux[0] ya saco el nro)
			String aux2[] = aux[1].split("#", 2);
			String receptor = aux2[0];
			String mensaje = aux2[1];
			// llama a servidor envia mensaje para que busque al destinatario
			server.enviaMensaje(receptor, mensaje);
			// y llame a la funcion envia mensaje de ese cliente que encontro
		}
	   else if (nroRequest == 2){//quiere agendar un usuario, pregunta si esta
			String nickBuscado = aux[1];
			boolean confirmacion = server.usuarioExiste(nickBuscado);
			//2#false si no esta
			//2#nick si existe
			
			if (confirmacion){
				rta = "2#" + nickBuscado;
			}
			else
				rta = "2#false";
			out.println(rta);
		}
		
	}
	
	public void recibeMensaje(String mensaje){
		//si alguien le mando un mensaje a este usuario, se los pasamos mediante el output para que le llegue a su app
		String aux = "1#" + mensaje;
		out.println(aux);
	}
	public void confirmaLogIn(String mensaje){
		String aux = "3#" + mensaje;
		out.println(aux);
	}

}