package sabor.tradicion.restaurantesabortradicion.Controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import sabor.tradicion.restaurantesabortradicion.DTOs.UsuarioDTO.UsuarioUpdateDTO;
import sabor.tradicion.restaurantesabortradicion.Entidades.Usuario;
import sabor.tradicion.restaurantesabortradicion.Servicios.UserService;

import java.util.List;

@RestController
@RequestMapping("/api/users")
@CrossOrigin(origins = "*")
public class UserController {

    @Autowired
    UserService userService;

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping
    public ResponseEntity<List<Usuario>> obtenerTodosLosUsuarios() {
        return ResponseEntity.ok(userService.obtenerTodosLosUsuarios());
    }
    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/{id}")
    public ResponseEntity<Usuario> obtenerUsuarioPorId(@PathVariable Long id) {
        Usuario usuario = userService.obtenerUsuarioPorId(id);
        if (usuario == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(usuario);
    }

    @PutMapping("/{id}")
    public ResponseEntity<String> actualizarDatos(@PathVariable Long id,@RequestBody UsuarioUpdateDTO usuarioUpdateDTO) {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        //no va a dar null ya que para entrar aqui ya se debio haber verificado que el usuario si existe
        Usuario usuario = (Usuario) authentication.getPrincipal();
        if (usuarioUpdateDTO == null || !usuarioUpdateDTO.idUsuario().equals(usuario.getIdUsuario())) {
            return ResponseEntity.badRequest().body("Los datos del usuario no coinciden con el ID indicado.");
        }

        boolean actualizado = userService.actualizarDatos(usuarioUpdateDTO);
        if (!actualizado) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("No se actualizaron los datos: el usuario no existe o el correo ya está en uso.");
        }
        return ResponseEntity.ok("Los datos del usuario se actualizaron correctamente.");
    }
}
