package persistencia;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.time.LocalDate;
import java.util.Calendar;

import org.json.JSONArray;
import org.json.JSONObject;


public class PersistenciaCentral {
	
	protected static JSONArray leerArchivoJSON(String archivoPath) throws IOException, FileNotFoundException {
	    File archivo = new File(archivoPath);
	    if (!archivo.exists()) {
	        throw new FileNotFoundException(archivoPath);
	    }
	    String contenido = new String(Files.readAllBytes(archivo.toPath()));
	    return new JSONArray(contenido);
	}
	
	protected static void guardarArchivoJSON(String archivo, JSONArray jEmpleados) 
			throws IOException, FileNotFoundException {
    try (FileWriter fileWriter = new FileWriter(archivo)) {
        fileWriter.write(jEmpleados.toString(4));
    	}
	}
	
	protected static void guardarArchivoJSON(String archivo, JSONObject jEmpleados) 
			throws IOException, FileNotFoundException {
    try (FileWriter fileWriter = new FileWriter(archivo)) {
        fileWriter.write(jEmpleados.toString(4));
   		}
	}
	
	protected static Calendar fechaEnCalendar(String fechaString) {
	    Calendar calendar = Calendar.getInstance();
	    
	    if (fechaString.contains("T")) {
	        java.time.ZonedDateTime zdt = java.time.ZonedDateTime.parse(fechaString);
	        calendar.set(
	            zdt.getYear(), 
	            zdt.getMonthValue() - 1, 
	            zdt.getDayOfMonth(), 
	            zdt.getHour(), 
	            zdt.getMinute(), 
	            zdt.getSecond()
	        );
	    } 
	    else {
	        java.time.LocalDate ld = java.time.LocalDate.parse(fechaString);
	        calendar.set(
	            ld.getYear(), 
	            ld.getMonthValue() - 1, 
	            ld.getDayOfMonth(), 
	            0, 0, 0 
	        );
	    }
	    
	    calendar.set(Calendar.MILLISECOND, 0);
	    return calendar;
	}
	
	protected static String calendarToString(Calendar calendar) {
	    if (calendar == null) {
	        return "";
	    }
	    return String.format("%d-%02d-%02d",
	            calendar.get(Calendar.YEAR),
	            calendar.get(Calendar.MONTH) + 1,
	            calendar.get(Calendar.DAY_OF_MONTH));
	}
	
}
