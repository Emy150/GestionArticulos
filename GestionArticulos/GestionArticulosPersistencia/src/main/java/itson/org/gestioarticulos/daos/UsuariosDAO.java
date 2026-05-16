package itson.org.gestioarticulos.daos;

import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Filters;
import itson.org.gestionarticulos.conexion.ManejadorConexiones;
import static itson.org.gestionarticulos.conexion.ManejadorConexiones.obtenerCodecs;
import itson.org.gestionarticulos.entidades.Administrador;
import itson.org.gestionarticulos.exceptions.PersistenciaException;
import itson.org.gestionarticulos.interfaces.IBaseMongoDAO;
import itson.org.gestionarticulos.interfaces.IUsuariosDAO;

import java.util.logging.Logger;
import org.bson.types.ObjectId;

/**
 *
 * @author emyla
 */
public class UsuariosDAO implements IUsuariosDAO, IBaseMongoDAO {

    private static final Logger LOGGER = Logger.getLogger(UsuariosDAO.class.getName());
    private static final String COLECCION = "usuarios";

    @Override
    public MongoDatabase obtenerBaseDatos(MongoClient cliente) {
        return cliente.getDatabase(ManejadorConexiones.BASE_DATOS).withCodecRegistry(obtenerCodecs());
    }

    @Override
    public MongoCollection<Administrador> obtenerColeccion(MongoDatabase baseDatos) {
        return baseDatos.getCollection(COLECCION, Administrador.class);
    }

    @Override
    public Administrador obtenerAdminPorId(String idUsuario) throws PersistenciaException {
        try (MongoClient cliente = ManejadorConexiones.crearConexion()) {
            MongoDatabase baseDatos = this.obtenerBaseDatos(cliente);
            MongoCollection<Administrador> coleccion = this.obtenerColeccion(baseDatos);

            ObjectId idObj = new ObjectId(idUsuario);
            Administrador admin = coleccion.find(Filters.eq("_id", idObj)).first();

            if (admin == null) {
                LOGGER.severe("ERROR! No se encontró el administrador con ID: " + idUsuario);
                throw new PersistenciaException("Administrador no encontrado");
            }
            return admin;
            
        } catch (IllegalArgumentException e) {
            LOGGER.severe("El ID proporcionado no es un ObjectId válido: " + idUsuario);
            throw new PersistenciaException("El ID del administrador no tiene un formato válido.", e);
            
        } catch (PersistenciaException e) {
            throw e; 
            
        } catch (Exception e) {
            LOGGER.severe("Ocurrió un error inesperado al buscar el admin");
            throw new PersistenciaException("Error en la persistencia de usuarios", e);
        }
    }

    @Override
    public Administrador autenticarAdmin(String correo, String contrasenia) throws PersistenciaException {
        try (MongoClient cliente = ManejadorConexiones.crearConexion()) {
            MongoDatabase baseDatos = this.obtenerBaseDatos(cliente);
            MongoCollection<Administrador> coleccion = this.obtenerColeccion(baseDatos);

            // Buscamos un documento donde coincida el correo Y la contraseña
            Administrador adminEncontrado = coleccion.find(
                Filters.and(
                    Filters.eq("correo", correo),
                    Filters.eq("contrasenia", contrasenia)
                )
            ).first();

            if (adminEncontrado != null) {
                LOGGER.fine("DAO: ¡Credenciales validadas exitosamente para " + correo + "! :P");
                return adminEncontrado;
            } else {
                LOGGER.warning("DAO: Intento de login fallido para " + correo);
                return null; // Retornamos null para que el BO sepa que no hubo match
            }

        } catch (Exception e) {
            LOGGER.severe("DAO: Error al intentar autenticar al usuario " + correo);
            throw new PersistenciaException("Error en el proceso de autenticación en la BD", e);
        }
    }
}