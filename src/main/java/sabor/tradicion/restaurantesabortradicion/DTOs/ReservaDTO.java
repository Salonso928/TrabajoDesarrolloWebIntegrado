package sabor.tradicion.restaurantesabortradicion.DTOs;

public record ReservaDTO(Long idReserva, Long idMesa, Long idUsuario, String fecha, Integer nroPersonas, Long idHorarioTurno) {
}
