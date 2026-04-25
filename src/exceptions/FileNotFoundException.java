package exceptions;

@SuppressWarnings("serial")
public class FileNotFoundException extends Exception{
	
	private String nombreArchivo;
	
	public FileNotFoundException(String nombreArchivo) {
		this.nombreArchivo = nombreArchivo;
	}
	@Override
    public String getMessage( )    {
        return "El ingrediente " + nombreArchivo + " está repetido";
    }
}
