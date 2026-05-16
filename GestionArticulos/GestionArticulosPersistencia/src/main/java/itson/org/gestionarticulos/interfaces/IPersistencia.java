package itson.org.gestionarticulos.interfaces;

import itson.org.gestionarticulos.entidades.Administrador;
import itson.org.gestionarticulos.entidades.Producto;
import itson.org.gestionarticulos.exceptions.PersistenciaException;
import java.util.List;

/**
 *
 * @author emyla
 */
public interface IPersistencia {
    
    // Métodos de los productos
    public Producto registrarProducto(Producto producto) throws PersistenciaException;
    
    public Producto consultarProductoPorId(String idProducto) throws PersistenciaException;
    
    public List<Producto> buscarProductos(String filtro) throws PersistenciaException;
    
    public Producto modificarProducto(Producto producto) throws PersistenciaException;
    
    public Producto eliminarProducto(String idProducto) throws PersistenciaException;
    
    // Métodos de usuario
    public Administrador autenticarAdmin(String idUsuario, String contrasenia) throws PersistenciaException;
    
}
