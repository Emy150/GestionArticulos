package itson.org.gestionarticulos.interfaces;

import itson.org.gestionarticulos.dtos.AdministradorDTO;
import itson.org.gestionarticulos.exceptions.NegocioException;

/**
 *
 * @author emyla
 */
public interface IUsuariosBO {
    
    public abstract AdministradorDTO autenticarAdmin(String idUsuario, String contrasenia) throws NegocioException;
    
}
