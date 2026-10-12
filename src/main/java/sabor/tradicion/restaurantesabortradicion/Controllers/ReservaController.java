package sabor.tradicion.restaurantesabortradicion.Controllers;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import sabor.tradicion.restaurantesabortradicion.DTOs.Reservas.CancelarReservaDTO;
import sabor.tradicion.restaurantesabortradicion.DTOs.Reservas.MesaEstadoDTO;
import sabor.tradicion.restaurantesabortradicion.DTOs.Reservas.ReservaRegistroDTO;
import sabor.tradicion.restaurantesabortradicion.DTOs.Reservas.ReservaVistaDTO;
import sabor.tradicion.restaurantesabortradicion.Entidades.HorarioTurno;
import sabor.tradicion.restaurantesabortradicion.Entidades.Reserva;
import sabor.tradicion.restaurantesabortradicion.Servicios.ReservaService;

import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.List;

@RestController
@RequestMapping("/api/reservas")
@CrossOrigin(origins = "*")
public class ReservaController {

    private final ReservaService reservaService;

    public ReservaController(ReservaService reservaService) {
        this.reservaService = reservaService;
    }

    @GetMapping("/mesas")
    public ResponseEntity<List<MesaEstadoDTO>> obtenerEstadoMesas(@RequestParam String fecha,@RequestParam Long turnoId) {
        final LocalDate fechaReserva;
        try {
            fechaReserva = LocalDate.parse(fecha);
        } catch (DateTimeParseException exception) {
            return ResponseEntity.badRequest().build();
        }

        return ResponseEntity.ok(
                reservaService.getAllMesasEstado(fechaReserva, turnoId)
        );
    }

    @GetMapping("/horarios")
    public ResponseEntity<List<HorarioTurno>> obtenerHorariosTurnos() {
        return ResponseEntity.ok(reservaService.getAllHorariosTurnos());
    }

    @PostMapping("/realizar")
    public ResponseEntity<String> realizarReserva(@RequestBody ReservaRegistroDTO reservaRegistroDTO) {
        boolean reservada = reservaService.realizarReserva(reservaRegistroDTO);

        if (!reservada) {
            return ResponseEntity.badRequest()
                    .body("Error al realizar la reserva");
        }

        return ResponseEntity.ok("La reserva se realizó correctamente.");
    }

    @PostMapping("/cancelar")
    public ResponseEntity<String> cancelarReserva(@RequestBody CancelarReservaDTO cancelarReserva) {
        boolean cancelada = reservaService.cancelarReserva(cancelarReserva);

        if (!cancelada) {
            return ResponseEntity.badRequest()
                    .body("Error al cancelar la reserva");
        }
        return ResponseEntity.ok("La reserva se canceló correctamente.");
    }

    @GetMapping("/reserva/{id}")
    public ResponseEntity<ReservaVistaDTO> verReserva(@PathVariable Long id) {
        var reserva = reservaService.verReserva(id);
        if (reserva == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(reserva);
    }

    //endpoint para ver todas las reservas de una fecha
    @GetMapping("/fecha/{fecha}")
    public ResponseEntity<List<ReservaVistaDTO>> verReservasPorFecha(@PathVariable String fecha) {
        final LocalDate fechaReserva;
        try {
            fechaReserva = LocalDate.parse(fecha);
        } catch (DateTimeParseException exception) {
            return ResponseEntity.badRequest().build();
        }
        return ResponseEntity.ok(reservaService.verReservasPorFecha(fechaReserva));
    }
}