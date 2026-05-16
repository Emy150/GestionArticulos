package itson.org.gestionarticulos.conexion;

import static com.mongodb.MongoClientSettings.getDefaultCodecRegistry;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import org.bson.codecs.configuration.CodecProvider;
import static org.bson.codecs.configuration.CodecRegistries.fromProviders;
import static org.bson.codecs.configuration.CodecRegistries.fromRegistries;
import org.bson.codecs.configuration.CodecRegistry;
import org.bson.codecs.pojo.PojoCodecProvider;

/**
 *
 * @author emyla
 */
public class ManejadorConexiones {
    
    public final static String CONEXION = "mongodb://localhost:27017";
    
    public final static String BASE_DATOS = "Ghost_Tracks";
    
    public static MongoClient crearConexion(){
        MongoClient cliente = MongoClients.create(CONEXION);
        return cliente;
    }
    
    public static CodecRegistry obtenerCodecs(){
        CodecProvider pojoCodecProvider = PojoCodecProvider.builder().automatic(true).build();
        
        CodecRegistry pojoCodecRegistry = fromRegistries(getDefaultCodecRegistry(), fromProviders(pojoCodecProvider));
       
        return pojoCodecRegistry;
    }
}
