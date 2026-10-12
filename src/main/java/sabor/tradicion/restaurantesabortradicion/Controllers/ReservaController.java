package sabor.tradicion.restaurantesabortradicion.Controllers;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import sabor.tradicion.restaurantesabortradicion.DTOs.MesaEstadoDTO;
import sabor.tradicion.restaurantesabortradicion.DTOs.ReservaDTO;
import sabor.tradicion.restaurantesabortradicion.Entidades.HorarioTurno;
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

    @PostMapping
    public ResponseEntity<String> realizarReserva(@RequestBody ReservaDTO reservaDTO) {
        boolean reservada = reservaService.realizarReserva(reservaDTO);

        if (!reservada) {
            return ResponseEntity.badRequest()
                    .body("Error al realizar la reserva");
        }

        return ResponseEntity.ok("La reserva se realizó correctamente.");
    }
}
