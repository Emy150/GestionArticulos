/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package itson.org.gestionarticulos.interfaces;

import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;

/**
 *
 * @author emyla
 */
public interface IBaseMongoDAO {
    
    public abstract MongoDatabase obtenerBaseDatos(MongoClient cliente);
    
    public abstract MongoCollection obtenerColeccion(MongoDatabase baseDatos);
}
