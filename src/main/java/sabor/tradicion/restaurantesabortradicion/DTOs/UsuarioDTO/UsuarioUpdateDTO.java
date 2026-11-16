package sabor.tradicion.restaurantesabortradicion.DTOs.UsuarioDTO;


//DTO para actualizar datos de usuario

public record UsuarioUpdateDTO(Long idUsuario, String nombre, String email, String password) {
}
