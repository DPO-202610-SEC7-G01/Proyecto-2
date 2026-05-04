package exceptions;

import modelo.usuario.*;
import modelo.producto.*;

import java.util.ArrayList;
import java.util.Calendar;

import modelo.*;


public class UsuariosException extends Exception {
    
    private static final long serialVersionUID = 1L;
    
    // Usuario 
    public UsuariosException(Usuario usuario, String campo, String causa) {
        super(construirMensajeCompleto(usuario, campo, causa));
    }
    
    
    private static String construirMensajeCompleto(Usuario usuario, String campo, String causa) {
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
    
    //Cliente
    public UsuariosException(Cliente cliente, String campo, String causa) {
        super(construirMensajeCompleto(cliente, campo, causa));
    }
    
    private static String construirMensajeCompleto(Cliente cliente, String campo, String causa) {
        StringBuilder mensaje = new StringBuilder();
        mensaje.append("Error en el campo '").append(campo).append("' del cliente: ");
        
        if (cliente != null) {
            mensaje.append("ID=").append(cliente.getId());
            mensaje.append(", Login='").append(cliente.getLogin()).append("'");
            mensaje.append(", Nombre='").append(cliente.getNombre()).append("'");
            mensaje.append(", Edad=").append(cliente.getEdad());
        } else {
            mensaje.append("Cliente nulo");
        }
        
        mensaje.append(" | ").append(causa);
        
        return mensaje.toString();
    }
    
    public UsuariosException(Torneo torneo, String campo, String causa) {
        super(construirMensajeTorneo(torneo, campo, causa));
    }
    
    private static String construirMensajeTorneo(Torneo torneo, String campo, String causa) {
        StringBuilder mensaje = new StringBuilder();
        mensaje.append("Error en el campo '").append(campo).append("' del torneo: ");
        
        if (torneo != null) {
            mensaje.append("Nombre='").append(torneo.getNombre()).append("'");
            mensaje.append(", Tipo='").append(torneo.getTipo()).append("'");
            mensaje.append(", Juego='").append(torneo.getJuego().getNombre()).append("'");
        } else {
            mensaje.append("Torneo nulo");
        }
        
        mensaje.append(" | ").append(causa);
        
        return mensaje.toString();
    }
    
    // Mesero
    public static final String RAZON_EDAD = "EDAD";
    public static final String RAZON_JUGADORES = "JUGADORES";
    public static final String RAZON_DISPONIBILIDAD = "DISPONIBILIDAD";
	public static final String RAZON_CATEGORIA = "CATEGORIA";
	public static final String RAZON_LIMITE = "LIMITE";
	public static final String RAZON_COCINERO_NO_DISPONIBLE = "COCINERO_NO_DISPONIBLE";
	public static final String RAZON_COCINERO_NO_SABE_PLATILLO = "COCINERO_NO_SABE_PLATILLO";
	public static final String RAZON_ALERGENO_PELIGROSO = "ALERGENO_PELIGROSO";
	public static final String RAZON_BEBIDA_NO_CONOCIDA = "BEBIDA_NO_CONOCIDA";
	
    public UsuariosException(Juego juego, String razon, String causa) {
        super(construirMensajeJuegoNoApto(juego, razon, causa));
    }
    
    private static String construirMensajeJuegoNoApto(Juego juego, String razon, String causa) {
        StringBuilder mensaje = new StringBuilder();
        mensaje.append("El juego no es apto para esta reserva: ");
        
        if (juego != null) {
            mensaje.append("ID=").append(juego.getId());
            mensaje.append(", Nombre='").append(juego.getNombre()).append("'");
            mensaje.append(", Categoría='").append(juego.getCategoria()).append("'");
        } else {
            mensaje.append("Juego nulo");
        }
        
        mensaje.append(" | Razón: ");
        
        switch(razon) {
            case RAZON_EDAD:
                mensaje.append("Edad insuficiente");
                break;
            case RAZON_JUGADORES:
                mensaje.append("Número de jugadores insuficiente");
                break;
            case RAZON_DISPONIBILIDAD:
                mensaje.append("Juego no disponible");
                break;
            default:
                mensaje.append(razon);
        }
        
        mensaje.append(" | ").append(causa);
        
        return mensaje.toString();
    }
    

    public UsuariosException(Cliente cliente, Juego juego, int edadMinima) {
        super(construirMensajeEdadInsuficiente(cliente, juego, edadMinima));
    }
    
    private static String construirMensajeEdadInsuficiente(Cliente cliente, Juego juego, int edadMinima) {
        StringBuilder mensaje = new StringBuilder();
        mensaje.append("El juego no es apto por edad: ");
        
        if (juego != null) {
            mensaje.append("Juego='").append(juego.getNombre()).append("'");
            mensaje.append(", Edad mínima requerida=").append(edadMinima);
        }
        
        if (cliente != null) {
            mensaje.append(" | Cliente='").append(cliente.getNombre()).append("'");
            mensaje.append(", Edad del cliente=").append(cliente.getEdad());
        }
        
        mensaje.append(" | El cliente no cumple con la edad mínima requerida para este juego.");
        
        return mensaje.toString();
    }
    
    public UsuariosException(Juego juego, int jugadoresRequeridos, int jugadoresDisponibles) {
        super(construirMensajeJugadoresInsuficientes(juego, jugadoresRequeridos, jugadoresDisponibles));
    }
    
    private static String construirMensajeJugadoresInsuficientes(Juego juego, int jugadoresRequeridos, int jugadoresDisponibles) {
        StringBuilder mensaje = new StringBuilder();
        mensaje.append("El juego no es apto por número de jugadores: ");
        
        if (juego != null) {
            mensaje.append("Juego='").append(juego.getNombre()).append("'");
            mensaje.append(", Jugadores requeridos=").append(jugadoresRequeridos);
        }
        
        mensaje.append(" | Jugadores disponibles en la reserva=").append(jugadoresDisponibles);
        mensaje.append(" | No hay suficientes jugadores para este juego.");
        
        return mensaje.toString();
    }
    
    public UsuariosException(Reserva reserva, String razon, String causa) {
        super(construirMensajeSinCocinero(reserva, causa));
    }
    
    private static String construirMensajeSinCocinero(Reserva reserva, String causa) {
        StringBuilder mensaje = new StringBuilder();
        mensaje.append("Error al servir platillo: ");
        
        if (reserva != null) {
            mensaje.append(", Fecha='").append(formatearFecha(reserva.getFecha())).append("'");
            mensaje.append(", Clientes=").append(reserva.getClientes().size());
        } else {
            mensaje.append("Reserva nula");
        }
        
        mensaje.append(" | ").append(causa);
        mensaje.append(" | No hay ningún cocinero disponible en el turno actual.");
        
        return mensaje.toString();
    }
    
    
    public UsuariosException(Cocinero cocinero, Platillo platillo, Reserva reserva) {
        super(construirMensajeCocineroNoSabePlatillo(cocinero, platillo, reserva));
    }
    
    private static String construirMensajeCocineroNoSabePlatillo(Cocinero cocinero, Platillo platillo, Reserva reserva) {
        StringBuilder mensaje = new StringBuilder();
        mensaje.append("Error al servir platillo: ");
        
        if (platillo != null) {
            mensaje.append("Platillo='").append(platillo.getNombre()).append("'");
            mensaje.append(", ID=").append(platillo.getId());
        } else {
            mensaje.append("Platillo nulo");
        }
        
        if (cocinero != null) {
            mensaje.append(" | Cocinero='").append(cocinero.getNombre()).append("'");
            mensaje.append(", ID=").append(cocinero.getId());
            mensaje.append(" | Platillos que sabe: ");
            mensaje.append(listarPlatillos(cocinero.getPlatillosConocidos()));
        } else {
            mensaje.append(" | Cocinero nulo");
        }
        
        mensaje.append(" | El cocinero de turno no sabe preparar este platillo.");
        
        return mensaje.toString();
    }
    
    public UsuariosException(Cliente cliente, Platillo platillo, String ingredientePeligroso, Reserva reserva) {
        super(construirMensajeAlergenoPeligroso(cliente, platillo, ingredientePeligroso, reserva));
    }
    
    
    private static String construirMensajeAlergenoPeligroso(Cliente cliente, Platillo platillo, 
            String ingredientePeligroso, Reserva reserva) {
			StringBuilder mensaje = new StringBuilder();
			mensaje.append("⚠️ ALERTA DE SEGURIDAD ALIMENTARIA ⚠️\n");
			mensaje.append("No se puede servir el platillo por riesgo de alergia:\n");
			
			if (platillo != null) {
			mensaje.append("• Platillo: '").append(platillo.getNombre()).append("'\n");
			mensaje.append("• Ingrediente problemático: '").append(ingredientePeligroso).append("'\n");
			}
			
			if (cliente != null) {
			mensaje.append("• Cliente afectado: '").append(cliente.getNombre()).append("'\n");
			mensaje.append("• Alergenos del cliente: ").append(listarAlergenos(cliente.getAlergenos())).append("\n");
			}
		
			
			mensaje.append("• Acción requerida: Contactar al cliente y ofrecer alternativas.");
			
	 return mensaje.toString();
    }
    
	 
	
	 
	 public UsuariosException(Cocinero cocinero, Bebida bebida, Reserva reserva) {
		    super(construirMensajeCocineroNoSabeBebida(cocinero, bebida, reserva));
		}
	 
	 private static String construirMensajeCocineroNoSabeBebida(Cocinero cocinero, Bebida bebida, Reserva reserva) {
		    StringBuilder mensaje = new StringBuilder();
		    mensaje.append("Error al servir bebida: ");
		    
		    if (bebida != null) {
		        mensaje.append("Bebida='").append(bebida.getNombre()).append("'");
		        mensaje.append(", ID=").append(bebida.getId());
		        mensaje.append(", Temperatura='").append(bebida.getTemperatura()).append("'");
		        if (bebida.isTieneAlcohol()) {
		            mensaje.append(", Alcohólica=Sí");
		        }
		    } else {
		        mensaje.append("Bebida nula");
		    }
		    
		    if (cocinero != null) {
		        mensaje.append(" | Cocinero='").append(cocinero.getNombre()).append("'");
		        mensaje.append(", ID=").append(cocinero.getId());
		        mensaje.append(" | Bebidas que sabe: ");
		        mensaje.append(listarBebidas(cocinero.getBebidasConocidas()));
		    } else {
		        mensaje.append(" | Cocinero nulo");
		    }
		    
		    mensaje.append(" | El cocinero de turno no sabe preparar esta bebida.");
		    
		    return mensaje.toString();
		}
	 
	 
	 public UsuariosException(Bebida bebida, String razon, String causa) {
		    super(construirMensajeBebidaConJuegoAccion(bebida, causa));
		}
	 
	 private static String construirMensajeBebidaConJuegoAccion(Bebida bebida, String causa) {
		    StringBuilder mensaje = new StringBuilder();
		    
		    if (bebida != null) {
		        mensaje.append("Bebida: '").append(bebida.getNombre()).append("'");
		        mensaje.append(", Temperatura: '").append(bebida.getTemperatura()).append("'\n");
		    }
		    
		    mensaje.append("Juego de acción detectado en la reserva.\n");
		    mensaje.append(causa);
		    mensaje.append("\n\n Recomendación: Servir bebidas frías o esperar a que termine el juego de acción.");
		    
		    return mensaje.toString();
		}
	 
	 // aux 
	 private static String formatearFecha(Calendar fecha) {
	        if (fecha == null) return "Sin fecha";
	        return fecha.get(Calendar.DAY_OF_MONTH) + "/" + 
	               (fecha.get(Calendar.MONTH) + 1) + "/" + 
	               fecha.get(Calendar.YEAR);
	    }
	 
	 private static String listarPlatillos(ArrayList<Platillo> list) {
	        if (list == null || list.isEmpty()) {
	            return "Ninguno";
	        }
	        StringBuilder sb = new StringBuilder();
	        for (int i = 0; i < list.size(); i++) {
	            if (i > 0) sb.append(", ");
	            sb.append("'").append(list.get(i).getNombre()).append("'");
	        }
	        return sb.toString();
	 } 
	   
	 private static String listarAlergenos(ArrayList<String> alergenos) {
	        if (alergenos == null || alergenos.isEmpty()) {
	            return "Ninguno";
	        }
	        return String.join(", ", alergenos);
	    }
	 
	 private static String listarBebidas(ArrayList<Bebida> bebidas) {
		    if (bebidas == null || bebidas.isEmpty()) {
		        return "Ninguna";
		    }
		    StringBuilder sb = new StringBuilder();
		    for (int i = 0; i < bebidas.size(); i++) {
		        if (i > 0) sb.append(", ");
		        sb.append("'").append(bebidas.get(i).getNombre()).append("'");
		    }
		    return sb.toString();
	 }
        
}