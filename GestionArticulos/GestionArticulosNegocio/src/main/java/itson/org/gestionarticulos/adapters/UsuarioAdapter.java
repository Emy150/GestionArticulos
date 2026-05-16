package itson.org.gestionarticulos.adapters;

import itson.org.gestionarticulos.dtos.AdministradorDTO;
import itson.org.gestionarticulos.entidades.Administrador;
import itson.org.gestionarticulos.exceptions.NegocioException;
import java.util.logging.Logger;

/**
 * Clase encargada de traducir la información de los usuarios (Administradores)
 * entre la capa de presentación (DTOs) y la base de datos (Entidades).
 * * @author emyla
 */
public class UsuarioAdapter {

    private static final Logger LOGGER = Logger.getLogger(UsuarioAdapter.class.getName());

    /**
     * Convierte una entidad Administrador (que viene de BD) a un AdministradorDTO (para la pantalla).
     * @param admin
     * @return 
     * @throws itson.org.gestionarticulos.exceptions.NegocioException
     */
    public AdministradorDTO adaptAdministradorToDTO(Administrador admin) throws NegocioException {
        
        // Validación de seguridad para evitar NullPointerExceptions
        if (admin == null) {
            LOGGER.severe("ERROR! El administrador recibido es nulo :o");
            throw new NegocioException("No fue posible adaptar, la entidad Administrador es nula.");
        }

        try {
            AdministradorDTO dto = new AdministradorDTO();

            // Mapeamos los datos personales
            dto.setNombre(admin.getNombre());
            dto.setApellidoPaterno(admin.getApellidoPaterno());
            dto.setApellidoMaterno(admin.getApellidoMaterno());
            dto.setCorreo(admin.getCorreo());
            
            // OJO: Por seguridad, rara vez enviamos la contraseña de vuelta a la interfaz gráfica.
            // Si tu app la necesita estrictamente en la vista, descomenta la siguiente línea:
            // dto.setContrasenia(admin.getContrasenia());

            LOGGER.fine("Conversión de Administrador a DTO realizada con éxito :D");
            return dto;

        } catch (Exception e) {
            LOGGER.severe("ERROR al armar el AdministradorDTO!");
            throw new NegocioException("Ocurrió un error al adaptar de Administrador a DTO", e);
        }
    }

    /**
     * Convierte un AdministradorDTO (que viene de la pantalla) a una entidad Administrador (para la BD).
     */
    public Administrador adaptDTOToAdministrador(AdministradorDTO dto) throws NegocioException {
        
        // Validamos que el DTO traiga información
        if (dto == null) {
            LOGGER.severe("ERROR! El AdministradorDTO llegó nulo D:");
            throw new NegocioException("No fue posible adaptar, el DTO es nulo.");
        }

        try {
            Administrador admin = new Administrador();

            // Pasamos todos los datos (aquí sí incluimos la contraseña porque va hacia el sistema/BD)
            admin.setNombre(dto.getNombre());
            admin.setApellidoPaterno(dto.getApellidoPaterno());
            admin.setApellidoMaterno(dto.getApellidoMaterno());
            admin.setCorreo(dto.getCorreo());
            admin.setContrasenia(dto.getContrasenia());
            
            LOGGER.fine("Conversión de DTO a Entidad Administrador lista yey!");
            return admin;

        } catch (Exception e) {
            LOGGER.severe("ERROR al armar la entidad Administrador!");
            throw new NegocioException("Ocurrió un error al adaptar de DTO a Administrador", e);
        }
    }
}