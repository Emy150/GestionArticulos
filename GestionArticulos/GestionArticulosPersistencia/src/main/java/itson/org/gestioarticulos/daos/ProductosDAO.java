package itson.org.gestioarticulos.daos;

import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Filters;
import com.mongodb.client.result.InsertOneResult;
import com.mongodb.client.result.UpdateResult;
import itson.org.gestionarticulos.conexion.ManejadorConexiones;
import static itson.org.gestionarticulos.conexion.ManejadorConexiones.obtenerCodecs;
import itson.org.gestionarticulos.entidades.Producto;
import itson.org.gestionarticulos.enums.EstadoProducto;
import itson.org.gestionarticulos.enums.TipoProducto;
import itson.org.gestionarticulos.exceptions.PersistenciaException;
import itson.org.gestionarticulos.interfaces.IBaseMongoDAO;
import itson.org.gestionarticulos.interfaces.IProductosDAO;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;
import org.bson.types.ObjectId;

/**
 *
 * @author emyla
 */
public class ProductosDAO implements IProductosDAO, IBaseMongoDAO {

    private static final String COLECCION = "productos";
    
    @Override
    public MongoDatabase obtenerBaseDatos(MongoClient cliente) {
        MongoDatabase ghostTracks = cliente.getDatabase(ManejadorConexiones.BASE_DATOS).withCodecRegistry(obtenerCodecs());
        return ghostTracks;
    }

    @Override
    public MongoCollection<Producto> obtenerColeccion(MongoDatabase baseDatos) {
        MongoCollection<Producto> coleccionProductos = baseDatos.getCollection(COLECCION, Producto.class);
        return coleccionProductos;
    }
    
    @Override
    public Producto registratNuevoProducto(Producto producto) throws PersistenciaException {
        try(MongoClient cliente = ManejadorConexiones.crearConexion()){
            MongoDatabase baseDatos = this.obtenerBaseDatos(cliente);
            MongoCollection<Producto> coleccion = this.obtenerColeccion(baseDatos);
            
            if (producto == null){
                throw new PersistenciaException("Error, el producto está vacío");
            }
            InsertOneResult resultadoInsersion = coleccion.insertOne(producto);
            
            if(!resultadoInsersion.wasAcknowledged()){ 
                throw new PersistenciaException("Error al registrar el producto en la base de datos.");
            }
            
            // Asignamos el id limpio extraído directamente del ObjectId
            producto.setIdProducto(resultadoInsersion.getInsertedId().asObjectId().getValue().toHexString());
            
            return producto;
        }
    }

    @Override
    public Producto modificarProducto(Producto producto) throws PersistenciaException {

        try(MongoClient cliente = ManejadorConexiones.crearConexion()){
            MongoDatabase baseDatos = this.obtenerBaseDatos(cliente);
            MongoCollection<Producto> coleccion = this.obtenerColeccion(baseDatos);
            
            if (producto == null || producto.getIdProducto() == null){
                throw new PersistenciaException("Error, el producto o su ID están vacíos D:");
            }
            
            // Convertimos el String a ObjectId para que Mongo lo entienda
            ObjectId idObj = new ObjectId(producto.getIdProducto());
            
            // Reemplazamos el documento completo
            UpdateResult resultado = coleccion.replaceOne(Filters.eq("_id", idObj), producto);
            
            // Verificamos :0
            if(resultado.getMatchedCount() == 0){
                throw new PersistenciaException("No se encontró el producto a modificar.");
            }
            
            return producto;
        } catch (IllegalArgumentException e) {
            throw new PersistenciaException("El ID del producto no tiene un formato válido.", e);
        }
    }

    @Override
    public Producto eliminarProducto(String idProducto) throws PersistenciaException {

        try(MongoClient cliente = ManejadorConexiones.crearConexion()){
            MongoDatabase baseDatos = this.obtenerBaseDatos(cliente);
            MongoCollection<Producto> coleccion = this.obtenerColeccion(baseDatos);
            
            ObjectId idObj = new ObjectId(idProducto);// Convertimos el id recibido en ObjectId 
            // para que mongo lo interprete
            
            // findOneAndDelete hace la magia de borrar y regresar el objeto borrado en 1 paso
            Producto eliminado = coleccion.findOneAndDelete(Filters.eq("_id", idObj));
            
            // VERIFICACIÓN
            if (eliminado == null) {
                throw new PersistenciaException("No se encontró el producto a eliminar.");
            }
            
            return eliminado;
        } catch (IllegalArgumentException e) {
            throw new PersistenciaException("El ID del producto no tiene un formato válido.", e);
        }
    }

    @Override
    public Producto consultarProductoPorId(String idProducto) throws PersistenciaException {
        try (MongoClient cliente = ManejadorConexiones.crearConexion()) {
            MongoDatabase baseDatos = this.obtenerBaseDatos(cliente);
            MongoCollection<Producto> coleccion = this.obtenerColeccion(baseDatos);
            
            // Convertimos el string a ObjectId
            ObjectId idObj = new ObjectId(idProducto);
            
            // Buscamos el producto
            Producto encontrado = coleccion.find(Filters.eq("_id", idObj)).first();
            
            if (encontrado == null) {
                throw new PersistenciaException("No se encontró el producto");
            }
            
            return encontrado;
        } catch (IllegalArgumentException e) {
            throw new PersistenciaException("El ID del producto no tiene un formato válido.", e);
        }
    }

    @Override
    public List<Producto> consultarCatalogo() throws PersistenciaException {
        try(MongoClient cliente = ManejadorConexiones.crearConexion()){
            MongoDatabase baseDatos = this.obtenerBaseDatos(cliente);
            MongoCollection<Producto> coleccion = this.obtenerColeccion(baseDatos);
            
            // Pasamos los resultados directo a un ArrayList, yey!
            return coleccion.find().into(new ArrayList<>());
        }
    }

    @Override
    public List<Producto> buscarProductos(String filtro) throws PersistenciaException {
        try(MongoClient cliente = ManejadorConexiones.crearConexion()){
            MongoDatabase baseDatos = this.obtenerBaseDatos(cliente);
            MongoCollection<Producto> coleccion = this.obtenerColeccion(baseDatos);
            
            // SET UP del filtro (Expresión regular para ignorar mayúsculas o minúsculas)
            Pattern patron = Pattern.compile(filtro, Pattern.CASE_INSENSITIVE);
            
            // Buscamos que coincida con el Título o el Artista :D
            return coleccion.find(
                    Filters.or(
                            Filters.regex("titulo", patron),
                            Filters.regex("artista", patron)
                    )
            ).into(new ArrayList<>());
        }
    }

    @Override
    public List<Producto> buscarProductoPorTipo(TipoProducto tipo) throws PersistenciaException {
        try(MongoClient cliente = ManejadorConexiones.crearConexion()){
            MongoDatabase baseDatos = this.obtenerBaseDatos(cliente);
            MongoCollection<Producto> coleccion = this.obtenerColeccion(baseDatos);
            
            return coleccion.find(Filters.eq("tipo", tipo)).into(new ArrayList<>());
        }
    }

    @Override
    public List<Producto> buscarPorEstado(EstadoProducto estado) throws PersistenciaException {
        try(MongoClient cliente = ManejadorConexiones.crearConexion()){
            MongoDatabase baseDatos = this.obtenerBaseDatos(cliente);
            MongoCollection<Producto> coleccion = this.obtenerColeccion(baseDatos);
            
            return coleccion.find(Filters.eq("estado", estado)).into(new ArrayList<>());
        }
    }

    @Override
    public List<Producto> consultarStockCritico() throws PersistenciaException {
        try(MongoClient cliente = ManejadorConexiones.crearConexion()){
            MongoDatabase baseDatos = this.obtenerBaseDatos(cliente);
            MongoCollection<Producto> coleccion = this.obtenerColeccion(baseDatos);
            
            // Buscamos los que tengan stock menor (lt - less than) a 5
            return coleccion.find(Filters.lt("stockInicial", 5)).into(new ArrayList<>());
        }
    }

    @Override
    public List<Producto> buscarPorGenero(String idGenero) throws PersistenciaException {
        try(MongoClient cliente = ManejadorConexiones.crearConexion()){
            MongoDatabase baseDatos = this.obtenerBaseDatos(cliente);
            MongoCollection<Producto> coleccion = this.obtenerColeccion(baseDatos);
            
            // Convertimos el string a ObjectId para que coincida con el tipo en Mongo
            ObjectId idObj = new ObjectId(idGenero);
            
            // Filtramos por el campo referenciado
            return coleccion.find(Filters.eq("idGenero", idObj)).into(new ArrayList<>());
            
        } catch (IllegalArgumentException e) {
            throw new PersistenciaException("El ID del género no tiene un formato válido.", e);
        }
    }
}