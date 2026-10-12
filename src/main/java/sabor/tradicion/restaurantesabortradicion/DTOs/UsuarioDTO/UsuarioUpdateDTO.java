package sabor.tradicion.restaurantesabortradicion.DTOs.UsuarioDTO;


//DTO para actualizar datos de usuario

public record UsuarioUpdateDTO(long idUsuario, String nombre, String email, String password) {
}
