//package itson.org.gestionarticulos.daosmocks;
//
//import itson.org.gestionarticulos.entidades.Administrador;
//import itson.org.gestionarticulos.exceptions.PersistenciaException;
//import itson.org.gestionarticulos.interfaces.IUsuariosDAO;
//import java.util.ArrayList;
//import java.util.List;
//import java.util.logging.Logger;
//
///**
// *
// * @author emyla
// */
//public class UsuariosDAO implements IUsuariosDAO {
//
//    private static final Logger LOGGER = Logger.getLogger(UsuariosDAO.class.getName());
//    
//    // Lista estática para que los admins no se borren 
//    private static List<Administrador> administradores;
//
//    public UsuariosDAO() {
//        // Inicializamos la lista solo si es la primera vez que se usa el DAO
//        if (administradores == null) {
//            administradores = new ArrayList<>();
//            cargarUsuarios();
//        }
//    }
//
//    private void cargarUsuarios() {
//        // Creamos un admin de prueba para poder loguearnos
//        Administrador adminMock = new Administrador(
//                "ADMIN01", 
//                "Emily", 
//                "Lara", 
//                "M", 
//                "emy@ghosttracks.com", 
//                "12345" // Contraseña súper secreta
//        );
//        
//        administradores.add(adminMock);
//        LOGGER.fine("Usuario Administrador cargado correctamente en el MOCK :D");
//    }
//
//    @Override
//    public Administrador obtenerAdminPorId(String idUsuario) throws PersistenciaException {
//        try {
//            // Buscamos en nuestra lista de admins por el id
//            for (Administrador admin : administradores) {
//                if (admin.getIdUsuario().equals(idUsuario)) {
//                    return admin; // Si lo encuentra, lo regresa yey
//                }
//            }
//            
//            // Si termina el ciclo y no hay nada...
//            LOGGER.severe("ERROR! No se encontró el administrador con ID: " + idUsuario);
//            throw new PersistenciaException("Administrador no encontrado");
//            
//        } catch (PersistenciaException e) {
//            throw e;
//        } catch (Exception e) {
//            LOGGER.severe("Ocurrió un error inesperado al buscar el admin");
//            throw new PersistenciaException("Error en la persistencia de usuarios", e);
//        }
//    }
//
//    @Override
//    public boolean validarContraseniaAdmin(String contrasenia) throws PersistenciaException {
//        try {
//            // En un mock simple, checamos si la contraseña coincide con el primer admin de la lista
//            for (Administrador admin : administradores) {
//                if (admin.getContrasenia().equals(contrasenia)) {
//                    LOGGER.fine("Contraseña validada correctamente! Bienvenido :P");
//                    return true;
//                }
//            }
//            
//            LOGGER.severe("Intento de login fallido: Contraseña incorrecta");
//            return false;
//            
//        } catch (Exception e) {
//            LOGGER.severe("Error al validar contraseña");
//            throw new PersistenciaException("Error en validación", e);
//        }
//    }
//}