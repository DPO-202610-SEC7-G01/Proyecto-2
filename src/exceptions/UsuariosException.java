package exceptions;

import modelo.usuario.*;

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
}