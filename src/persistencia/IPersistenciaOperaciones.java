package persistencia;

import java.io.IOException;
import modelo.Cafe;




public interface IPersistenciaOperaciones
{
    /**
     * Carga la información de los clientes, reservas y transacciones, y actualiza la estructura 
     * de objetos que se encuentra dentro del café.
     * @param archivo La ruta al archivo que contiene la información que se va a cargar.
     * @param cafe El café dentro del cual debe almacenarse la información (necesario para vincular mesas y usuarios).
     * @throws IOException Se lanza esta excepción si hay problemas leyendo el archivo.
     * @throws Exception Se lanza esta excepción si hay información inconsistente dentro del archivo, o entre el archivo y el estado del café (por ejemplo, si una reserva apunta a una mesa que no existe).
     */
    public void cargarOperaciones( String archivo, Cafe cafe ) throws IOException, Exception; 

    /**
     * Salva en un archivo toda la información sobre los clientes, reservas y transacciones del café. 
     * @param archivo La ruta al archivo donde debe quedar almacenada la información.
     * @param cafe El café que tiene la información que se quiere almacenar.
     * @throws IOException Se lanza esta excepción si hay problemas escribiendo el archivo.
     */
    public void salvarOperaciones( String archivo, Cafe cafe ) throws IOException;

}