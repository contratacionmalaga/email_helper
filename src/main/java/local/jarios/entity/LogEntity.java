package local.jarios.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

/**
 * Description: LogEntity
 * Author: Juan Antonio
 * Date: 04/06/2024
 * Team: Juan Antonio
 */

@Setter
@Getter
@Entity
@NoArgsConstructor
@Table(
        name = "log",
        schema = "imp_placsp_gc"
)
public class LogEntity extends Auditable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", updatable = false, nullable = false)
    private int id;


    @OneToMany(mappedBy = "logEntity", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<FicheroGcEntity> ficherosGcEntity;

    @OneToOne(mappedBy = "logEntity", cascade = CascadeType.ALL, orphanRemoval = true)
    private EstadisticaEntity estadisticaEntity;

}
