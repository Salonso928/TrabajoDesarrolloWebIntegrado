package sabor.tradicion.restaurantesabortradicion.Entidades;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalTime;

@Entity
@Table(name = "horarios_turnos")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class HorarioTurno {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private LocalTime horaInicio;

    @Column(nullable = false)
    private LocalTime horaFin;

    @Column(nullable = false)
    private String descripcion;
}
