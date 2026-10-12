package sabor.tradicion.restaurantesabortradicion.Entidades;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "mesas")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class Mesa {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idMesa;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private EstadoMesa estado;

    @Column(nullable = false)
    private Integer capacidadMaxima;

}
