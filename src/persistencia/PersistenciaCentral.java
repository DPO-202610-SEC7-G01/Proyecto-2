package persistencia;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.time.LocalDate;
import java.util.Calendar;

import org.json.JSONArray;
import org.json.JSONObject;

import exceptions.FileNotFoundException;

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
	    LocalDate fechaLocal = LocalDate.parse(fechaString);
	    Calendar calendar = Calendar.getInstance();
	    calendar.set(fechaLocal.getYear(), fechaLocal.getMonthValue() - 1, fechaLocal.getDayOfMonth());
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
