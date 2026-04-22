package persistencia;



import java.io.IOException;
import modelo.Cafe; 

/**
 * Interfaz que define los métodos necesarios para persistir la información base del café
 * (mesas, empleados y menú de productos).
 */
public interface IPersistenciaCafe
{
    public void cargarCafe( String archivo, Cafe cafe ) throws IOException, Exception; 
    public void salvarCafe( String archivo, Cafe cafe ) throws IOException;
}