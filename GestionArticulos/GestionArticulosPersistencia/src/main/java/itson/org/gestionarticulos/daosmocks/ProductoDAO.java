package itson.org.gestionarticulos.daosmocks;

import itson.org.gestionarticulos.entidades.Imagen;
import itson.org.gestionarticulos.interfaces.IProductosDAO;
import itson.org.gestionarticulos.entidades.Producto;
import itson.org.gestionarticulos.enums.EstadoProducto;
import itson.org.gestionarticulos.enums.Genero;
import itson.org.gestionarticulos.enums.TipoProducto;
import itson.org.gestionarticulos.exceptions.PersistenciaException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.logging.Logger;

/**
 *
 * @author emyla
 */
public class ProductoDAO implements IProductosDAO{

private static final Logger LOGGER = Logger.getLogger(Producto.class.getName());

    private Integer idProductos = 1;
    private static List<Producto> catalogo;
    private Imagen imagenMock = new Imagen("01", new byte[0]);
    
    public ProductoDAO(){
        // Para inicializar una sola vez el catalogo
        if (catalogo == null) {
            catalogo = new ArrayList<>();
            cargarProductos();
        }
    }
    
    private void cargarProductos(){
        Producto producto1 = new Producto(
                "001", 
                "Breach", 
                "Twenty One Pilots", 
                TipoProducto.CASSETTE,    
                Genero.ROCK, 
                700.00, 
                15, 
                EstadoProducto.DISPONIBLE,
                imagenMock,
                LocalDateTime.now()
        );
        this.catalogo.add(producto1);
        idProductos++;
    }
    
    
    @Override
    public Producto registratNuevoProducto(Producto producto) throws PersistenciaException {
        try{
            idProductos++; // Incrementamos el contador de ids
            String id = String.valueOf(idProductos); // Hacemos conversión de int a String (solo aplicará en los MOCK, porq BO mandará el adaptado)
            producto.setIdProducto("00"+id); //Le setemos un id al producto + un pequeño formato extra
            catalogo.add(producto); // Agregamos el producto al catalogo (lista)
            LOGGER.fine("Producto " + producto.getTitulo() + " registrado exitosamente! :D"); // Mensajuto logger de q todo al cien
            
            return producto; // retornamos el producto que acabamos de agregar 
        } catch(Exception e){
            LOGGER.severe(e.getMessage());
            throw new PersistenciaException("No fue posible registrar el producto " + producto.getTitulo(), e);
        }
    }
    
    @Override
    public Producto modificarProducto(Producto producto) throws PersistenciaException{
        try{
            int indice = -1; // Indice es la posición de nuestra lista
            
            // Primero hay que buscar que el producto que el método recibió si exista dentro del catalogo
            for(int i = 0; i < catalogo.size(); i++){ // recorremos la lista y en cada iteración...
                if (catalogo.get(i).getIdProducto().equals(producto.getIdProducto())){ // Comparamos el id del producto que llego con el
                    // id del producto en el que nos encontremos dentro del recorrido
                    indice = i; // En caso de haber encontrado el producto, indicamos en q posición fue
                    break;
                }
            }
            // Validación... si el producto no existe dentro del arreglo, el indice permanecerá como -1
            if (indice == -1){
                LOGGER.severe("ERROR!");
                throw new PersistenciaException("No se encontró el producto con Id: " + producto.getIdProducto() + "D:");
            }
            // Le indicamos al catalogo que reemplace el producto que está en la posición del indice (1, 3, 100, el q sea)
            // por el producto que el metodo recibió
            catalogo.set(indice, producto); //En teoria está bien esto porq pues la modificación como tal estará en BO
            
            LOGGER.fine("El producto fue modificado con éxito :D!"); // Mensajito de éxito (estoy intentando usar loggers a lo wey)
            return producto; // Retornamos el producto q modificamos (en caso de q todo salga bien)
        } catch(Exception e){
            LOGGER.severe("Error al modificar producto!");
            throw new PersistenciaException("No se pudo modificar el producto con id: "+ producto.getIdProducto(), e);
        }
    }

    @Override
    public Producto eliminarProducto(String idProducto) throws PersistenciaException {
        try{
            // Haremos lo mismo q en modificar, primero hay q checar que el producto se encuentre dentro del catalogo
            int indice = -1;
            for(int i = 0; i < catalogo.size(); i++){
                if(catalogo.get(i).getIdProducto().equals(idProducto)){
                    indice = i;
                    break;
                }
            }
            
            // Si no está, el indice se quedará como -1
            if(indice == -1){
                LOGGER.severe("ERROR!");
                throw new PersistenciaException("El producto que deseas eliminar no existe :c");
            }
                        
            // Si sí lo encontró, entonces:
            Producto productoEliminado = catalogo.get(indice); // Guardamos el producto por si las moscas
            
            catalogo.remove(indice); // Le indicamos que borre el producto que está en esa posición
            
            LOGGER.fine("Producto elimiado con éxito yey :D");
            
            return productoEliminado; // le pedimos q regrese el producto eliminado
        } catch(PersistenciaException e){
            LOGGER.severe(e.getMessage());
            throw new PersistenciaException("No se pudo eliminar el producto :o", e);
        }
    }

    @Override
    public Producto consultarProductoPorId(String idProducto) throws PersistenciaException {
  
        if(idProducto == null){
            LOGGER.severe("ERROR! El id del producto que busca no puede estar vacío");
            throw new PersistenciaException("Verifique que el id");
        }
        
        try{
            for (Producto producto : catalogo){
                if(producto.getIdProducto().equals(idProducto)){
                    return producto;
                }
            }
            LOGGER.severe("ERROR");
            throw new PersistenciaException("No se encontró el producto");
        } catch(PersistenciaException e){
            throw e;
        }
    
    }

    @Override
    public List<Producto> consultarCatalogo() throws PersistenciaException {
        // Simplemente retornamos la lista completa que tenemos en memoria
        // En un DAO real, esto sería un "SELECT * FROM productos"
        return catalogo;
    }

    @Override
    public List<Producto> buscarProductos(String filtro) throws PersistenciaException {
        try {
            List<Producto> resultados = new ArrayList<>();
            // Pasamos el filtro a minúsculas para que la búsqueda no sea sensible a mayúsculas
            String busqueda = filtro.toLowerCase();

            for (Producto p : catalogo) {
                // Checamos si el título o el artista contienen el texto que el usuario escribió
                if (p.getTitulo().toLowerCase().contains(busqueda) || 
                    p.getArtista().toLowerCase().contains(busqueda)) {
                    resultados.add(p);
                }
            }
            return resultados;
        } catch (Exception e) {
            LOGGER.severe("Error al filtrar productos por texto");
            throw new PersistenciaException("Error al buscar productos con el filtro: " + filtro, e);
        }
    }

    @Override
    public List<Producto> buscarProductoPorTipo(TipoProducto tipo) throws PersistenciaException {
        try {
            List<Producto> resultados = new ArrayList<>();
            for (Producto p : catalogo) {
                // Comparamos el enum de la lista con el que nos mandaron (ej. VINILO)
                if (p.getTipo().equals(tipo)) {
                    resultados.add(p);
                }
            }
            return resultados;
        } catch (Exception e) {
            LOGGER.severe("Error al filtrar por tipo de producto");
            throw new PersistenciaException("Error al buscar productos del tipo: " + tipo, e);
        }
    }

    @Override
    public List<Producto> buscarPorEstado(EstadoProducto estado) throws PersistenciaException {
        try {
            List<Producto> resultados = new ArrayList<>();
            for (Producto p : catalogo) {
                // Filtramos por estado (ej. DISPONIBLE, AGOTADO)
                if (p.getEstado().equals(estado)) {
                    resultados.add(p);
                }
            }
            return resultados;
        } catch (Exception e) {
            LOGGER.severe("Error al filtrar por estado");
            throw new PersistenciaException("Error al buscar productos con estado: " + estado, e);
        }
    }

    @Override
    public List<Producto> consultarStockCritico() throws PersistenciaException {
        try {
            List<Producto> criticos = new ArrayList<>();
            // Definimos que stock crítico es cuando quedan menos de 5 unidades
            int limiteCritico = 5; 
            
            for (Producto producto : catalogo) {
                if (producto.getStockInicial() <= limiteCritico) {
                    criticos.add(producto);
                } 
            }
            return criticos;
        } catch (Exception e) {
            LOGGER.severe("Error al consultar stock crítico");
            throw new PersistenciaException("No se pudo obtener la lista de stock crítico", e);
        }
    }
    
    
    
    
    
}
