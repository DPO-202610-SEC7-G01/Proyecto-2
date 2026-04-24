package persistencia;

/**
 * Esta clase centraliza el acceso a los implementadores de la persistencia del Board Game Café.
 */
public class CentralPersistencia
{
 
    public static final String JSON = "JSON";

    // --- Métodos Estáticos ---
    public static PersistenciaCafe getPersistenciaCafe( String tipoArchivo ) throws Exception 
    {
        if ( tipoArchivo.equals( JSON ) )
        {
            return new PersistenciaCafeJson( );
        }
        else
        {
            throw new Exception( "Tipo de archivo inválido: " + tipoArchivo );
        }
    }

    public static PersistenciaOperaciones getPersistenciaOperaciones( String tipoArchivo ) throws Exception
    {
        if ( tipoArchivo.equals( JSON ) )
        {
            return new PersistenciaOperacionesJson( );
        }
        else
        {
            throw new Exception( "Tipo de archivo inválido: " + tipoArchivo );
        }
    }
}
