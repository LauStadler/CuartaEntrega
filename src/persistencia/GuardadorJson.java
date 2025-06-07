package persistencia;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Reader;
import java.io.Writer;
import java.util.ArrayList;
import java.util.List;

import javax.swing.DefaultListModel;

import modelo.Contacto;

public class GuardadorJson implements IGuardador {

    private final Gson gson = new Gson();

    @Override
    public void guardar(DefaultListModel<Contacto> contactos, String nombreUsuario) throws IOException {
        String nombreArchivo = nombreUsuario + ".json";
        // Convertir DefaultListModel a List<Contacto>
        List<Contacto> lista = new ArrayList<>();
        for (int i = 0; i < contactos.size(); i++) {
            lista.add(contactos.get(i));
        }
        try (FileWriter writer = new FileWriter(nombreArchivo)) {
            gson.toJson(lista, writer);
        }
    }
}
