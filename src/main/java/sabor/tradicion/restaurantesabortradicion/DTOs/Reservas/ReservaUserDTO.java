package sabor.tradicion.restaurantesabortradicion.DTOs.Reservas;

import sabor.tradicion.restaurantesabortradicion.Entidades.EstadoReserva;
import sabor.tradicion.restaurantesabortradicion.Entidades.Reserva;

import java.time.LocalDate;
import java.time.LocalDateTime;

public record ReservaUserDTO(Long idReserva, Long idMesa, LocalDateTime fechaReserva, LocalDate fecha, String horario, EstadoReserva estado) {

    public ReservaUserDTO(Reserva reserva){
        this(reserva.getIdReserva(), reserva.getMesa().getIdMesa(), reserva.getFechaDeReserva(), reserva.getFecha(), reserva.getHorarioTurno().getHoraInicio() + " - " + reserva.getHorarioTurno().getHoraFin(), reserva.getEstado());

    }
}


//{
//        "idReserva": id,
//        "idMesa" mesa,
//        "fechaReserva": (aqui es la fecha cuando se reservo la mesa),
//        "fecha": (es la fecha en la cual el cliente ira al restaurante)
//        "horario": el horario
//"Estado": el estado de la reserva
//}