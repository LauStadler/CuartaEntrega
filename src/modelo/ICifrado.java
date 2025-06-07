package modelo;

public interface ICifrado {
    public String cifrar(String mensaje)  throws Exception;
    public String descifrar(String mensajeCifrado)  throws Exception;
    public void setClave(String clave);
}
