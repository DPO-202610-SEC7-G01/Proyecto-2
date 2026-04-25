package modelo.producto;

public class JuegoDificil extends Juego{
	
	
	private String instrucciones;
		
	public JuegoDificil(int id, int precio, String nombre, int anioPublicacion, String empresMatriz, int numJugadores,
			String restriccionEdad, String categoria,String instrucciones) {
		super(id, precio, nombre, anioPublicacion, empresMatriz, numJugadores, restriccionEdad, categoria);
		this.instrucciones = instrucciones;
	}
	
	//Getters Y Setters
	public String getInstrucciones() {
		return instrucciones;
	}
	//Métodos

	@Override
	public boolean requiereInstructor() {
		return true;
	}
	

}
