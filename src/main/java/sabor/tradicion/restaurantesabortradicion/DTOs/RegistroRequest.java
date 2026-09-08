package sabor.tradicion.restaurantesabortradicion.DTOs;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;

@AllArgsConstructor
@Data
public class RegistroRequest {
    private String email;
    private String password;
}
