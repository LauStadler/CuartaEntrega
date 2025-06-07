package modelo;

//esta es la clase "Contexto" del patron strategy
public class Cifrador {
   
    private ICifrado cifrador;

    public Cifrador(){
        super();
    }

    public void setClave(String clave){
        cifrador.setClave(clave);
    }


    public void setCifrador(ICifrado cifrador){
        this.cifrador = cifrador;
    }

    public String cifrar(String mensaje){
        try {
            return this.cifrador.cifrar(mensaje);
        } catch (Exception ex) {
        }
        return mensaje;
    }

    public String descifrar(String mensaje){
        try {
            return this.cifrador.descifrar(mensaje);
        } catch (Exception ex) {
        }
        return mensaje;
    }
}
