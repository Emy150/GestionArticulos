package itson.org.gestionarticulos.adapters.newpackage;

import itson.org.gestionarticulos.dtos.NuevoProductoDTO;
import itson.org.gestionarticulos.dtos.ProductoActualizadoDTO;
import itson.org.gestionarticulos.dtos.ProductoDTO;
import itson.org.gestionarticulos.entidades.Imagen;
import itson.org.gestionarticulos.entidades.Producto;
import itson.org.gestionarticulos.enums.EstadoProducto;
import itson.org.gestionarticulos.enums.Genero;
import itson.org.gestionarticulos.enums.TipoProducto;
import itson.org.gestionarticulos.exceptions.NegocioException;
import java.time.LocalDateTime;
import java.util.logging.Logger;

/**
 *
 * @author emyla
 */
public class ProductoAdapter {
    
    private static final Logger LOGGER = Logger.getLogger(ProductoAdapter.class.getName());
    
    public ProductoDTO adaptProductoToProductoDTO(Producto producto) throws NegocioException{
        
        if(producto == null){
            LOGGER.severe("Error!");
            throw new NegocioException("No fue posible adaptar de Producto a ProductoDTO");
        }
        
        try{
            ProductoDTO productoDTO = new ProductoDTO();
            
            productoDTO.setTitulo(producto.getTitulo());
            productoDTO.setArtista(producto.getArtista());
            productoDTO.setPrecio(producto.getPrecio());
            productoDTO.setStockInicial(producto.getStockInicial());
            
            productoDTO.setImg(producto.getImgProducto().getBytes());
            productoDTO.setGenero(producto.getGenero().name());
            productoDTO.setTipo(producto.getTipo().name());  
            productoDTO.setEstado(producto.getEstado().name());
            
            LOGGER.fine("Yipiii, si se realizó la conversión");
            return productoDTO;
        } catch(Exception e){
            LOGGER.severe("Error!");
            throw new NegocioException("No fue posible adaptar de Producto a ProductoDTO", e);
        }          
    }
    
    public Producto adaptProductoDTOToProducto(ProductoDTO productoDTO) throws NegocioException{
        
        // Validamos que si haya contenido en el ProductoDTO q resibimos
        if(productoDTO == null){
            LOGGER.severe("Error! No se puede continuar");
            throw new NegocioException("El ProductoDTO es nulo");
        }
        // Si todo bien, intentamos la conversión
        try{
            Producto producto = new Producto();
            
            producto.setIdProducto(null);
            producto.setTitulo(productoDTO.getTitulo());
            producto.setArtista(productoDTO.getArtista());
            producto.setPrecio(productoDTO.getPrecio());
            producto.setStockInicial(productoDTO.getStockInicial());
            
            producto.setEstado(EstadoProducto.DISPONIBLE);
            producto.setTipo(TipoProducto.valueOf(productoDTO.getTipo()));
            producto.setGenero(Genero.valueOf(productoDTO.getGenero()));
            
            Imagen img = new Imagen();
            img.setBytes(productoDTO.getImg());
            producto.setImgProducto(img);
            
            LOGGER.fine("Conversión realizada con éxito");
            return producto;
            
        } catch(Exception e){
            LOGGER.severe("ERROR!");
            throw new NegocioException("No fue posible adaptar de ProductoDTO a Producto", e);
        }
    }
        
    public Producto adaptNuevoProductoDTOToProducto(NuevoProductoDTO nuevoDTO) throws NegocioException{
        
        if(nuevoDTO == null){
            LOGGER.severe("Error");
            throw new NegocioException("Nuevo producto DTO está vacío");
        }
        
        try{
            Producto producto = new Producto();
            
            producto.setIdProducto(null);
            producto.setTitulo(nuevoDTO.getTitulo());
            producto.setArtista(nuevoDTO.getArtista());
            producto.setPrecio(nuevoDTO.getPrecio());
            producto.setStockInicial(nuevoDTO.getStockInicial());
            
            producto.setEstado(EstadoProducto.DISPONIBLE);
            producto.setTipo(TipoProducto.valueOf(nuevoDTO.getTipo()));
            producto.setGenero(Genero.valueOf(nuevoDTO.getGenero()));
            
            Imagen img = new Imagen();
            img.setBytes(nuevoDTO.getImagen());
            producto.setImgProducto(img);
            
            producto.setFechaRegistro(LocalDateTime.now());
            
            LOGGER.fine("Conversión de Nuevo Producto DTO a Producto");
            return producto;
        } catch(Exception e){
            LOGGER.severe("Error!");
            throw new NegocioException("No se pudo convertir de Nuevo dto a Producto");
        }
    }
    
    public Producto adaptProductoActualizadoDTOToProducto(ProductoActualizadoDTO actualizadoDTO) throws NegocioException {
    
        // Validamos que el DTO no venga nulo
        if (actualizadoDTO == null) {
            LOGGER.severe("Error! El DTO de producto actualizado es nulo.");
            throw new NegocioException("El ProductoActualizadoDTO no puede ser nulo");
        }

        try {
            Producto producto = new Producto();

            producto.setIdProducto(actualizadoDTO.getIdProducto());
            producto.setTitulo(actualizadoDTO.getTitulo());
            producto.setArtista(actualizadoDTO.getArtista());
            producto.setPrecio(actualizadoDTO.getPrecio());
            producto.setStockInicial(actualizadoDTO.getStockInicial());

            producto.setTipo(TipoProducto.valueOf(actualizadoDTO.getTipo()));
            producto.setGenero(Genero.valueOf(actualizadoDTO.getGenero()));

            producto.setEstado(EstadoProducto.DISPONIBLE);

            if (actualizadoDTO.getImagen() != null) {
                Imagen img = new Imagen();
                img.setBytes(actualizadoDTO.getImagen());
                producto.setImgProducto(img);
            }

            LOGGER.fine("Conversión de ProductoActualizadoDTO a Producto exitosa");
            return producto;

        } catch (IllegalArgumentException e) {
            LOGGER.severe("Error al convertir Enums: " + e.getMessage());
            throw new NegocioException("Error en el formato de tipo o género", e);
        } catch (Exception e) {
            LOGGER.severe("Error inesperado en la conversión!");
            throw new NegocioException("No fue posible adaptar el producto actualizado", e);
        }
    }
    
}
