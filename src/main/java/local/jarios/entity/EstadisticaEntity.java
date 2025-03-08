package local.jarios.entity;

import jakarta.persistence.*;
import local.jarios.exceptions.MiUnknownHostException;
import local.jarios.helpers.ComunHelper;
import local.jarios.utils.TamanoCampos;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.sql.Timestamp;
import java.time.LocalDateTime;

/**
 * Description: Importaciones de Ficheros Excel desde Internet
 * Author: Juan Antonio
 * Date: 04/06/2024
 * Team: Juan Antonio
 */

@Setter
@Getter
@NoArgsConstructor
@Entity
@Table(
        name = "estadistica"
)
public class EstadisticaEntity extends Auditable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", updatable = false, nullable = false)
    private int id;

    @OneToOne(
            cascade = CascadeType.ALL,
            fetch = FetchType.LAZY)
    @JoinColumn(
            name = "log_id",
            nullable = false,
            referencedColumnName = "id",
            foreignKey = @ForeignKey(name = "fk_estadistica_log"))
    private LogEntity logEntity;

    @Column(name = "equipo", nullable = false, length = TamanoCampos.TAMANO_100)
    private String equipo;

    @Column(name = "nTotalFicherosLeidos")
    private int nTotalFicherosLeidos;

    @Column(name = "nTotalFicherosProcesados")
    private int nTotalFicherosProcesados;

    @Column(name = "nRregistrosGc")
    private int nRregistrosGc;

    @Column(name = "fechaHoraInicial")
    private Timestamp fechaHoraInicial;

    @Column(name = "fechaHoraFinal")
    private Timestamp fechaHoraFinal;

    @Column(name = "duracion", length = TamanoCampos.TAMANO_100)
    private String duracion;

    public EstadisticaEntity(LogEntity logEntity) throws MiUnknownHostException {

        this.logEntity = logEntity;
        this.fechaHoraInicial = Timestamp.valueOf(LocalDateTime.now());
        this.equipo = ComunHelper.getHostName();
    }
}
