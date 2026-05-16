package itson.org.gestionarticulos.fachada;

import itson.org.gestioarticulos.daos.ProductosDAO;
import itson.org.gestioarticulos.daos.UsuariosDAO;
import itson.org.gestionarticulos.entidades.Administrador;
import itson.org.gestionarticulos.entidades.Producto;
import itson.org.gestionarticulos.exceptions.PersistenciaException;
import itson.org.gestionarticulos.interfaces.IPersistencia;
import itson.org.gestionarticulos.interfaces.IProductosDAO;
import itson.org.gestionarticulos.interfaces.IUsuariosDAO;
import java.util.List;
import java.util.logging.Logger;

/**
 *
 * @author emyla
 */
public class PersistenciaFachada implements IPersistencia {
    
    private static final Logger LOGGER = Logger.getLogger(PersistenciaFachada.class.getName());

    private static PersistenciaFachada instancia;
    
    private final IProductosDAO productosDAO;
    private final IUsuariosDAO usuariosDAO;

    
    private PersistenciaFachada() {
        // Inicializamos el DAO de productos
        this.productosDAO = new ProductosDAO();
        this.usuariosDAO = new UsuariosDAO();
    }
    
    
    public static PersistenciaFachada getInstancia() {
        if (instancia == null) {
            instancia = new PersistenciaFachada();
            LOGGER.fine("Fachada: ¡Creando la única instancia del Singleton! B)");
        }
        return instancia;
    }

    // MÉTODOS DE PRODUCTOS
    @Override
    public Producto registrarProducto(Producto producto) throws PersistenciaException {
        // La fachada nomás le pasa el recado al DAO y listo
        LOGGER.fine("Fachada: Mandando registrar producto al DAO :P");
        return productosDAO.registratNuevoProducto(producto);
    }

    @Override
    public Producto consultarProductoPorId(String idProducto) throws PersistenciaException {
        LOGGER.fine("Fachada: Buscando producto con id: " + idProducto);
        return productosDAO.consultarProductoPorId(idProducto);
    }

    @Override
    public List<Producto> buscarProductos(String filtro) throws PersistenciaException {
        // Le pedimos al DAO que busque lo que coincida con el filtro
        return productosDAO.buscarProductos(filtro);
    }

    @Override
    public Producto modificarProducto(Producto producto) throws PersistenciaException {
        LOGGER.fine("Fachada: Actualizando datos en el DAO");
        return productosDAO.modificarProducto(producto);
    }

    @Override
    public Producto eliminarProducto(String idProducto) throws PersistenciaException {
        LOGGER.fine("Fachada: Avisando al DAO que borre el producto con id " + idProducto);
        return productosDAO.eliminarProducto(idProducto);
    }
    
    // MÉTODOS DE USURIOS
    @Override
    public Administrador autenticarAdmin(String idUsuario, String contrasenia) throws PersistenciaException {
        LOGGER.fine("Fachada: Redirigiendo solicitud de autenticación al DAO");
        // CORREGIDO: Pasamos ambos parámetros y el tipo de retorno ahora coincide (Administrador)
        return usuariosDAO.autenticarAdmin(idUsuario, contrasenia);
    }
}