package itson.org.gestionarticulos.dtos;

import java.time.LocalDate;

/**
 *
 * @author emyla
 */
public class ReporteInventarioDTO {
    
    private LocalDate fechaGeneracion;
    private String periodo;
    private Integer totalArticulos;
    private Double valorMonetario;
    private InventarioDTO inventario;
    private AdministradorDTO autor;

    public ReporteInventarioDTO() {
        
    }

    public ReporteInventarioDTO(LocalDate fechaGeneracion, String periodo, Integer totalArticulos, Double valorMonetario, InventarioDTO inventario, AdministradorDTO autor) {
        this.fechaGeneracion = fechaGeneracion;
        this.periodo = periodo;
        this.totalArticulos = totalArticulos;
        this.valorMonetario = valorMonetario;
        this.inventario = inventario;
        this.autor = autor;
    }

    public LocalDate getFechaGeneracion() {
        return fechaGeneracion;
    }

    public void setFechaGeneracion(LocalDate fechaGeneracion) {
        this.fechaGeneracion = fechaGeneracion;
    }

    public String getPeriodo() {
        return periodo;
    }

    public void setPeriodo(String periodo) {
        this.periodo = periodo;
    }

    public Integer getTotalArticulos() {
        return totalArticulos;
    }

    public void setTotalArticulos(Integer totalArticulos) {
        this.totalArticulos = totalArticulos;
    }

    public Double getValorMonetario() {
        return valorMonetario;
    }

    public void setValorMonetario(Double valorMonetario) {
        this.valorMonetario = valorMonetario;
    }

    public InventarioDTO getInventario() {
        return inventario;
    }

    public void setInventario(InventarioDTO inventario) {
        this.inventario = inventario;
    }

    public AdministradorDTO getAutor() {
        return autor;
    }

    public void setAutor(AdministradorDTO autor) {
        this.autor = autor;
    }
    
    
}
