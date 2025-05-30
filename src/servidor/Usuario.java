package servidor;

public class Usuario {

	private AtencionCliente clientHandler;
	private String nickname;
	private boolean conectado = false;
	
	
	public Usuario(String nickname) {
		super();
		this.nickname = nickname;
	}
	public boolean isConectado() {
		return conectado;
	}
	public void setConectado(boolean conectado) {
		this.conectado = conectado;
	}
	
	public String getNickname() {
		return nickname;
	}

    public AtencionCliente getClientHandler() {
        return clientHandler;
    }

    public void setClientHandler(AtencionCliente clientHandler) {
        this.clientHandler = clientHandler;
    }	

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("Usuario{");
        sb.append("clientHandler=").append(clientHandler);
        sb.append(", nickname=").append(nickname);
        sb.append(", conectado=").append(conectado);
        sb.append('}');
        return sb.toString();
    }
	
}
