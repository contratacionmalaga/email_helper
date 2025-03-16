package local.jarios.entity;

import com.fasterxml.uuid.Generators;
import jakarta.persistence.*;
import local.jarios.exceptions.MiUnknownHostException;
import local.jarios.helpers.ComunHelper;
import local.jarios.utils.TamanoCampos;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.UUID;

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
public class Estadistica extends Auditable {

    @Id
    @Column(name = "id", updatable = false, nullable = false)
    private UUID id;

    @OneToOne(
            fetch = FetchType.LAZY)
    @JoinColumn(
            name = "log_id",
            nullable = false,
            referencedColumnName = "id",
            foreignKey = @ForeignKey(name = "fk_estadistica_log",
                    foreignKeyDefinition = "FOREIGN KEY (log_id) REFERENCES log(id) ON DELETE CASCADE"))
    private Log logEntity;

    @Column(name = "equipo", nullable = false, length = TamanoCampos.TAMANO_250)
    private String equipo;

    @Column(name = "nTotalFicherosLeidos")
    private int nTotalFicherosLeidos;

    @Column(name = "nTotalFicherosProcesados")
    private int nTotalFicherosProcesados;

    @Column(name = "nRregistrosGc")
    private int nRregistrosGc;

    @Column(name = "fechaHoraInicialParseo", nullable = false)
    private Timestamp fechaHoraInicialParseo;

    @Column(name = "fechaHoraFinalParseo", nullable = false)
    private Timestamp fechaHoraFinalParseo;

    @Column(name = "fechaHoraInicialBaseDatos", nullable = false)
    private Timestamp fechaHoraInicialBaseDatos;

    @Column(name = "fechaHoraFinalBaseDatos", nullable = false)
    private Timestamp fechaHoraFinalBaseDatos;

    @Column(name = "duracionParseo", nullable = false, length = TamanoCampos.TAMANO_250)
    private String duracionParseo;

    @Column(name = "duracionBaseDatos", nullable = false, length = TamanoCampos.TAMANO_250)
    private String duracionBaseDatos;

    public Estadistica(Log logEntity) throws MiUnknownHostException {

        this.id = Generators.timeBasedEpochGenerator().generate();
        this.logEntity = logEntity;
        this.fechaHoraInicialParseo = Timestamp.valueOf(LocalDateTime.now());
        this.equipo = ComunHelper.getHostName();
    }
}
