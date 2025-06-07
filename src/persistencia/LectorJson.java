package persistencia;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import javax.swing.DefaultListModel;

import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.util.List;

import modelo.Contacto;

public class LectorJson extends AbstractLector  implements ILector {

    private final Gson gson = new Gson();

    @Override
    public DefaultListModel<Contacto> cargarContactos(String nombreUsuario) throws IOException {
        String nombreArchivo = nombreUsuario + ".json";
        try (Reader reader = new FileReader(nombreArchivo)) {
            return gson.fromJson(reader, new TypeToken<DefaultListModel<Contacto>>(){}.getType());
        }
    }
}