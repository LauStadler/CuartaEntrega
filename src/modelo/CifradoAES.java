package modelo;

import java.util.Base64;
import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

public class CifradoAES implements ICifrado{

    private SecretKey claveAES;

    public CifradoAES() {
        // El SecretKey se setea cuando le seteás la clave.
    }

    public void setClave(String clave){
        // Asegurarse de que la clave tenga 16 bytes (AES-128)
        byte[] claveBytes = clave.getBytes();
        this.claveAES = new SecretKeySpec(claveBytes, 0, 16, "AES");
    }

    @Override
    public String cifrar(String mensaje) throws Exception {
        Cipher cipher = Cipher.getInstance("AES");
        cipher.init(Cipher.ENCRYPT_MODE, claveAES);
        byte[] mensajeCifrado = cipher.doFinal(mensaje.getBytes("UTF-8"));
        return Base64.getEncoder().encodeToString(mensajeCifrado);
    }

    @Override
    public String descifrar(String mensajeCifrado) throws Exception {
        Cipher cipher = Cipher.getInstance("AES");
        cipher.init(Cipher.DECRYPT_MODE, claveAES);
        byte[] mensajeDescifrado = cipher.doFinal(Base64.getDecoder().decode(mensajeCifrado));
        return new String(mensajeDescifrado, "UTF-8");
    }

    public String getClave() {
        // Si querés devolver la clave en texto
        return new String(claveAES.getEncoded());
    }
}
