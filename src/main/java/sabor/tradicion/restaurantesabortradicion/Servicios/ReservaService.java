package sabor.tradicion.restaurantesabortradicion.Servicios;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import sabor.tradicion.restaurantesabortradicion.DTOs.MesaEstadoDTO;
import sabor.tradicion.restaurantesabortradicion.DTOs.ReservaDTO;
import sabor.tradicion.restaurantesabortradicion.Entidades.EstadoMesa;
import sabor.tradicion.restaurantesabortradicion.Entidades.EstadoReserva;
import sabor.tradicion.restaurantesabortradicion.Entidades.HorarioTurno;
import sabor.tradicion.restaurantesabortradicion.Entidades.Mesa;
import sabor.tradicion.restaurantesabortradicion.Entidades.Reserva;
import sabor.tradicion.restaurantesabortradicion.Entidades.Usuario;
import sabor.tradicion.restaurantesabortradicion.Repositorios.HorarioTurnoRepository;
import sabor.tradicion.restaurantesabortradicion.Repositorios.MesaRepository;
import sabor.tradicion.restaurantesabortradicion.Repositorios.ReservaRepository;
import sabor.tradicion.restaurantesabortradicion.Repositorios.UsuarioRepository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeParseException;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class ReservaService {

    @Autowired
    ReservaRepository reservaRepository;
    @Autowired
    MesaRepository mesaRepository;
    @Autowired
    UsuarioRepository usuarioRepository;
    @Autowired
    HorarioTurnoRepository horarioTurnoRepository;


    public List<MesaEstadoDTO> getAllMesasEstado(LocalDate fecha,Long turnoId) {

        var mesas = mesaRepository.findAll();

        List<Reserva> reservasDelTurno = reservaRepository.findByFechaAndHorarioTurnoIdAndEstadoNot(
                fecha, turnoId, EstadoReserva.CANCELADA
        );

        Set<Long> mesasOcupadasIds = reservasDelTurno.stream()
                .map(r -> r.getMesa().getIdMesa())
                .collect(Collectors.toSet());

        return mesas.stream().map(mesa -> {
            String estado = mesasOcupadasIds.contains(mesa.getIdMesa()) ? "OCUPADA" : "DISPONIBLE";
            return new MesaEstadoDTO(mesa.getIdMesa(),mesa.getCapacidadMaxima(), estado);
        }).collect(Collectors.toList());

    }

    public boolean realizarReserva(ReservaDTO reservaDTO) {
        if (reservaDTO == null
                || reservaDTO.idMesa() == null
                || reservaDTO.idUsuario() == null
                || reservaDTO.idHorarioTurno() == null
                || reservaDTO.fecha() == null
                || reservaDTO.nroPersonas() == null
                || reservaDTO.nroPersonas() <= 0) {
            return false;
        }

        final LocalDate fecha;
        try {
            fecha = LocalDate.parse(reservaDTO.fecha());
        } catch (DateTimeParseException exception) {
            return false;
        }

        Mesa mesa = mesaRepository.findById(reservaDTO.idMesa()).orElse(null);
        Usuario usuario = usuarioRepository.findById(reservaDTO.idUsuario()).orElse(null);
        HorarioTurno horarioTurno =
                horarioTurnoRepository.findById(reservaDTO.idHorarioTurno()).orElse(null);

        if (mesa == null
                || usuario == null
                || horarioTurno == null
                || mesa.getEstado() == EstadoMesa.EN_MANTENIMIENTO
                || mesa.getCapacidadMaxima() < reservaDTO.nroPersonas()) {
            return false;
        }

        boolean mesaOcupada = reservaRepository
                .findByFechaAndHorarioTurnoIdAndEstadoNot(
                        fecha,
                        horarioTurno.getId(),
                        EstadoReserva.CANCELADA
                )
                .stream()
                .anyMatch(reserva -> reserva.getMesa().getIdMesa().equals(mesa.getIdMesa()));

        if (mesaOcupada) {
            return false;
        }

        Reserva reserva = new Reserva();
        reserva.setMesa(mesa);
        reserva.setUsuario(usuario);
        reserva.setFecha(fecha);
        reserva.setFechaDeReserva(LocalDateTime.now());
        reserva.setNroPersonas(reservaDTO.nroPersonas());
        reserva.setHorarioTurno(horarioTurno);
        reserva.setEstado(EstadoReserva.PENDIENTE);
        reservaRepository.save(reserva);

        return true;
    }

    public List<HorarioTurno> getAllHorariosTurnos() {
        return horarioTurnoRepository.findAll();
    }
}
