package itson.org.gestionarticulos.interfaces;

import itson.org.gestionarticulos.entidades.Administrador;
import itson.org.gestionarticulos.exceptions.PersistenciaException;

/**
 *
 * @author emyla
 */
public interface IUsuariosDAO {
    
    public abstract Administrador obtenerAdminPorId(String idUsuario) throws PersistenciaException;
    
    public abstract boolean validarContraseniaAdmin(String contraseña) throws PersistenciaException;
}
