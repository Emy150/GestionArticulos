package itson.org.gestionarticulos.pruebasdaos;

import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Filters;
import itson.org.gestionarticulos.conexion.ManejadorConexiones;
import itson.org.gestioarticulos.daos.UsuariosDAO;
import itson.org.gestionarticulos.entidades.Administrador;
import itson.org.gestionarticulos.exceptions.PersistenciaException;
import org.bson.types.ObjectId;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

/**
 *
 * @author emyla
 */
public class UsuariosDAOTest {
    
    private UsuariosDAO dao;

    @BeforeEach
    public void setUp() {
        // Inicializamos el DAO antes de cada prueba
        this.dao = new UsuariosDAO();
    }

    private void insertarAdminDePrueba(Administrador admin) {
        try (MongoClient cliente = ManejadorConexiones.crearConexion()) {
            MongoDatabase baseDatos = cliente.getDatabase(ManejadorConexiones.BASE_DATOS)
                                             .withCodecRegistry(ManejadorConexiones.obtenerCodecs());
            MongoCollection<Administrador> coleccion = baseDatos.getCollection("usuarios", Administrador.class);
            coleccion.insertOne(admin);
        }
    }

    private void eliminarAdminDePrueba(String idUsuario) {
        try (MongoClient cliente = ManejadorConexiones.crearConexion()) {
            MongoDatabase baseDatos = cliente.getDatabase(ManejadorConexiones.BASE_DATOS)
                                             .withCodecRegistry(ManejadorConexiones.obtenerCodecs());
            MongoCollection<Administrador> coleccion = baseDatos.getCollection("usuarios", Administrador.class);
            coleccion.deleteOne(Filters.eq("_id", new ObjectId(idUsuario)));
        }
    }

    /**
     * Test de obtenerAdminPorId, insertando un admin real y buscándolo
     */
    @Test
    public void testObtenerAdminPorIdFuncionaOk() throws Exception {
        // SET UP :D
        String idGenerado = new ObjectId().toHexString(); // Generamos un ID válido de Mongo
        Administrador adminPrueba = new Administrador(idGenerado, "Emily", "Lara", "M", "emy@ghosttracks.com", "12345");
        
        // Insertamos directamente en BD para la prueba
        this.insertarAdminDePrueba(adminPrueba);
        
        // EJECUCIÓN lol
        assertDoesNotThrow(() -> {
            try {
                Administrador admin = dao.obtenerAdminPorId(idGenerado);
                
                // VERIFICACIÓN
                assertNotNull(admin, "El administrador no debería ser null");
                assertEquals("Emily", admin.getNombre(), "El nombre debería coincidir con el insertado en la BD");
                System.out.println("Admin encontrado en BD con éxito: " + admin.getNombre());
            } finally {
                this.eliminarAdminDePrueba(idGenerado);
            }
        });
    }

    /**
     * Test para validar que truene correctamente si el ID tiene formato válido pero no existe
     */
    @Test
    public void testObtenerAdminPorIdFallaConIdInexistente() {
        // SET UP (Generamos un ObjectId falso que NO vamos a insertar)
        String idFalsoPeroValido = new ObjectId().toHexString();
        
        // EJECUCIÓN Y VERIFICACIÓN
        PersistenciaException ex = assertThrows(PersistenciaException.class, () -> {
            dao.obtenerAdminPorId(idFalsoPeroValido);
        }, "Debería lanzar PersistenciaException porque el ID no existe en la BD");
        
        assertEquals("Administrador no encontrado", ex.getMessage());
        System.out.println("La prueba de ID inexistente pasó correctamente :o");
    }

    /**
     * Test para validar que truene correctamente si le pasamos basura en lugar de un ID hexadecimal
     */
    @Test
    public void testObtenerAdminPorIdFallaConIdFormatoInvalido() {
        // SET UP
        String idBasura = "GHOST_ID_QUE_NO_ES_HEXADECIMAL";
        
        // EJECUCIÓN Y VERIFICACIÓN
        PersistenciaException ex = assertThrows(PersistenciaException.class, () -> {
            dao.obtenerAdminPorId(idBasura);
        }, "Debería lanzar PersistenciaException por formato inválido");
        
        assertTrue(ex.getMessage().contains("formato válido"));
        System.out.println("La prueba de formato de ID inválido pasó correctamente!");
    }

    /**
     * Test de autenticarAdmin con el correo y la clave correctas
     */
    @Test
    public void testAutenticarAdminFuncionaOk() throws Exception {
        // SET UP
        String idGenerado = new ObjectId().toHexString();
        String correoCorrecto = "test@test.com";
        String contraCorrecta = "super_secreta_777";
        Administrador adminPrueba = new Administrador(idGenerado, "AdminTest", "ApePat", "ApeMat", correoCorrecto, contraCorrecta);
        
        this.insertarAdminDePrueba(adminPrueba);
        
        // EJECUCIÓN lol
        assertDoesNotThrow(() -> {
            try {
                Administrador resultado = dao.autenticarAdmin(correoCorrecto, contraCorrecta);
                
                // VERIFICACIÓN yey
                assertNotNull(resultado, "El administrador NO debe ser nulo porque las credenciales son correctas");
                assertEquals(correoCorrecto, resultado.getCorreo(), "El correo del admin devuelto debe coincidir");
                System.out.println("Login contra BD simulado exitoso con credenciales correctas!");
            } finally {
                this.eliminarAdminDePrueba(idGenerado);
            }
        });
    }

    /**
     * Test de autenticarAdmin con una clave que nada que ver
     */
    @Test
    public void testAutenticarAdminFallaConClaveErronea() throws Exception {
        // SET UP
        String idGenerado = new ObjectId().toHexString();
        String correoReal = "fallo@test.com";
        Administrador adminPrueba = new Administrador(idGenerado, "AdminFallo", "A", "B", correoReal, "contra_real");
        this.insertarAdminDePrueba(adminPrueba);
        
        String contraIncorrecta = "password_incorrecto_:( ";
        
        // EJECUCIÓN
        assertDoesNotThrow(() -> {
            try {
                Administrador resultado = dao.autenticarAdmin(correoReal, contraIncorrecta);
                
                // VERIFICACIÓN
                assertNull(resultado, "El resultado DEBE ser nulo porque la contraseña es incorrecta");
                System.out.println("El sistema rechazó correctamente la contraseña errónea consultando la BD");
            } finally {
                this.eliminarAdminDePrueba(idGenerado);
            }
        });
    }
}