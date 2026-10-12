package sabor.tradicion.restaurantesabortradicion.Servicios;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import sabor.tradicion.restaurantesabortradicion.DTOs.Reservas.CancelarReservaDTO;
import sabor.tradicion.restaurantesabortradicion.DTOs.Reservas.MesaEstadoDTO;
import sabor.tradicion.restaurantesabortradicion.DTOs.Reservas.ReservaRegistroDTO;
import sabor.tradicion.restaurantesabortradicion.DTOs.Reservas.ReservaVistaDTO;
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

    public boolean realizarReserva(ReservaRegistroDTO reservaRegistroDTO) {
        if (reservaRegistroDTO == null
                || reservaRegistroDTO.idMesa() == null
                || reservaRegistroDTO.idUsuario() == null
                || reservaRegistroDTO.idHorarioTurno() == null
                || reservaRegistroDTO.fecha() == null
                || reservaRegistroDTO.nroPersonas() == null
                || reservaRegistroDTO.nroPersonas() <= 0) {
            return false;
        }

        final LocalDate fecha;
        try {
            fecha = LocalDate.parse(reservaRegistroDTO.fecha());
        } catch (DateTimeParseException exception) {
            return false;
        }

        Mesa mesa = mesaRepository.findById(reservaRegistroDTO.idMesa()).orElse(null);
        Usuario usuario = usuarioRepository.findById(reservaRegistroDTO.idUsuario()).orElse(null);
        HorarioTurno horarioTurno =
                horarioTurnoRepository.findById(reservaRegistroDTO.idHorarioTurno()).orElse(null);

        if (mesa == null
                || usuario == null
                || horarioTurno == null
                || mesa.getEstado() == EstadoMesa.EN_MANTENIMIENTO
                || mesa.getCapacidadMaxima() < reservaRegistroDTO.nroPersonas()) {
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
        reserva.setNroPersonas(reservaRegistroDTO.nroPersonas());
        reserva.setHorarioTurno(horarioTurno);
        reserva.setEstado(EstadoReserva.PENDIENTE);
        reservaRepository.save(reserva);

        return true;
    }

    public boolean cancelarReserva(CancelarReservaDTO cancelarReserva) {
        Reserva reserva = reservaRepository.findById(cancelarReserva.idReserva()).orElse(null);
        if (reserva == null || reserva.getEstado() == EstadoReserva.CANCELADA) {
            //la reserva no existe o ya fue cancelada
            return false;
        }

        if(!reserva.getUsuario().getIdUsuario().equals(cancelarReserva.idUsuario())) {
            //La reserva no le pertenece al usuario que intenta cancelarla
            return false;
        }

        reserva.setEstado(EstadoReserva.CANCELADA);
        reservaRepository.save(reserva);
        return true;
    }

    public ReservaVistaDTO verReserva(Long idReserva) {

        var reservaVista = reservaRepository.findByIdConRelaciones(idReserva);

        if(!reservaVista.isPresent()) return null;

        return new ReservaVistaDTO(reservaVista.get());


    }

    public List<ReservaVistaDTO> verReservasPorFecha(LocalDate fecha) {
        var reserva = reservaRepository.findByFecha(fecha);
        return reserva.stream().map(ReservaVistaDTO::new).collect(Collectors.toList());

    }

    public List<HorarioTurno> getAllHorariosTurnos() {
        return horarioTurnoRepository.findAll();
    }
}
