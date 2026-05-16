package itson.org.gestionarticulos.objetosnegocio;

import itson.org.gestionarticulos.adapters.UsuarioAdapter;
import itson.org.gestionarticulos.dtos.AdministradorDTO;
import itson.org.gestionarticulos.entidades.Administrador;
import itson.org.gestionarticulos.exceptions.NegocioException;
import itson.org.gestionarticulos.fachada.PersistenciaFachada;
import itson.org.gestionarticulos.interfaces.IPersistencia;
import itson.org.gestionarticulos.interfaces.IUsuariosBO;
import java.util.logging.Logger;

/**
 *
 * @author emyla
 */
public class UsuariosBO implements IUsuariosBO {

    private static final Logger LOGGER = Logger.getLogger(UsuariosBO.class.getName());
    
    private final IPersistencia persistencia;
    private final UsuarioAdapter adapter;

    public UsuariosBO() {
        // Usamos nuestro Singleton de la fachada
        this.persistencia = PersistenciaFachada.getInstancia();
        // Usamos un adaptador para los usuarios/admins (similar al de productos)
        this.adapter = new UsuarioAdapter(); 
    }
    
    // Constructor extra por si luego quieres hacerle pruebas unitarias con Mocks ;)
    public UsuariosBO(IPersistencia persistencia, UsuarioAdapter adapter) {
        this.persistencia = persistencia;
        this.adapter = adapter;
    }

    @Override
    public AdministradorDTO autenticarAdmin(String idUsuario, String contrasenia) throws NegocioException {
        
        // 1. Validaciones de negocio: Que no vengan vacíos
        if (idUsuario == null || idUsuario.trim().isEmpty()) {
            LOGGER.warning("BO: Intento de login con ID de usuario vacío");
            throw new NegocioException("El ID de usuario no puede estar vacío");
        }
        if (contrasenia == null || contrasenia.trim().isEmpty()) {
            LOGGER.warning("BO: Intento de login con contraseña vacía");
            throw new NegocioException("La contraseña no puede estar vacía");
        }

        try {
            // 2. Le pedimos a la fachada que verifique las credenciales
            // (Asegúrate de que tu interfaz IPersistencia tenga este método)
            Administrador adminEntidad = persistencia.autenticarAdmin(idUsuario, contrasenia);
            
            // Si la persistencia nos regresa null, significa que no lo encontró o la pass está mal
            if (adminEntidad == null) {
                LOGGER.info("BO: Credenciales incorrectas para el usuario: " + idUsuario);
                throw new NegocioException("Credenciales incorrectas o usuario no encontrado");
            }

            // 3. Si todo salió bien, lo convertimos a DTO y lo regresamos a la pantalla
            LOGGER.fine("BO: Administrador autenticado con éxito :D");
            return adapter.adaptAdministradorToDTO(adminEntidad);

        } catch (NegocioException e) {
            // Si es un error de negocio que nosotros lanzamos arriba (credenciales incorrectas), 
            // lo dejamos pasar tal cual
            throw e; 
        } catch (Exception e) {
            LOGGER.severe("BO: Ocurrió un error inesperado al intentar autenticar al admin");
            throw new NegocioException("Error interno al intentar iniciar sesión", e);
        }
    }
}