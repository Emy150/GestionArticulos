package itson.org.gestionarticulos.interfaces;

import itson.org.gestionarticulos.dtos.AdministradorDTO;
import itson.org.gestionarticulos.dtos.NuevoProductoDTO;
import itson.org.gestionarticulos.dtos.ProductoActualizadoDTO;
import itson.org.gestionarticulos.dtos.ProductoDTO;
import itson.org.gestionarticulos.exceptions.NegocioException;
import java.util.List;

/**
 *
 * @author emyla
 */
public interface IGestionArticulos {
    
    // Productos
    ProductoDTO registrarProducto(NuevoProductoDTO nuevoDto) throws NegocioException;
    
    ProductoDTO modificarProducto(ProductoActualizadoDTO actualizadoDto) throws NegocioException;
    
    ProductoDTO eliminarProducto(String idProducto) throws NegocioException;
    
    List<ProductoDTO> consultarCatalogo(String filtro) throws NegocioException;
    
    // Usuarios
    AdministradorDTO autenticarAdmin(String correo, String contrasenia) throws NegocioException;
}
