package persistencia;

import java.io.FileNotFoundException;
import java.io.IOException;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import exceptions.*;
import modelo.Cafe;
import modelo.Torneo;
import modelo.producto.Juego;
import modelo.usuario.Cliente;
import modelo.usuario.Empleado;
import modelo.usuario.Usuario;

public class PersistenciaTorneos extends PersistenciaCentral {

    public static void descargarTorneos(String torneosArchivo, Cafe miCafe)
            throws IOException, FileNotFoundException, JSONException, NumeroJugadoresExcedidoException, InvalidCredentialsException, RestriccionEdadInvalidaException, CategoriaInvalidaException {
        
        JSONArray jTorneos = leerArchivoJSON(torneosArchivo);

        for (int i = 0; i < jTorneos.length(); i++) {
            JSONObject jTorneo = jTorneos.getJSONObject(i);

            String nombre = jTorneo.getString("nombre");
            String tipo = jTorneo.getString("tipo");
            int precio = jTorneo.getInt("precio");
            int numParticipantes = jTorneo.getInt("numParticipantes");
            boolean activo = jTorneo.getBoolean("activo");
            
            JSONObject jJuego = jTorneo.getJSONObject("juego");
            Juego juego = PersistenciaProductos.descargarJuegos(jJuego);

            Torneo nuevoTorneo = new Torneo(tipo, nombre, juego, numParticipantes, precio);
            nuevoTorneo.setActivo(activo);
            
            JSONArray jParticipantesIds = jTorneo.optJSONArray("participantesIds");
            if (jParticipantesIds != null) {
                for (int j = 0; j < jParticipantesIds.length(); j++) {
                    int idUsuario = jParticipantesIds.getInt(j);
                    Usuario participante = buscarUsuarioPorId(idUsuario, miCafe);
                    
                    if (participante != null) {
                        try {
                            nuevoTorneo.agregarParticipantes(participante);
                        } catch (TorneoException e) {
                        }
                    }
                }
            }

            miCafe.getTorneosActivos().add(nuevoTorneo); 
        }
    }

    public static void salvarTorneos(String torneosArchivo, Cafe miCafe) throws IOException, FileNotFoundException {
        JSONArray jTorneos = new JSONArray();

        for (Torneo torneo : miCafe.getTorneosActivos()) {
            JSONObject jTorneo = new JSONObject();
            jTorneo.put("nombre", torneo.getNombre()); 
            jTorneo.put("tipo", torneo.getTipo());
            jTorneo.put("precio", torneo.getPrecio());
            jTorneo.put("numParticipantes", torneo.getNumParticipantes());
            jTorneo.put("activo", torneo.isActivo());
            jTorneo.put("premio", torneo.gePremio());
            
            jTorneo.put("fecha", calendarToString(torneo.getFecha()));

            jTorneo.put("juego", PersistenciaProductos.AsalvarJuegos(torneo.getJuego()));

            JSONArray jParticipantesIds = new JSONArray();
            for (Usuario u : torneo.getParticipantes()) {
                jParticipantesIds.put(u.getId());
            }
            jTorneo.put("participantesIds", jParticipantesIds);

            jTorneos.put(jTorneo);
        }

        guardarArchivoJSON(torneosArchivo, jTorneos);
    }

    private static Usuario buscarUsuarioPorId(int id, Cafe miCafe) {
        for (Cliente c : miCafe.getClientes()) {
            if (c.getId() == id) return c;
        }
        for (Empleado e : miCafe.getEmpleados()) {
            if (e.getId() == id) return e;
        }
        return null;
    }
}