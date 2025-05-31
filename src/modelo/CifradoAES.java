package modelo;

import java.util.Base64;
import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;

public class CifradoAES implements ICifrado{

  
    private final SecretKeySpec clave;

    public CifradoAES(String claveTexto) {
        // Asegurarse de que la clave tenga 16 bytes (AES-128)
        byte[] claveBytes = claveTexto.getBytes();
        this.clave = new SecretKeySpec(claveBytes, 0, 16, "AES");
    }

    @Override
    public String cifrar(String mensaje) throws Exception {
        Cipher cipher = Cipher.getInstance("AES");
        cipher.init(Cipher.ENCRYPT_MODE, clave);
        byte[] mensajeCifrado = cipher.doFinal(mensaje.getBytes("UTF-8"));
        return Base64.getEncoder().encodeToString(mensajeCifrado);
    } //return cipher.doFinal(texto.getBytes());

    @Override
    public String descifrar(String mensajeCifrado) throws Exception {
        Cipher cipher = Cipher.getInstance("AES");
        cipher.init(Cipher.DECRYPT_MODE, clave);
        byte[] mensajeDescifrado = cipher.doFinal(Base64.getDecoder().decode(mensajeCifrado));
        return new String(mensajeDescifrado, "UTF-8");
    }
}
