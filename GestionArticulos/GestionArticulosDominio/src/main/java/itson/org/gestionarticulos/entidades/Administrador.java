package itson.org.gestionarticulos.entidades;

/**
 *
 * @author emyla
 */
public class Administrador extends Usuario{

    public Administrador() {
        
    }

    public Administrador(String idUsuario, String nombre, String apellidoPaterno, String apellidoMaterno, String correo, String contrasenia) {
        super(idUsuario, nombre, apellidoPaterno, apellidoMaterno, correo, contrasenia);
    }
    
    
}
