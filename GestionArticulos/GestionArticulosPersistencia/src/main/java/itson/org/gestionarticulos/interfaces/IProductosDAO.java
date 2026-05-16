package itson.org.gestionarticulos.interfaces;

import itson.org.gestionarticulos.entidades.Producto;
import itson.org.gestionarticulos.enums.EstadoProducto;
import itson.org.gestionarticulos.enums.TipoProducto;
import itson.org.gestionarticulos.exceptions.PersistenciaException;
import java.util.List;

/**
 *
 * @author emyla
 */
public interface IProductosDAO {

    public abstract Producto registratNuevoProducto(Producto producto) throws PersistenciaException;
    
    public abstract Producto modificarProducto(Producto producto) throws PersistenciaException;
    
    public abstract Producto eliminarProducto(String idProducto) throws PersistenciaException;
    
    public abstract Producto consultarProductoPorId(String idProducto) throws PersistenciaException;
    
    public abstract List<Producto> consultarCatalogo() throws PersistenciaException;
    
    public abstract List<Producto> buscarProductos(String filtro) throws PersistenciaException;
    
    public abstract List<Producto> buscarProductoPorTipo(TipoProducto tipo) throws PersistenciaException;
    
    public abstract List<Producto> buscarPorEstado(EstadoProducto estado) throws PersistenciaException;
    
    public abstract List<Producto> buscarPorGenero(String idGenero) throws PersistenciaException;
    
    public abstract List<Producto> consultarStockCritico() throws PersistenciaException;
}
