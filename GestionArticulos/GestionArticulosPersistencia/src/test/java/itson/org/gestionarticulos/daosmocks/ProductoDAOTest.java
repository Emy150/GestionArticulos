package itson.org.gestionarticulos.daosmocks;

import itson.org.gestionarticulos.entidades.Imagen;
import itson.org.gestionarticulos.entidades.Producto;
import itson.org.gestionarticulos.enums.EstadoProducto;
import itson.org.gestionarticulos.enums.Genero;
import itson.org.gestionarticulos.enums.TipoProducto;
import itson.org.gestionarticulos.exceptions.PersistenciaException;
import java.time.LocalDateTime;
import java.util.List;
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
    
    @Test
    public void testConsultarProductoPorIdFuncionaOk() throws Exception {
        // SET UP :D
        // Usaremos el ID "001" que ya viene cargado por defecto en el constructor del DAO
        String idExistente = "001";
        
        // EJECUCIÓN
        assertDoesNotThrow(() -> {
            Producto encontrado = dao.consultarProductoPorId(idExistente);
            
            // VERIFICACIÓN lol
            assertNotNull(encontrado, "El producto debería existir");
            assertEquals("Breach", encontrado.getTitulo(), "El título debería coincidir con el cargado");
            System.out.println("Producto encontrado por ID: " + encontrado);
        });
    }

    @Test
    public void testConsultarCatalogoRegresaListaConDatos() throws Exception {
        // SET UP 
        // No ocupamos registrar nada extra porque el DAO ya carga uno al inicio
        
        // EJECUCIÓN
        List<Producto> lista = dao.consultarCatalogo();
        
        // VERIFICACIÓN
        assertNotNull(lista, "La lista no puede ser null");
        assertFalse(lista.isEmpty(), "La lista debería tener al menos el producto precargado");
        System.out.println("Tamaño del catálogo consultado: " + lista.size());
    }

    @Test
    public void testBuscarProductosPorFiltroFuncionaOk() throws Exception {
        // SET UP
        // Buscaremos "Pilots" que es parte del artista "Twenty One Pilots"
        String filtro = "Pilots";
        
        // EJECUCIÓN
        List<Producto> filtrados = dao.buscarProductos(filtro);
        
        // VERIFICACIÓN
        assertFalse(filtrados.isEmpty(), "Debería encontrar productos con el filtro: " + filtro);
        // Checamos que el artista realmente contenga el filtro (ignorando mayúsculas)
        assertTrue(filtrados.get(0).getArtista().toLowerCase().contains(filtro.toLowerCase()));
        System.out.println("Resultados de búsqueda con '" + filtro + "': " + filtrados.size());
    }

    @Test
    public void testBuscarProductoPorTipoFuncionaOk() throws Exception {
        // SET UP
        // El producto precargado "001" es de tipo CASSETTE
        TipoProducto tipoBusqueda = TipoProducto.CASSETTE;
        
        // EJECUCIÓN
        List<Producto> resultados = dao.buscarProductoPorTipo(tipoBusqueda);
        
        // VERIFICACIÓN
        assertFalse(resultados.isEmpty(), "Debería haber al menos un cassette");
        assertEquals(TipoProducto.CASSETTE, resultados.get(0).getTipo());
        System.out.println("Productos de tipo " + tipoBusqueda + " encontrados: " + resultados.size());
    }

    @Test
    public void testConsultarStockCriticoFiltraCorrectamente() throws Exception {
        // SET UP
        // Registramos un producto que tenga 3 de stock (es menor a 5, por lo tanto es crítico)
        Producto critico = new Producto(null, "Rare Album", "Unknown", 
                                        TipoProducto.VINILO, Genero.POP, 2000.0, 3, 
                                        EstadoProducto.DISPONIBLE, imagenTest, LocalDateTime.now());
        dao.registratNuevoProducto(critico);
        
        // EJECUCIÓN
        List<Producto> listaCritica = dao.consultarStockCritico();
        
        // VERIFICACIÓN
        // Buscamos si el producto "Rare Album" está en la lista de críticos
        boolean fueDetectado = false;
        for(Producto p : listaCritica) {
            if(p.getTitulo().equals("Rare Album")) {
                fueDetectado = true;
                break;
            }
        }
        
        assertTrue(fueDetectado, "El producto con stock 3 debió ser detectado como crítico");
        System.out.println("Productos en stock crítico encontrados: " + listaCritica.size());
    }

    @Test
    public void testConsultarProductoPorIdLanzaExcepcionSiNoExiste() {
        // SET UP
        String idFalso = "999";
        
        // EJECUCIÓN Y VERIFICACIÓN
        // Aquí probamos que falle cuando debe fallar
        PersistenciaException ex = assertThrows(PersistenciaException.class, () -> {
            dao.consultarProductoPorId(idFalso);
        });
        
        assertEquals("No se encontró el producto", ex.getMessage());
        System.out.println("La excepción se lanzó correctamente para el ID inexistente.");
    }
    
}
