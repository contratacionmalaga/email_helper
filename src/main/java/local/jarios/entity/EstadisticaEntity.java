package local.jarios.entity;

import jakarta.persistence.*;
import local.jarios.utils.ConstantesGenerales;
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
        name = "estadistica",
        schema = "imp_placsp_gc"
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
            foreignKey = @ForeignKey(
                    name = "fk_estadistica_log",
                    foreignKeyDefinition =
                            "FOREIGN KEY (log_id) " +
                            "REFERENCES " + ConstantesGenerales.ESQUEMA_PRINCIPAL + "log(id) ON DELETE CASCADE"))
    private LogEntity logEntity;

    @Column(name = "nFicheros")
    private int nFicheros;

    @Column(name = "nRregistrosGc")
    private int nRregistrosGc;

    @Column(name = "fechaHoraInicial")
    private Timestamp fechaHoraInicial;

    @Column(name = "fechaHoraFinal")
    private Timestamp fechaHoraFinal;

    @Column(name = "duracion")
    private String duracion;

    public EstadisticaEntity(LogEntity logEntity) {

        this.logEntity = logEntity;
        this.fechaHoraInicial = Timestamp.valueOf(LocalDateTime.now());
    }

    public void calcularTiempoEjecucion() {

        /// Calculamos la diferencia en milisegundos
        long diffInMillis = this.fechaHoraFinal.getTime() - this.fechaHoraInicial.getTime();

        /// Calculamos las horas, minutos, segundos y milisegundos
        long hours = diffInMillis / (1000 * 60 * 60);
        long minutes = (diffInMillis % (1000 * 60 * 60)) / (1000 * 60);
        long seconds = (diffInMillis % (1000 * 60)) / 1000;
        long milliseconds = diffInMillis % 1000;

        /// Devolvemos el tiempo transcurrido en formato "hh:mm:ss:SSS"
        this.duracion = String.format("%02d:%02d:%02d:%03d", hours, minutes, seconds, milliseconds);
    }

    public void aumentarNumFicheros() {

        this.nFicheros ++;
    }

    public void aumentarNumRegistrosGc(int numRegistros) {

        this.nRregistrosGc += numRegistros;
    }
}
