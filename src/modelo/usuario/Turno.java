package modelo.usuario;

import java.util.Calendar;

public class Turno {

	    private Calendar fecha;
	    private boolean activo;  
	    
	    // Constructor
	    public Turno(Calendar fecha, boolean activo) {
	        this.fecha = fecha;
	        this.activo = activo;
	    }
	    
	    public Turno(Calendar fecha) {
	        this.fecha = fecha;
	        this.activo = true;  
	    }
	    
	    // Getters y Setters
	    public Calendar getFecha() {
	        return fecha;
	    }
	    
	    public void setFecha(Calendar fecha) {
	        this.fecha = fecha;
	    }
	    
	    public boolean isActivo() {
	        return activo;
	    }
	    
	    public void setActivo(boolean activo) {
	        this.activo = activo;
	    }
	    
	    // Métodos 
	    public boolean esMismaFecha(Calendar otraFecha) {
	        return this.fecha.get(Calendar.YEAR) == otraFecha.get(Calendar.YEAR) &&
	               this.fecha.get(Calendar.DAY_OF_YEAR) == otraFecha.get(Calendar.DAY_OF_YEAR);
	    }
	    
	    @Override
	    public String toString() {
	        return String.format("Fecha: %d-%d-%d, Activo: %s",
	            fecha.get(Calendar.YEAR),
	            fecha.get(Calendar.MONTH) + 1,
	            fecha.get(Calendar.DAY_OF_MONTH),
	            activo ? "Trabaja" : "Descansa"
	        );
	    }
	    
	    @Override
	    public boolean equals(Object obj) {
	        if (this == obj) return true;
	        if (obj == null || getClass() != obj.getClass()) return false;
	        Turno otro = (Turno) obj;
	        return esMismaFecha(otro.fecha);
	    }
	    
	    @Override
	    public int hashCode() {
	        return fecha.get(Calendar.YEAR) * 10000 + 
	               fecha.get(Calendar.DAY_OF_YEAR);
	    }
}
	

