package sabor.tradicion.restaurantesabortradicion.Servicios;

import org.springframework.stereotype.Service;
import sabor.tradicion.restaurantesabortradicion.DTOs.RegistroRequest;
import sabor.tradicion.restaurantesabortradicion.Entidades.Usuario;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class UserService {
    private final Map<String, Usuario> usuariosEnMemoria = new ConcurrentHashMap<>();

    public UserService() {
        usuariosEnMemoria.put("cliente@sabor.com", new Usuario("cliente@sabor.com", "1234", "USER"));
        usuariosEnMemoria.put("admin@sabor.com", new Usuario("admin@sabor.com", "1234", "ADMIN"));
        usuariosEnMemoria.put("empleado@sabor.com", new Usuario("empleado@sabor.com", "1234", "EMPLOYEE"));
    }
    public boolean registrarUsuario(RegistroRequest request) {
        if (usuariosEnMemoria.containsKey(request.getEmail())) {
            return false;
        }
        //se crean usuarios por defecto, los otros roles son colocados por el admin
        Usuario nuevoUsuario= new Usuario(request.getEmail(),request.getPassword(),"USER");
        usuariosEnMemoria.put(request.getEmail(), nuevoUsuario);
        return true;
    }

    public Usuario validarCredenciales(String email, String password) {
        Usuario usuario = usuariosEnMemoria.get(email);
        if (usuario != null && usuario.getPassword().equals(password)) {
            return usuario;
        }
        return null;
    }
}
