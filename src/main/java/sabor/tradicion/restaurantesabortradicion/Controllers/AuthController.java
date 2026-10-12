package sabor.tradicion.restaurantesabortradicion.Controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;
import sabor.tradicion.restaurantesabortradicion.DTOs.AuthDTO.LoginRequest;
import sabor.tradicion.restaurantesabortradicion.DTOs.AuthDTO.RegistroRequest;
import sabor.tradicion.restaurantesabortradicion.DTOs.AuthDTO.RespuestaAuth;
import sabor.tradicion.restaurantesabortradicion.Entidades.Usuario;
import sabor.tradicion.restaurantesabortradicion.Servicios.*;

@RestController
@RequestMapping("/api/auth")
@CrossOrigin(origins = "*")//para que cualquier frontend pueda acceder
public class AuthController {
    @Autowired
    private UserService userService;

    @Autowired
    private JwtService jwtService;


    @PostMapping("/register")
    public ResponseEntity<?> registrar(@RequestBody RegistroRequest request) {
        boolean registrado = userService.registrarUsuario(request);
        if (!registrado) {
            return ResponseEntity.badRequest().body("El correo ya está registrado.");
        }
        return ResponseEntity.ok("¡Usuario registrado con éxito!");
    }
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest loginRequest) {
        Usuario usuario = userService.validarCredenciales(loginRequest.getEmail(), loginRequest.getPassword());

        if (usuario == null) {
            return ResponseEntity.status(401).body("Credenciales incorrectas.");
        }

        UserDetails principal = User.builder()
                .username(usuario.getEmail())
                .password(usuario.getPassword())
                .roles(usuario.getRol().name())
                .build();

        String token = jwtService.generateToken(principal);
        RespuestaAuth respuesta = new RespuestaAuth(usuario, token);

        return ResponseEntity.ok(respuesta);

    }
}
