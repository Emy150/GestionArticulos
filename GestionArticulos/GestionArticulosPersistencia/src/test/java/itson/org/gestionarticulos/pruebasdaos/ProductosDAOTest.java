package itson.org.gestionarticulos.pruebasdaos;

import itson.org.gestioarticulos.daos.ProductosDAO;
import itson.org.gestionarticulos.entidades.Imagen;
import itson.org.gestionarticulos.entidades.Producto;
import itson.org.gestionarticulos.enums.EstadoProducto;
import itson.org.gestionarticulos.enums.TipoProducto;
import itson.org.gestionarticulos.exceptions.PersistenciaException;
import java.time.LocalDateTime;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import org.bson.types.ObjectId;

/**
 *
 * @author emyla
 */
public class ProductosDAOTest {
    
    private ProductosDAO dao;
    private Imagen imagenTest = new Imagen("02", new byte[0]);
    // Generamos un ID falso de género para poder usarlo en las pruebas de inserción
    private String idGeneroTest = new ObjectId().toHexString();
    
    public ProductosDAOTest() {
    
    }
        
    @BeforeEach
    public void setUp() {
        this.dao = new ProductosDAO(); 
    }
    
    @Test
    public void testRegistratNuevoProductoFuncionaOk() {
        // SET UP :0
        Producto producto = new Producto(
                null, 
                "Americana", 
                "The Offspring", 
                TipoProducto.VINILO,    
                idGeneroTest, // <-- Usamos el string del ObjectId referenciado
                1200.00, 
                10, 
                EstadoProducto.DISPONIBLE,
                imagenTest,
                LocalDateTime.now()
        );
        
        // EJECUCIÓN lol
        assertDoesNotThrow(() -> {
            Producto productoRegistrado = this.dao.registratNuevoProducto(producto);
            
            try {
                System.out.println("PRODUCTO REGISTRADO EN TEST EN BD REAL: " + productoRegistrado.getTitulo());
            
                // VERIFICACIÓN
                assertNotNull(productoRegistrado.getIdProducto(), "El ID no debería ser nulo tras guardarse en Mongo");
                assertEquals(producto.getTitulo(), productoRegistrado.getTitulo());
            } finally {
                this.dao.eliminarProducto(productoRegistrado.getIdProducto());
            }
        }, "No debería lanzar PersistenciaException");
    }

    @Test
    public void testModificarProductoFuncionaOk() {
        // SET UP :0
        Producto nuevo = new Producto(null, "Scaled and Icy", "Twenty One Pilots", TipoProducto.CD, idGeneroTest, 850.00, 5, EstadoProducto.DISPONIBLE, imagenTest, LocalDateTime.now());

        assertDoesNotThrow(() -> {
            Producto registrado = this.dao.registratNuevoProducto(nuevo);
            
            try {
                // Cambiamos los datos
                registrado.setTitulo("Clancy"); 
                registrado.setPrecio(950.00);

                Producto modificado = this.dao.modificarProducto(registrado);

                // VERIFICACIÓN :D
                assertEquals("Clancy", modificado.getTitulo(), "El título debió actualizarse");
                assertEquals(950.00, modificado.getPrecio(), "El precio debió actualizarse");
                System.out.println("Producto después de modificar: " + modificado.getTitulo());
            } finally {
                this.dao.eliminarProducto(registrado.getIdProducto());
            }
        });
    }

    @Test
    public void testEliminarProductoFuncionaOk() {
        // SET UP :0
        Producto p = new Producto(null, "Blurryface", "Twenty One Pilots", TipoProducto.VINILO, idGeneroTest, 500.0, 10, EstadoProducto.DISPONIBLE, imagenTest, LocalDateTime.now());

        assertDoesNotThrow(() -> {
            Producto registrado = this.dao.registratNuevoProducto(p);
            String idABorrar = registrado.getIdProducto();

            try {
                Producto eliminado = this.dao.eliminarProducto(idABorrar);

                // VERIFICACIÓN :D
                assertNotNull(eliminado, "El producto eliminado no debería ser null");
                assertEquals(idABorrar, eliminado.getIdProducto(), "El Id del eliminado debe coincidir");
                System.out.println("Se eliminó correctamente: " + eliminado.getTitulo());
                
                assertThrows(PersistenciaException.class, () -> {
                    this.dao.consultarProductoPorId(idABorrar);
                });
            } finally {
                try {
                    this.dao.eliminarProducto(idABorrar);
                } catch (Exception e) {
                    
                }
            }
        });
    }

    @Test
    public void testConsultarProductoPorIdFuncionaOk() {
        // SET UP :0
        Producto p = new Producto(null, "Breach", "Wallflowers", TipoProducto.CASSETTE, idGeneroTest, 150.0, 5, EstadoProducto.DISPONIBLE, imagenTest, LocalDateTime.now());
        
        assertDoesNotThrow(() -> {
            Producto registrado = this.dao.registratNuevoProducto(p);
            String idRealGenerado = registrado.getIdProducto(); 
            
            try {
                Producto encontrado = this.dao.consultarProductoPorId(idRealGenerado);
                
                // VERIFICACIÓN :D
                assertNotNull(encontrado, "El producto debería existir en la BD");
                assertEquals("Breach", encontrado.getTitulo());
                System.out.println("Producto encontrado por ID: " + encontrado.getTitulo());
            } finally {
                // TEAR DOWN BLINDADO
                this.dao.eliminarProducto(idRealGenerado);
            }
        });
    }

    @Test
    public void testConsultarProductoPorIdLanzaExcepcionSiNoExiste() {
        // SET UP :0
        // Generamos un ObjectId falso pero con formato válido para Mongo
        String idFalso = new ObjectId().toHexString(); 
        
        // EJECUCIÓN Y VERIFICACIÓN
        PersistenciaException ex = assertThrows(PersistenciaException.class, () -> {
            this.dao.consultarProductoPorId(idFalso);
        });
        
        assertEquals("No se encontró el producto", ex.getMessage());
        System.out.println("La excepción se lanzó correctamente para ID inexistente.");
        
    }

    @Test
    public void testConsultarCatalogoRegresaLista() {
        // SET UP :0
        Producto p = new Producto(null, "Toxicity", "System of a Down", TipoProducto.CD, idGeneroTest, 250.0, 15, EstadoProducto.DISPONIBLE, imagenTest, LocalDateTime.now());
        
        assertDoesNotThrow(() -> {
            Producto registrado = this.dao.registratNuevoProducto(p);
            
            try {
                List<Producto> lista = this.dao.consultarCatalogo();
                
                assertNotNull(lista, "La lista no puede ser null");
                assertFalse(lista.isEmpty(), "La lista debería tener al menos el producto que acabamos de registrar");
                System.out.println("Tamaño del catálogo: " + lista.size());
            } finally {
                this.dao.eliminarProducto(registrado.getIdProducto());
            }
        });
    }

    @Test
    public void testBuscarProductosPorFiltroFuncionaOk() {
        // SET UP :0
        Producto p = new Producto(null, "The Black Parade", "My Chemical Romance", TipoProducto.VINILO, idGeneroTest, 800.0, 4, EstadoProducto.DISPONIBLE, imagenTest, LocalDateTime.now());
        String filtro = "Chemical";
        
        assertDoesNotThrow(() -> {
            Producto registrado = this.dao.registratNuevoProducto(p);
            
            try {
                List<Producto> filtrados = this.dao.buscarProductos(filtro);
                
                // VERIFICACIÓN :D
                assertFalse(filtrados.isEmpty(), "Debería encontrar productos con el filtro: " + filtro);
                assertTrue(filtrados.get(0).getArtista().toLowerCase().contains(filtro.toLowerCase()));
                System.out.println("Búsqueda exitosa con filtro '" + filtro + "'");
            } finally {
                this.dao.eliminarProducto(registrado.getIdProducto());
            }
        });
    }

    @Test
    public void testBuscarProductoPorTipoFuncionaOk() {
        // SET UP :0
        Producto p = new Producto(null, "Master of Puppets", "Metallica", TipoProducto.CD, idGeneroTest, 400.0, 20, EstadoProducto.DISPONIBLE, imagenTest, LocalDateTime.now());
        
        assertDoesNotThrow(() -> {
            Producto registrado = this.dao.registratNuevoProducto(p);
            
            try {
                // Buscamos específicamente los CDs
                List<Producto> resultados = this.dao.buscarProductoPorTipo(TipoProducto.CD);
                
                // VERIFICACIÓN :D
                assertFalse(resultados.isEmpty(), "La lista de resultados no debería estar vacía");
                
                // Verificamos que el producto encontrado realmente sea del tipo que pedimos
                assertEquals(TipoProducto.CD, resultados.get(0).getTipo());
                System.out.println("Se encontraron artículos de tipo CD");
            } finally {
                this.dao.eliminarProducto(registrado.getIdProducto());
            }
        });
    }

    @Test
    public void testBuscarPorEstadoFuncionaOk() {
        // SET UP :0
        Producto p = new Producto(null, "Rare Album", "Unknown", TipoProducto.VINILO, idGeneroTest, 2000.0, 0, EstadoProducto.AGOTADO, imagenTest, LocalDateTime.now());
        
        assertDoesNotThrow(() -> {
            Producto registrado = this.dao.registratNuevoProducto(p);
            
            try {
                List<Producto> resultados = this.dao.buscarPorEstado(EstadoProducto.AGOTADO);
                
                // VERIFICACIÓN :D
                assertFalse(resultados.isEmpty());
                assertEquals(EstadoProducto.AGOTADO, resultados.get(0).getEstado());
                System.out.println("Se encontraron artículos AGOTADOS");
            } finally {
                this.dao.eliminarProducto(registrado.getIdProducto());
            }
        });
    }

    @Test
    public void testConsultarStockCriticoFiltraCorrectamente() {
        // SET UP
        // Registramos un producto con stock 2 (menor a 5, por lo tanto crítico)
        Producto p = new Producto(null, "Edicion Limitada", "Artista Top", TipoProducto.CD, idGeneroTest, 1500.0, 2, EstadoProducto.DISPONIBLE, imagenTest, LocalDateTime.now());
        
        assertDoesNotThrow(() -> {
            Producto registrado = this.dao.registratNuevoProducto(p);
            
            try {
                List<Producto> listaCritica = this.dao.consultarStockCritico();
                
                // VERIFICACIÓN :D
                boolean fueDetectado = false;
                for(Producto prod : listaCritica) {
                    if(prod.getIdProducto().equals(registrado.getIdProducto())) {
                        fueDetectado = true;
                        break;
                    }
                }
                
                assertTrue(fueDetectado, "El producto con stock 2 debió ser detectado como crítico");
                System.out.println("Producto en stock crítico detectado exitosamente.");
            } finally {
                this.dao.eliminarProducto(registrado.getIdProducto());
            }
        });
    }

    @Test
    public void testBuscarPorGeneroFuncionaOk() {
        // SET UP :0
        // Generamos un ID específico y único para esta prueba
        String idGeneroUnico = new ObjectId().toHexString();
        Producto p = new Producto(null, "Album por Genero", "Artista Prueba", TipoProducto.VINILO, idGeneroUnico, 600.0, 10, EstadoProducto.DISPONIBLE, imagenTest, LocalDateTime.now());
       
        assertDoesNotThrow(() -> {
            Producto registrado = this.dao.registratNuevoProducto(p);
            
            try {
                List<Producto> filtrados = this.dao.buscarPorGenero(idGeneroUnico);
                
                // VERIFICACIÓN :D
                assertFalse(filtrados.isEmpty(), "Debería encontrar productos con el idGenero: " + idGeneroUnico);
                assertEquals(idGeneroUnico, filtrados.get(0).getIdGenero(), "El ID del género debe coincidir con el buscado");
                System.out.println("Búsqueda exitosa por referencia manual (idGenero).");
            } finally {
                this.dao.eliminarProducto(registrado.getIdProducto());
            }
        });
    }
}