package sabor.tradicion.restaurantesabortradicion.Repositorios;

import org.springframework.data.jpa.repository.JpaRepository;
import sabor.tradicion.restaurantesabortradicion.Entidades.EstadoReserva;
import sabor.tradicion.restaurantesabortradicion.Entidades.Reserva;

import java.time.LocalDate;
import java.util.List;

public interface ReservaRepository extends JpaRepository<Reserva, Long> {

    List<Reserva> findByFechaAndHorarioTurnoIdAndEstadoNot(LocalDate fecha, Long turnoId, EstadoReserva estado);
}
