package local.jarios.entity;

import jakarta.persistence.*;
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

    private static final int MILESIMAS = 1000;
    private static final int SEGUNDOS = 60;
    private static final int MINUTOS = 60;

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

    @Column(name = "nTotalFicheros")
    private int nTotalFicheros;

    @Column(name = "nTotalProcesados")
    private int nTotalProcesados;

    @Column(name = "nRregistrosGc")
    private int nRregistrosGc;

    @Column(name = "fechaHoraInicial")
    private Timestamp fechaHoraInicial;

    @Column(name = "fechaHoraFinal")
    private Timestamp fechaHoraFinal;

    @Column(name = "duracion", length = TamanoCampos.TAMANO_FECHA_LARGA)
    private String duracion;

    public EstadisticaEntity(LogEntity logEntity) {

        this.logEntity = logEntity;
        this.fechaHoraInicial = Timestamp.valueOf(LocalDateTime.now());
    }

    /**
     *
     */
    public void calcularTiempoEjecucion() {

        /// Calculamos la diferencia en milisegundos
        long diffInMillis = this.fechaHoraFinal.getTime() - this.fechaHoraInicial.getTime();

        /// Calculamos las horas, minutos, segundos y milisegundos
        long hours = diffInMillis / (MILESIMAS * SEGUNDOS * MINUTOS);
        long minutes = (diffInMillis % (MILESIMAS * SEGUNDOS * MINUTOS)) / (MILESIMAS * SEGUNDOS);
        long seconds = (diffInMillis % (MILESIMAS * SEGUNDOS)) / MILESIMAS;
        long milliseconds = diffInMillis % MILESIMAS;

        /// Devolvemos el tiempo transcurrido en formato "hh:mm:ss:SSS"
        this.duracion = String.format("%sh %sm %ss %sml", hours, minutes, seconds, milliseconds);
    }
}
