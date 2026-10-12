package sabor.tradicion.restaurantesabortradicion.Repositorios;

import org.springframework.data.jpa.repository.JpaRepository;
import sabor.tradicion.restaurantesabortradicion.Entidades.Usuario;

import java.util.Optional;

public interface UsuarioRepository extends JpaRepository<Usuario, Long> {
    Optional<Usuario> findByEmail(String email);

}
