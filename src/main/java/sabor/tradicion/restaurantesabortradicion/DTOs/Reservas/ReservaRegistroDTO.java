package sabor.tradicion.restaurantesabortradicion.DTOs.Reservas;

public record ReservaRegistroDTO(Long idReserva, Long idMesa, Long idUsuario, String fecha, Integer nroPersonas, Long idHorarioTurno) {
}
