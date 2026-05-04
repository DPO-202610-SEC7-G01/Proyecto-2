package exceptions;

import modelo.producto.*;

public class ProductosException extends Exception {
    
    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String campo;
    private String codigoError;
    private Object objetoRelacionado;
    
    
    //Para las bebidas
    public ProductosException(Bebida bebida, String campo, String causa) {
        super(construirMensajeCompleto(bebida, campo, causa));
        this.campo = campo;
        this.objetoRelacionado = bebida;
    }
    
    private static String construirMensajeCompleto(Bebida bebida, String campo, String causa) {
        StringBuilder mensaje = new StringBuilder();
        mensaje.append("Error en el campo '").append(campo).append("' de la bebida: ");
        
        if (bebida != null) {
            mensaje.append("ID=").append(bebida.getId());
            mensaje.append(", Nombre='").append(bebida.getNombre()).append("'");
        } else {
            mensaje.append("Bebida nula");
        }
        
        mensaje.append(" | ").append(causa);
        
        return mensaje.toString();
    }

}