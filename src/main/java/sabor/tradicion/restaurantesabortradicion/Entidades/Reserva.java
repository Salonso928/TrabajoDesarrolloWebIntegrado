package sabor.tradicion.restaurantesabortradicion.Entidades;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

@Entity
@Table(name = "reservas", uniqueConstraints = {
        @UniqueConstraint(columnNames = {"id_mesa", "fecha", "horario_turno_id"})
})
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class Reserva {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idReserva;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "id_mesa", nullable = false)
    private Mesa mesa;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "id_usuario", nullable = false)
    private Usuario usuario;

    @Column(nullable = false)
    private LocalDate fecha;

    @Column(nullable = false)
    private LocalDateTime fechaDeReserva; //Fecha en que se realizo la reserva

    @Column(nullable = false)
    private Integer nroPersonas;//NO DEBE EXCEDER LA CAPACIDAD MAXIMA DE LA MESA, SE VALIDARA EN EL SERVICIO

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "horario_turno_id", nullable = false)
    private HorarioTurno horarioTurno;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private EstadoReserva estado;//este se pasara cuando lo registremos en el servicio

}
