package itson.org.gestionarticulos.entidades;

import itson.org.gestionarticulos.enums.EstadoProducto;
import itson.org.gestionarticulos.enums.Genero;
import itson.org.gestionarticulos.enums.TipoProducto;
import java.time.LocalDateTime;

/**
 * 
 * @author emyla
 */
public class Producto {

    private String idProducto;
    private String titulo;
    private String artista;
    private TipoProducto tipo;
    private Genero genero;
    private Double precio;
    private Integer stockInicial;
    private EstadoProducto estado;
    private Imagen imgProducto;
    private LocalDateTime fechaRegistro;

    public Producto() {
        
    }

    public Producto(
            String idProducto, 
            String titulo, 
            String artista, 
            TipoProducto tipo, 
            Genero genero, 
            Double precio, 
            Integer stockInicial, 
            EstadoProducto estado, 
            Imagen imgProducto, 
            LocalDateTime fechaRegistro
    ){
        this.idProducto = idProducto;
        this.titulo = titulo;
        this.artista = artista;
        this.tipo = tipo;
        this.genero = genero;
        this.precio = precio;
        this.stockInicial = stockInicial;
        this.estado = estado;
        this.imgProducto = imgProducto;
        this.fechaRegistro = fechaRegistro;
    }

    public String getIdProducto() {
        return idProducto;
    }

    public void setIdProducto(String idProducto) {
        this.idProducto = idProducto;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public String getArtista() {
        return artista;
    }

    public void setArtista(String artista) {
        this.artista = artista;
    }

    public TipoProducto getTipo() {
        return tipo;
    }

    public void setTipo(TipoProducto tipo) {
        this.tipo = tipo;
    }

    public Genero getGenero() {
        return genero;
    }

    public void setGenero(Genero genero) {
        this.genero = genero;
    }

    public Double getPrecio() {
        return precio;
    }

    public void setPrecio(Double precio) {
        this.precio = precio;
    }

    public Integer getStockInicial() {
        return stockInicial;
    }

    public void setStockInicial(Integer stockInicial) {
        this.stockInicial = stockInicial;
    }

    public EstadoProducto getEstado() {
        return estado;
    }

    public void setEstado(EstadoProducto estado) {
        this.estado = estado;
    }

    public Imagen getImgProducto() {
        return imgProducto;
    }

    public void setImgProducto(Imagen imgProducto) {
        this.imgProducto = imgProducto;
    }

    public LocalDateTime getFechaRegistro() {
        return fechaRegistro;
    }

    public void setFechaRegistro(LocalDateTime fechaRegistro) {
        this.fechaRegistro = fechaRegistro;
    }

    public String toString() {
        return "Producto{" +
                "id='" + idProducto + '\'' +
                ", titulo='" + titulo + '\'' +
                ", artista='" + artista + '\'' +
                ", precio=$" + precio +
                ", stock=" + stockInicial +
                '}';
    }
    
}
