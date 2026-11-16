package sabor.tradicion.restaurantesabortradicion.DTOs.AuthDTO;

import sabor.tradicion.restaurantesabortradicion.Entidades.Rol;

public record UsuarioDTO (Long idUsuario, String nombre, String email, Rol rol){
}
