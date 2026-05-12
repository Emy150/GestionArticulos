package itson.org.gestionarticulos.daosmocks;

import itson.org.gestionarticulos.entidades.Administrador;
import itson.org.gestionarticulos.exceptions.PersistenciaException;
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

    /**
     * Test de obtenerAdminPorId, para ver si encuentra al admin que cargamos por defecto
     */
    @Test
    public void testObtenerAdminPorIdFuncionaOk() throws Exception {
        // SET UP :D
        // El ID que pusiste en el cargarUsuarios() del DAO
        String idExistente = "ADMIN01"; 
        
        // EJECUCIÓN lol
        assertDoesNotThrow(() -> {
            Administrador admin = dao.obtenerAdminPorId(idExistente);
            
            // VERIFICACIÓN
            assertNotNull(admin, "El administrador no debería ser null");
            assertEquals("Emily", admin.getNombre(), "El nombre debería coincidir con el del mock");
            System.out.println("Admin encontrado con éxito: " + admin.getNombre());
        });
    }

    /**
     * Test para validar que truene correctamente si el ID no existe
     */
    @Test
    public void testObtenerAdminPorIdFallaConIdInexistente() {
        // SET UP
        String idFalso = "GHOST_ID";
        
        // EJECUCIÓN Y VERIFICACIÓN (Esperamos que lance la excepción que definiste)
        assertThrows(PersistenciaException.class, () -> {
            dao.obtenerAdminPorId(idFalso);
        }, "Debería lanzar PersistenciaException porque el ID no existe");
        
        System.out.println("La prueba de ID inexistente pasó correctamente :o");
    }

    /**
     * Test de validarContraseniaAdmin con la clave correcta
     */
    @Test
    public void testValidarContraseniaAdminFuncionaOk() throws Exception {
        // SET UP
        String contraCorrecta = "12345";
        
        // EJECUCIÓN
        boolean resultado = dao.validarContraseniaAdmin(contraCorrecta);
        
        // VERIFICACIÓN yey
        assertTrue(resultado, "La contraseña debería ser válida");
        System.out.println("Login simulado exitoso con contraseña correcta!");
    }

    /**
     * Test de validarContraseniaAdmin con una clave que nada que ver
     */
    @Test
    public void testValidarContraseniaAdminFallaConClaveErronea() throws Exception {
        // SET UP
        String contraIncorrecta = "password_incorrecto_:( ";
        
        // EJECUCIÓN
        boolean resultado = dao.validarContraseniaAdmin(contraIncorrecta);
        
        // VERIFICACIÓN
        assertFalse(resultado, "La contraseña NO debería ser válida");
        System.out.println("El sistema rechazó correctamente la contraseña errónea");
    }
}