package exceptions;

import java.util.Calendar;

public class EmpleadoEnTurnoException extends Exception {
    
    public EmpleadoEnTurnoException() {
        super("El empleado se encuentra actualmente en turno.");
    }
    
    public EmpleadoEnTurnoException(String mensaje) {
        super(mensaje);
    }
    
    public EmpleadoEnTurnoException(String nombre, String fecha) {
        super("El empleado " + nombre + " se encuentra en turno el día " + fecha);
    }
    
    public EmpleadoEnTurnoException(int idEmpleado, Calendar fecha) {
        super("El empleado con ID " + idEmpleado + " está en turno el día " + 
              fecha.get(Calendar.DAY_OF_MONTH) + "/" + (fecha.get(Calendar.MONTH) + 1) + "/" + 
              fecha.get(Calendar.YEAR));
    }
}