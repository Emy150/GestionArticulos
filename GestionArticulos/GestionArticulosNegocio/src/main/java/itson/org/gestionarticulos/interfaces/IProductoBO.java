package itson.org.gestionarticulos.interfaces;

import itson.org.gestionarticulos.dtos.NuevoProductoDTO;
import itson.org.gestionarticulos.dtos.ProductoDTO;
import itson.org.gestionarticulos.exceptions.NegocioException;

/**
 *
 * @author emyla
 */
public interface IProductoBO {

    public abstract ProductoDTO registrarProducto(NuevoProductoDTO nuevoDTO) throws NegocioException;

}
