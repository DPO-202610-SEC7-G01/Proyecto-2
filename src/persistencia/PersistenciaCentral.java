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
	
}
