package sabor.tradicion.restaurantesabortradicion.Servicios;

import org.springframework.stereotype.Service;
import sabor.tradicion.restaurantesabortradicion.DTOs.RegistroRequest;
import sabor.tradicion.restaurantesabortradicion.DTOs.UsuarioUpdateDTO;
import sabor.tradicion.restaurantesabortradicion.Entidades.Rol;
import sabor.tradicion.restaurantesabortradicion.Entidades.Usuario;
import sabor.tradicion.restaurantesabortradicion.Repositorios.UsuarioRepository;

import java.util.List;

@Service
public class UserService {
    private final UsuarioRepository usuarioRepository;

    public UserService(UsuarioRepository usuarioRepository) {
        this.usuarioRepository = usuarioRepository;
    }

    public boolean registrarUsuario(RegistroRequest request) {
        if (usuarioRepository.findByEmail(request.getEmail()).isPresent()) {
            return false;
        }

        // Los usuarios nuevos se registran como clientes; los otros roles los asigna un administrador.
        Usuario nuevoUsuario = new Usuario(
                request.getNombre(),
                request.getEmail(),
                request.getPassword(),
                Rol.CLIENTE
        );
        usuarioRepository.save(nuevoUsuario);
        return true;
    }

    public Usuario validarCredenciales(String email, String password) {
        return usuarioRepository.findByEmail(email)
                .filter(usuario -> usuario.getPassword().equals(password))
                .orElse(null);
    }

    public List<Usuario> obtenerTodosLosUsuarios() {
        return usuarioRepository.findAll();
    }

    public Usuario obtenerUsuarioPorId(Long id) {
        return usuarioRepository.findById(id).orElse(null);
    }

    //en el DTO se traen los datos que se van a sobreescribir
    public boolean actualizarDatos(UsuarioUpdateDTO usuario) {
        Usuario usuarioExistente = usuarioRepository.findById(usuario.idUsuario()).orElse(null);
        if (usuarioExistente == null) {
            return false; // El usuario no existe
        }

        if (usuario.email() != null && !usuario.email().equals(usuarioExistente.getEmail())) {
            var emailEnUso = usuarioRepository.findByEmail(usuario.email());
            if (emailEnUso.isPresent()) {
                return false;
            }
            usuarioExistente.setEmail(usuario.email());
        }

        if (usuario.nombre() != null && !usuario.nombre().isBlank()) {
            usuarioExistente.setNombre(usuario.nombre());
        }
        if (usuario.password() != null && !usuario.password().isBlank()) {
            usuarioExistente.setPassword(encriptarPassword(usuario.password()));
        }

        usuarioRepository.save(usuarioExistente);
        return true;
    }

    private String encriptarPassword(String password) {
        // por implementar
        return password;
    }
}
