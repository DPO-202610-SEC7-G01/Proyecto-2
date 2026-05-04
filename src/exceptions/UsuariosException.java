package exceptions;

import modelo.usuario.Usuario;

public class UsuariosException extends Exception {
    
    private static final long serialVersionUID = 1L;
    private String campo;
    private String codigoError;
    private Object objetoRelacionado;
    
    // Base
    
    public UsuariosException() {
        super();
    }
    
    public UsuariosException(String message) {
        super(message);
    }
    
    public UsuariosException(String campo, String message) {
        super(message);
        this.campo = campo;
    }
    
    // Usuario 
    public UsuariosException(Usuario usuario, String campo, String causa) {
        super(construirMensajeCompleto(usuario, campo, causa));
        this.campo = campo;
        this.objetoRelacionado = usuario;
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
    
}