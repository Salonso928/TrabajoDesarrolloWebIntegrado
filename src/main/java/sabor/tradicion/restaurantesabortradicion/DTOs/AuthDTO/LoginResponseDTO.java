package sabor.tradicion.restaurantesabortradicion.DTOs.AuthDTO;

import sabor.tradicion.restaurantesabortradicion.Entidades.Rol;

public record LoginResponseDTO(String token, UsuarioDTO usuario) {
}
