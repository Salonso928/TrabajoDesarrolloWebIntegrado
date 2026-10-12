package sabor.tradicion.restaurantesabortradicion.DTOs.AuthDTO;

import lombok.AllArgsConstructor;
import lombok.Data;

@AllArgsConstructor
@Data
public class RegistroRequest {
    private String nombre;
    private String email;
    private String password;
}
