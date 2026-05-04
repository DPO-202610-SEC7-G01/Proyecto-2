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
	public Juego(int id, int precio, String nombre, int anioPublicacion, String empresMatriz, int numJugadores,
	        String restriccionEdad, String categoria) throws NumeroJugadoresExcedidoException, RestriccionEdadInvalidaException, CategoriaInvalidaException {
	    super(id, precio, nombre);
	    if(anioPublicacion >0) {
		    this.anioPublicacion = anioPublicacion;
	    }
	    
	    this.empresMatriz = empresMatriz;
	    
	    if (numJugadores >= 1 && numJugadores <= 40) {
	        this.numJugadores = numJugadores;
	    } else {
	        throw new NumeroJugadoresExcedidoException(numJugadores, 1, 40);
	    }

	    if (restriccionEdad.contains("-5") || restriccionEdad.equalsIgnoreCase("Adultos")|| restriccionEdad.equalsIgnoreCase("+14")) {
	        this.restriccionEdad = restriccionEdad;
	    } else {
	        throw new RestriccionEdadInvalidaException(restriccionEdad, new String[]{"-5", "Adultos","+14"});
	    }

	    if (categoria.equals("Tablero") || categoria.equals("Cartas") || categoria.equals("Acción")) {
	        this.categoria = categoria;
	    } else {
	        throw new CategoriaInvalidaException(categoria, new String[]{"Tablero", "Cartas", "Acción"});
	    }

	    this.estado = "nuevo";
	    this.prestado = false;
	}
	
	
	//Getters y Setters
	
	public int getAnioPublicacion() {
		return anioPublicacion;
	}

	public void setAnioPublicacion(int anioPublicacion) {
		if(anioPublicacion >0) {
		    this.anioPublicacion = anioPublicacion;
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

	public void setNumJugadores(int numJugadores) throws NumeroJugadoresExcedidoException {
		if (numJugadores >= 1 && numJugadores <= 40) {
	        this.numJugadores = numJugadores;
	    } else {
	        throw new NumeroJugadoresExcedidoException(numJugadores, 1, 40);
	    }
	}

	public String getRestriccionEdad() {
		return restriccionEdad;
	}

	public void setRestriccionEdad(String restriccionEdad) throws RestriccionEdadInvalidaException {
		 if (restriccionEdad.contains("-5") || restriccionEdad.equalsIgnoreCase("Adultos")) {
		        this.restriccionEdad = restriccionEdad;
		    } else {
		        throw new RestriccionEdadInvalidaException(restriccionEdad, new String[]{"-5", "Adultos"});
		    }
	}
	
	public int extraerEdadMinima(String restriccionEdad) {
	    String texto = restriccionEdad.toLowerCase();

	    if (texto.contains("Adultos")) {
	        return 18;
	    }

	    String numeros = texto.replaceAll("[^0-9]", "");

	    if (!numeros.isEmpty()) {
	        return Integer.parseInt(numeros);
	    }

	    return 0;
	}

	public String getCategoria() {
		return categoria;
	}

	public void setCategoria(String categoria) throws CategoriaInvalidaException {
		if (categoria.equals("Tablero") || categoria.equals("Cartas") || categoria.equals("Acción")) {
	        this.categoria = categoria;
	    } else {
	        throw new CategoriaInvalidaException(categoria, new String[]{"Tablero", "Cartas", "Acción"});
	    }
	}

	public String getEstado() {
		return estado;
	}

	public void setEstado(String estado) { //unicamente para el admin 
		this.estado = estado;
	}

	public boolean estaDisponible() {
		return prestado;
	}

	public void setPrestado(boolean prestado) {
		this.prestado = prestado;
	}
	
	
	//Métodos
	//CONDICIONES PARA PRESTAMO
	public boolean esCategoriaAccion() {
	    return this.categoria != null && this.categoria.equals("Acción");
	}
	
	public boolean esAptoParaEdad(int edad) {
	    if (this.restriccionEdad == null) {
	        return true;
	    }
	    
	    if (this.restriccionEdad.contains("-5")) {
	        return edad >= 5;
	    } else if (this.restriccionEdad.equals("Adulto")) {
	        return edad >= 18;
	    }
	    
	    return false;
	}
	
	public boolean requiereInstructor() {
		return false;
	}
	
	//TRANSACCIÓN
	@Override
	public double getTasaImpuesto() {
	    return super.IVA; // IVA para juegos
	}
	
	
}
