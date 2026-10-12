package sabor.tradicion.restaurantesabortradicion.Repositorios;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import sabor.tradicion.restaurantesabortradicion.Entidades.EstadoReserva;
import sabor.tradicion.restaurantesabortradicion.Entidades.Reserva;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface ReservaRepository extends JpaRepository<Reserva, Long> {

    List<Reserva> findByFechaAndHorarioTurnoIdAndEstadoNot(LocalDate fecha, Long turnoId, EstadoReserva estado);

    @Query("SELECT r FROM Reserva r JOIN FETCH r.usuario u JOIN FETCH r.horarioTurno h JOIN FETCH r.mesa m WHERE r.idReserva = :id")
    Optional<Reserva> findByIdConRelaciones(@Param("id") Long id);

    @Query("SELECT r FROM Reserva r JOIN FETCH r.usuario u JOIN FETCH r.horarioTurno h JOIN FETCH r.mesa m WHERE r.fecha = :fecha")
    List<Reserva> findByFecha(@Param("fecha") LocalDate fecha);
}
