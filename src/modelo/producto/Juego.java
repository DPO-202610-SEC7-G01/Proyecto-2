package modelo.producto;

import exceptions.*;
public class Juego extends Producto{
	
	
	private int anioPublicacion;
	private String empresMatriz;
	private int numJugadores;
	private String restriccionEdad;
	private String categoria;
	private String estado;
	private boolean prestado;
	
	//Constructor
	 public Juego(int id, int precio, String nombre, int anioPublicacion, String empresMatriz, 
             int numJugadores, String restriccionEdad, String categoria) 
        throws ProductosException {
    super(id, precio, nombre);
    
    if (anioPublicacion > 0) {
        this.anioPublicacion = anioPublicacion;
    } else {
        throw new ProductosException(this, "anioPublicacion", 
            "El año de publicación debe ser positivo. Valor recibido: " + anioPublicacion);
    }
    
    this.empresMatriz = empresMatriz;
    
    if (numJugadores >= 1 && numJugadores <= 40) {
        this.numJugadores = numJugadores;
    } else {
        throw new ProductosException(this, "numJugadores", 
            "Número de jugadores inválido: " + numJugadores + ". Debe estar entre 1 y 40.");
    }
    
    if (restriccionEdad.equals("-5") || 
        restriccionEdad.equalsIgnoreCase("Adultos") ) {
        this.restriccionEdad = restriccionEdad;
    } else {
        throw new ProductosException(this, "restriccionEdad", 
            "Restricción de edad inválida: '" + restriccionEdad + 
            "'. Debe ser: '-5' o 'Adultos'.");
    }
    
    if (categoria.equals("Tablero") || categoria.equals("Cartas") || categoria.equals("Acción")) {
        this.categoria = categoria;
    } else {
        throw new ProductosException(this, "categoria", 
            "Categoría inválida: '" + categoria + 
            "'. Debe ser: 'Tablero', 'Cartas' o 'Acción'.");
    }
    
    this.estado = "nuevo";
    this.prestado = false;
	 }

	//Getters y Setters
	
	public int getAnioPublicacion() {
		return anioPublicacion;
	}

	public void setAnioPublicacion(int anioPublicacion) throws ProductosException {
		if(anioPublicacion >0) {
		    this.anioPublicacion = anioPublicacion;
	    }else {
	        throw new ProductosException(this, "anioPublicacion", 
	                "El año de publicación debe ser positivo. Valor recibido: " + anioPublicacion);
	        }
	}

	public String getEmpresMatriz() {
		return empresMatriz;
	}

	public void setEmpresMatriz(String empresMatriz) {
		this.empresMatriz = empresMatriz;
	}

	public int getNumJugadores() {
		return numJugadores;
	}

	public void setNumJugadores(int numJugadores) throws ProductosException {
	    if (numJugadores >= 1 && numJugadores <= 40) {
	        this.numJugadores = numJugadores;
	    } else {
	        throw new ProductosException(this, "numJugadores", 
	            "Número de jugadores inválido: " + numJugadores + ". Debe estar entre 1 y 40.");
	    }
	}

	public String getRestriccionEdad() {
		return restriccionEdad;
	}

	public void setRestriccionEdad(String restriccionEdad) throws ProductosException {
	    if (restriccionEdad.equals("-5") || 
	        restriccionEdad.equalsIgnoreCase("Adultos")) {
	        this.restriccionEdad = restriccionEdad;
	    } else {
	        throw new ProductosException(this, "restriccionEdad", 
	            "Restricción de edad inválida: '" + restriccionEdad + 
	            "'. Debe ser: '-5' o 'Adultos'");
	    }
	}
	
	public int extraerEdadMinima(String restriccionEdad) {    
	    String texto = restriccionEdad.toLowerCase();

	    if (texto.contains("adultos")) {
	        return 18;
	    }

	    if (texto.contains("-5")) {
	        return 5;
	    }

	    return 0;
	}

	public String getCategoria() {
		return categoria;
	}

	public void setCategoria(String categoria) throws ProductosException {
	    if (categoria.equals("Tablero") || categoria.equals("Cartas") || categoria.equals("Acción")) {
	        this.categoria = categoria;
	    } else {
	        throw new ProductosException(this, "categoria", 
	            "Categoría inválida: '" + categoria + 
	            "'. Debe ser: 'Tablero', 'Cartas' o 'Acción'.");
	    }
	}

	public String getEstado() {
		return estado;
	}

	public void setEstado(String estado) { 
		this.estado = estado;
	}

	public boolean estaDisponible() {
		return prestado;
	}

	public void setPrestado(boolean prestado) {
		this.prestado = prestado;
	}
	
	
	//Métodos
	public boolean esCategoriaAccion() {
	    return this.categoria != null && this.categoria.equals("Acción");
	}
	
	public boolean esAptoParaEdad(int edad) { 
	    if (this.restriccionEdad.contains("-5")) {
	        return edad >= 5;
	    } else {
	        return edad >= 18;
	    }
	}
	
	public boolean requiereInstructor() {
		return false;
	}
	
	//TRANSACCIÓN
	@Override
	public double getTasaImpuesto() {
	    return super.IVA; // IVA para juegos 19% 
	}
	
	
}
