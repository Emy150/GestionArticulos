package itson.org.gestionarticulos.exceptions;

/**
 *
 * @author emyla
 */
public class PersistenciaException extends Exception{
    
    public PersistenciaException() {
        
    }

    public PersistenciaException(String message) {
        super(message);
    }

    public PersistenciaException(String message, Throwable cause) {
        super(message, cause);
    }
    
}
