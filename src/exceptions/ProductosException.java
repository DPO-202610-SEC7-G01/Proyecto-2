package exceptions;

import modelo.producto.*;

public class ProductosException extends Exception {
    
    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
    
    
    //Para las bebidas
    public ProductosException(Bebida bebida, String campo, String causa) {
        super(construirMensajeCompleto(bebida, campo, causa));
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
    
   // Para los platillos 
    public ProductosException(Platillo platillo, String campo, String causa) {
        super(construirMensajeCompleto(platillo, campo, causa));
    }
    
    private static String construirMensajeCompleto(Platillo platillo, String campo, String causa) {
        StringBuilder mensaje = new StringBuilder();
        mensaje.append("Error en el campo '").append(campo).append("' del platillo: ");
        
        if (platillo != null) {
            mensaje.append("ID=").append(platillo.getId());
            mensaje.append(", Nombre='").append(platillo.getNombre()).append("'");
            mensaje.append(", Precio=").append(platillo.getPrecio());
        } else {
            mensaje.append("Platillo nulo");
        }
        
        mensaje.append(" | ").append(causa);
        
        return mensaje.toString();
    }
    
    // Para juegos
    public ProductosException(Juego juego, String campo, String causa) {
        super(construirMensajeCompleto(juego, campo, causa));
    }
    
    private static String construirMensajeCompleto(Juego juego, String campo, String causa) {
        StringBuilder mensaje = new StringBuilder();
        mensaje.append("Error en el campo '").append(campo).append("' del juego: ");
        
        if (juego != null) {
            mensaje.append("ID=").append(juego.getId());
            mensaje.append(", Nombre='").append(juego.getNombre()).append("'");
            mensaje.append(", Precio=").append(juego.getPrecio());
        } else {
            mensaje.append("Juego nulo");
        }
        
        mensaje.append(" | ").append(causa);
        return mensaje.toString();
    }
    
    //Para productos
    public ProductosException(Producto producto, String campo, String causa) {
        super(construirMensajeCompleto(producto, campo, causa));
    }
    
    private static String construirMensajeCompleto(Producto producto, String campo, String causa) {
        StringBuilder mensaje = new StringBuilder();
        mensaje.append("Error en el campo '").append(campo).append("' del producto: ");
        
        if (producto != null) {
            mensaje.append("ID=").append(producto.getId());
            mensaje.append(", Nombre='").append(producto.getNombre()).append("'");
            mensaje.append(", Precio=").append(producto.getPrecio());
        } else {
            mensaje.append("Producto nulo");
        }
        
        mensaje.append(" | ").append(causa);
        
        return mensaje.toString();
    }
    
    
}