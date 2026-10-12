package sabor.tradicion.restaurantesabortradicion.DTOs.AuthDTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import sabor.tradicion.restaurantesabortradicion.Entidades.Usuario;

//solo usar para pruebas
@AllArgsConstructor
@Data
public class RespuestaAuth {
    Usuario usuario;
    String token;

}
