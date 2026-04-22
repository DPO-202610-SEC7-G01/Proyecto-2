package modelo.producto;

public class Juego extends Producto{
	
	
	private int anioPublicacion;
	private String empresMatriz;
	private int numJugadores;
	private String restriccionEdad;
	private String categoria;
	private String estado;
	private boolean prestado;
	
	//Constructor
	public Juego(int id, int precio, String nombre, int anioPublicacion, String empresMatriz, int numJugadores,
	        String restriccionEdad, String categoria) {
	    super(id, precio, nombre);
	    this.anioPublicacion = anioPublicacion;
	    this.empresMatriz = empresMatriz;
	    
	    // Validación: Máximo 40 y mínimo 1 jugador 
	    if (numJugadores >= 1 && numJugadores <= 40) {
	        this.numJugadores = numJugadores;
	    } else {
	        this.numJugadores = 1;
	    }

	    // Validación: Restricción de edad específica 
	    if (restriccionEdad.equals("apto 5 anios") || restriccionEdad.equals("apto adultos")) {
	        this.restriccionEdad = restriccionEdad;
	    } else {
	        this.restriccionEdad = "apto adultos"; 
	    }

	    // Validación: Categorías permitidas
	    if (categoria.equals("Tablero") || categoria.equals("Cartas") || categoria.equals("Acción")) {
	        this.categoria = categoria;
	    } else {
	        this.categoria = "Tablero"; 
	    }

	    this.estado = "nuevo";
	    this.prestado = false;
	}
	
	
	//Getters y Setters

	public int getAnioPublicacion() {
		return anioPublicacion;
	}

	public void setAnioPublicacion(int anioPublicacion) {
		this.anioPublicacion = anioPublicacion;
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

	public void setNumJugadores(int numJugadores) {
		 if (numJugadores >= 1 && numJugadores <= 40) {
		       this.numJugadores = numJugadores;
		    } else {
		        this.numJugadores = 1;
		    }
	}

	public String getRestriccionEdad() {
		return restriccionEdad;
	}

	public void setRestriccionEdad(String restriccionEdad) {
		if (restriccionEdad.equals("apto 5 anios") || restriccionEdad.equals("apto adultos")) {
	        this.restriccionEdad = restriccionEdad;
	    } else {
	        this.restriccionEdad = "apto adultos"; 
	    }
	}

	public String getCategoria() {
		return categoria;
	}

	public void setCategoria(String categoria) {
		if (categoria.equals("Tablero") || categoria.equals("Cartas") || categoria.equals("Acción")) {
	        this.categoria = categoria;
	    } else {
	        this.categoria = "Tablero"; 
	    }
	}

	public String getEstado() {
		return estado;
	}

	public void setEstado(String estado) {
		this.estado = estado;
	}

	public boolean isPrestado() {
		return prestado;
	}

	public void setPrestado(boolean prestado) {
		this.prestado = prestado;
	}
	
	
	//Métodos
	@Override
	public double getTasaImpuesto() {
	    return super.IVA; // IVA para juegos
	}
	
	@Override
	public String getCategoriaProducto() {
		return "Juego";
	}
	
	public boolean requiereInstructor() {
		return false;
	}
	
	public boolean esAptoParaEdad(int edad) {
	    if (this.restriccionEdad == null) {
	        return true;
	    }
	    
	    if (this.restriccionEdad.equals("apto 5 anios")) {
	        return edad >= 5;
	    } else if (this.restriccionEdad.equals("apto adultos")) {
	        return edad >= 18;
	    }
	    
	    return false;
	}
	
	public boolean estaDisponible() {
		return this.prestado;
	}
	
	public boolean esCategoriaAccion() {
	    return this.categoria != null && this.categoria.equals("Acción");
	}

	

	
	
	
	
}
