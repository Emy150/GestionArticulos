package itson.org.gestionarticulos.daosmocks;

import itson.org.gestionarticulos.entidades.Imagen;
import itson.org.gestionarticulos.entidades.Producto;
import itson.org.gestionarticulos.enums.EstadoProducto;
import itson.org.gestionarticulos.enums.Genero;
import itson.org.gestionarticulos.enums.TipoProducto;
import java.time.LocalDateTime;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

/**
 *
 * @author emyla
 */
public class ProductoDAOTest {
    
    private ProductoDAO dao;
    private Imagen imagenTest = new Imagen("02", new byte[0]);
    
    public ProductoDAOTest() {
    
    }
        
    @BeforeEach
    public void setUp() {
        this.dao = new ProductoDAO();
    }
    
    /**
     * Test of registratNuevoProducto method, of class ProductoDAO.
     */
    @Test
    public void testRegistratNuevoProductoFuncionaOk() throws Exception {
        // SET UP :0
        Producto producto = new Producto(
                null, 
                "Americana", 
                "The Offspring", 
                TipoProducto.VINILO,    
                Genero.ROCK, 
                1200.00, 
                10, 
                EstadoProducto.DISPONIBLE,
                imagenTest,
                LocalDateTime.now()
        );
        
        // EJECUCIÓN lol
        assertDoesNotThrow(() -> {
            Producto productoRegistrado = dao.registratNuevoProducto(producto);
            System.out.println("PRODUCTO REGISTRADO EN TEST");
            System.out.println(productoRegistrado); 
        
            assertNotNull(productoRegistrado.getIdProducto());
            assertEquals(producto.getTitulo(), productoRegistrado.getTitulo());
        });
        
    }
    
    @Test
    public void testModificarProductoFuncionaOk() throws Exception{
        // SET UP
        // Primero registramos uno para que exista en el catalogo
        Producto nuevo = new Producto(
                null, 
                "Scaled and Icy", 
                "Twenty One Pilots", 
                TipoProducto.CD, 
                Genero.ROCK, 
                850.00, 
                5, 
                EstadoProducto.DISPONIBLE, 
                imagenTest, 
                LocalDateTime.now()
        );

        Producto registrado = dao.registratNuevoProducto(nuevo);
        String idAsignado = registrado.getIdProducto();

        // Ahora si modificamos
        // Creamos el objeto con el MISMO ID pero datos diferentes
        // Cambiaremos el título y el precio
        registrado.setTitulo("Clancy"); 
        registrado.setPrecio(950.00);

        // EJECUCIÓN
        assertDoesNotThrow(() -> {
            Producto modificado = dao.modificarProducto(registrado);

            //VERIFICACIÓN
            assertEquals("Clancy", modificado.getTitulo(), "El título debió actualizarse");
            assertEquals(950.00, modificado.getPrecio(), "El precio debió actualizarse");

            // yeeey
            System.out.println("Producto después de modificar: " + modificado);
        });
    }
    
    @Test
    public void testEliminarProductoFuncionaOk() throws Exception {
        //SET UP
        // Registramos uno para borrarlo
        Producto p = new Producto(
                null, 
                "Blurryface", 
                "Twenty One Pilots", 
                TipoProducto.VINILO, 
                Genero.ROCK, 
                500.0, 
                10, 
                EstadoProducto.DISPONIBLE, 
                imagenTest, 
                LocalDateTime.now()
        );

        Producto registrado = dao.registratNuevoProducto(p);
        String idABorrar = registrado.getIdProducto();

        // EJECUCIÓN
        assertDoesNotThrow(() -> {
            Producto eliminado = dao.eliminarProducto(idABorrar);

            //VERIFICACIÓN
            assertNotNull(eliminado, "El producto eliminado no debería ser null");
            assertEquals(idABorrar, eliminado.getIdProducto(), "El Id del eliminado debe coincidir");
            System.out.println("Se eliminó correctamente: " + eliminado.getTitulo());
        });
    }
    
}
