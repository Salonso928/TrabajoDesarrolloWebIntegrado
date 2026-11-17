package sabor.tradicion.restaurantesabortradicion.Controllers;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
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
@Tag(
        name = "Usuarios",
        description = "Servicios para consultar y administrar la información de los usuarios registrados"
)
@SecurityRequirement(name = "bearerAuth")
@RestController
@RequestMapping("/api/users")
@CrossOrigin(origins = "*")
public class UserController {

    @Autowired
    UserService userService;

    @Operation(
            summary = "Listar usuarios",
            description = "Obtiene el listado completo de usuarios registrados en el sistema."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Listado de usuarios obtenido correctamente"),
            @ApiResponse(responseCode = "401", description = "No autorizado"),
            @ApiResponse(responseCode = "403", description = "No tiene permisos para consultar los usuarios")
    })
    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping
    public ResponseEntity<List<Usuario>> obtenerTodosLosUsuarios() {
        return ResponseEntity.ok(userService.obtenerTodosLosUsuarios());
    }

    @Operation(
            summary = "Consultar usuario por ID",
            description = "Obtiene la información de un usuario específico mediante su identificador."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Usuario encontrado"),
            @ApiResponse(responseCode = "404", description = "No se encontró el usuario solicitado"),
            @ApiResponse(responseCode = "401", description = "No autorizado")
    })

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/{id}")
    public ResponseEntity<Usuario> obtenerUsuarioPorId(@PathVariable Long id) {
        Usuario usuario = userService.obtenerUsuarioPorId(id);
        if (usuario == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(usuario);
    }

    @Operation(
            summary = "Actualizar datos del usuario",
            description = "Permite modificar la información de un usuario existente, como su nombre, correo electrónico y contraseña."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Datos actualizados correctamente"),
            @ApiResponse(responseCode = "400", description = "Datos proporcionados inválidos"),
            @ApiResponse(responseCode = "404", description = "Usuario no encontrado"),
            @ApiResponse(responseCode = "401", description = "No autorizado")
    })
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
