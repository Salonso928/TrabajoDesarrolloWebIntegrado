package sabor.tradicion.restaurantesabortradicion.Entidades;

import lombok.AllArgsConstructor;
import lombok.Data;

@AllArgsConstructor
@Data
public class Usuario {
    private String email;
    private String password;
    private String rol;

}
