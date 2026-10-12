package sabor.tradicion.restaurantesabortradicion.DTOs.Reservas;

import sabor.tradicion.restaurantesabortradicion.DTOs.UsuarioDTO.UsuarioVistaDTO;
import sabor.tradicion.restaurantesabortradicion.Entidades.EstadoReserva;
import sabor.tradicion.restaurantesabortradicion.Entidades.HorarioTurno;
import sabor.tradicion.restaurantesabortradicion.Entidades.Mesa;
import sabor.tradicion.restaurantesabortradicion.Entidades.Reserva;

public record ReservaVistaDTO(long idReserva, Mesa mesa, UsuarioVistaDTO usuario, String fecha, String fechaReserva, Integer nroPersonas, HorarioTurno horarioTurno, EstadoReserva estado) {
    public ReservaVistaDTO(Reserva r) {
        this(
                r.getIdReserva(),
                r.getMesa(),
                new UsuarioVistaDTO(r.getUsuario().getIdUsuario(), r.getUsuario().getNombre()),
                r.getFecha() != null ? r.getFecha().toString() : null,
                r.getFechaDeReserva() != null ? r.getFechaDeReserva().toString() : null,
                r.getNroPersonas(),
                r.getHorarioTurno(),
                r.getEstado()
        );
    }
}
