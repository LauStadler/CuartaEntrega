package persistencia;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.util.List;

import modelo.Contacto;

public class LectorJson {

     @Override
    public DefaultListModel<Contacto> cargar(String nombreUsuario) throws IOException {
        String nombreArchivo = nombreUsuario + ".json";
        try (Reader reader = new FileReader(nombreArchivo)) {
            return gson.fromJson(reader, new TypeToken<DefaultListModel<Contacto>>(){}.getType());
        }
    }


}
