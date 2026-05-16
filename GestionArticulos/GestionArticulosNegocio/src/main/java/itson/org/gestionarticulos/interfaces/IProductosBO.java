package itson.org.gestionarticulos.interfaces;

import itson.org.gestionarticulos.dtos.NuevoProductoDTO;
import itson.org.gestionarticulos.dtos.ProductoActualizadoDTO;
import itson.org.gestionarticulos.dtos.ProductoDTO;
import itson.org.gestionarticulos.exceptions.NegocioException;
import java.util.List;

/**
 *
 * @author emyla
 */
public interface IProductosBO {

    public abstract ProductoDTO registrarProducto(NuevoProductoDTO nuevoDto) throws NegocioException;
    
    public abstract ProductoDTO modificarProducto(ProductoActualizadoDTO actualizadoDto) throws NegocioException;
    
    public abstract ProductoDTO eliminarProducto(String idProducto) throws NegocioException;
    
    public abstract List<ProductoDTO> buscarProductos(String filtro) throws NegocioException;
    
    public abstract boolean validarStockMinimo(ProductoDTO productoDto) throws NegocioException;
    
    public abstract boolean validarPrecioMinimo(ProductoDTO productoDto) throws NegocioException;

}
