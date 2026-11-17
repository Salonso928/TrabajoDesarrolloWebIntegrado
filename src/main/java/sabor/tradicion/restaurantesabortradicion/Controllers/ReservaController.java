package sabor.tradicion.restaurantesabortradicion.Controllers;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.apache.coyote.Response;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import sabor.tradicion.restaurantesabortradicion.DTOs.Reservas.*;
import sabor.tradicion.restaurantesabortradicion.Entidades.HorarioTurno;
import sabor.tradicion.restaurantesabortradicion.Entidades.Reserva;
import sabor.tradicion.restaurantesabortradicion.Entidades.Usuario;
import sabor.tradicion.restaurantesabortradicion.Servicios.ReservaService;

import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.List;

@Tag(
        name = "Reservas",
        description = "Servicios para gestionar reservas, consultar disponibilidad de mesas y administrar horarios del restaurante"
)
@SecurityRequirement(name = "bearerAuth")
@RestController
@RequestMapping("/api/reservas")
@CrossOrigin(origins = "*")
public class ReservaController {

    private final ReservaService reservaService;

    public ReservaController(ReservaService reservaService) {
        this.reservaService = reservaService;
    }

    @Operation(
            summary = "Consultar estado de mesas",
            description = "Obtiene el estado de disponibilidad de las mesas del restaurante para una fecha y turno determinados."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Estado de mesas consultado correctamente"),
            @ApiResponse(responseCode = "400", description = "Fecha o turno inválido")
    })
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

    @Operation(
            summary = "Consultar horarios y turnos",
            description = "Obtiene los horarios y turnos configurados para realizar reservas en el restaurante."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Horarios obtenidos correctamente")
    })
    @GetMapping("/horarios")
    public ResponseEntity<List<HorarioTurno>> obtenerHorariosTurnos() {
        return ResponseEntity.ok(reservaService.getAllHorariosTurnos());
    }

    @Operation(
            summary = "Realizar reserva",
            description = "Registra una nueva reserva indicando el usuario, mesa, fecha, cantidad de personas y horario seleccionado."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Reserva registrada correctamente"),
            @ApiResponse(responseCode = "400", description = "Los datos de la reserva son inválidos"),
            @ApiResponse(responseCode = "409", description = "La mesa no está disponible para el horario solicitado")
    })
    @PostMapping("/realizar")
    public ResponseEntity<String> realizarReserva(@RequestBody ReservaRegistroDTO reservaRegistroDTO) {
        boolean reservada = reservaService.realizarReserva(reservaRegistroDTO);

        if (!reservada) {
            return ResponseEntity.badRequest()
                    .body("Error al realizar la reserva");
        }

        return ResponseEntity.ok("La reserva se realizó correctamente.");
    }

    @Operation(
            summary = "Cancelar reserva",
            description = "Permite cancelar una reserva registrada, identificando al usuario y la reserva correspondiente."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Reserva cancelada correctamente"),
            @ApiResponse(responseCode = "404", description = "Reserva o usuario no encontrado"),
            @ApiResponse(responseCode = "400", description = "No se puede cancelar la reserva")
    })
    @PostMapping("/cancelar")
    public ResponseEntity<String> cancelarReserva(@RequestBody CancelarReservaDTO cancelarReserva) {
        boolean cancelada = reservaService.cancelarReserva(cancelarReserva);

        if (!cancelada) {
            return ResponseEntity.badRequest()
                    .body("Error al cancelar la reserva");
        }
        return ResponseEntity.ok("La reserva se canceló correctamente.");
    }

    @Operation(
            summary = "Completar reserva",
            description = "Permite cambiar el estado de una reserva a COMPLETADA mediante los identificadores del usuario y de la reserva."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Reserva completada correctamente"),
            @ApiResponse(responseCode = "404", description = "Reserva o usuario no encontrado"),
            @ApiResponse(responseCode = "400", description = "No se puede completar la reserva")
    })
    @PostMapping("/completar")
    public ResponseEntity<String> completarReserva(@RequestBody CancelarReservaDTO cancelarReserva) {
        boolean completada = reservaService.completarReserva(cancelarReserva);

        if (!completada) {
            return ResponseEntity.badRequest()
                    .body("Error al completar la reserva");
        }
        return ResponseEntity.ok("La reserva se completó correctamente.");
    }

    @Operation(
            summary = "Consultar detalle de reserva",
            description = "Obtiene toda la información de una reserva específica, incluyendo los datos de la mesa, usuario, fecha, horario y estado."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Detalle de reserva obtenido correctamente"),
            @ApiResponse(responseCode = "404", description = "Reserva no encontrada")
    })
    @GetMapping("/reserva/{id}")
    public ResponseEntity<ReservaVistaDTO> verReserva(@PathVariable Long id) {
        var reserva = reservaService.verReserva(id);
        if (reserva == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(reserva);
    }

    //endpoint para ver todas las reservas de una fecha
    @Operation(
            summary = "Consultar reservas por fecha",
            description = "Obtiene el listado de reservas registradas para una fecha específica, incluyendo información de mesas, usuarios y horarios."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Reservas obtenidas correctamente"),
            @ApiResponse(responseCode = "400", description = "Formato de fecha inválido")
    })
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

    @Operation(
            summary = "Consultar reservas por usuario",
            description = "Obtiene el historial de reservas realizadas por un usuario específico, incluyendo fecha, horario y estado."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Reservas obtenidas correctamente"),
            @ApiResponse(responseCode = "404", description = "Usuario no encontrado")
    })
    @GetMapping("/usuario/{idUsuario}")
    public ResponseEntity<List<ReservaUserDTO>> verReservasPorUsuario(@PathVariable Long idUsuario) {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        //no va a dar null ya que para entrar aqui ya se debio haber verificado que el usuario si existe
        Usuario usuario = (Usuario) authentication.getPrincipal();
        System.out.println("Usuario autenticado: " + usuario.getNombre() + ", ID: " + usuario.getIdUsuario());
        System.out.println("Id del request: "+idUsuario);
        if(!usuario.getIdUsuario().equals(idUsuario)) {
            return ResponseEntity.badRequest().build();

        }

        return ResponseEntity.ok(reservaService.verReservasPorUsuario(idUsuario));
    }
}