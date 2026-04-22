package modelo;

public class Mesa {
	
	private int id;
	private int numSillas;
	private boolean disponible;
	
	//Constructor
	public Mesa(int id, int numSillas, boolean disponible) {
		super();
		this.id = id;
		this.numSillas = numSillas;
		this.disponible = disponible;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getNumSillas() {
		return numSillas;
	}

	public void setNumSillas(int numSillas) {
		this.numSillas = numSillas;
	}

	public boolean isDisponible() {
		return disponible;
	}

	public void setDisponible(boolean disponible) {
		this.disponible = disponible;
	}
	
	
	
	

}
