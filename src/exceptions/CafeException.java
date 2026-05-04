package exceptions;

import modelo.Cafe;
import modelo.Torneo;
import modelo.usuario.Usuario;

public class CafeException extends Exception {
    
    private static final long serialVersionUID = 1L;
    
    // ===== CONSTRUCTOR PARA USUARIO =====
    
    public CafeException(Usuario usuario, String campo, String causa) {
        super(construirMensajeUsuario(usuario, campo, causa));
    }
    
    private static String construirMensajeUsuario(Usuario usuario, String campo, String causa) {
        StringBuilder mensaje = new StringBuilder();
        mensaje.append("Error en el campo '").append(campo).append("' del usuario: ");
        
        if (usuario != null) {
            mensaje.append("ID=").append(usuario.getId());
            mensaje.append(", Login='").append(usuario.getLogin()).append("'");
            mensaje.append(", Nombre='").append(usuario.getNombre()).append("'");
        } else {
            mensaje.append("Usuario nulo");
        }
        
        mensaje.append(" | ").append(causa);
        
        return mensaje.toString();
    }
    
    // Torneo
    
    public CafeException(Torneo torneo, String campo, String causa) {
        super(construirMensajeTorneo(torneo, campo, causa));
    }
    
    private static String construirMensajeTorneo(Torneo torneo, String campo, String causa) {
        StringBuilder mensaje = new StringBuilder();
        mensaje.append("Error en el campo '").append(campo).append("' del torneo: ");
        
        if (torneo != null) {
            mensaje.append("Nombre='").append(torneo.getNombre()).append("'");
            mensaje.append(", Tipo='").append(torneo.getTipo()).append("'");
            mensaje.append(", Juego='").append(torneo.getJuego().getNombre()).append("'");
            mensaje.append(", Participantes=").append(torneo.getNumParticipantes());
        } else {
            mensaje.append("Torneo nulo");
        }
        
        mensaje.append(" | ").append(causa);
        
        return mensaje.toString();
    }

    
}
