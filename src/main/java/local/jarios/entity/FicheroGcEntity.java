package local.jarios.entity;

import jakarta.persistence.*;
import local.jarios.utils.ConstantesGenerales;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Description: Importaciones de Ficheros Excel desde Internet
 * Author: Juan Antonio
 * Date: 04/06/2024
 * Team: Juan Antonio
 */

@Setter
@Getter
@Entity
@NoArgsConstructor
@Table(
        name = "_lista_ficheros_gc",
        schema = "imp_placsp_gc"
)
public class FicheroGcEntity extends Auditable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", updatable = false, nullable = false)
    private int id;

    @ManyToOne(
            cascade = CascadeType.ALL,
            fetch = FetchType.LAZY)
    @JoinColumn(
            name = "log_id",
            nullable = false,
            referencedColumnName = "id",
            foreignKey = @ForeignKey(
                    name = "fk_lista_ficheros_gc_log",
                    foreignKeyDefinition =
                            "FOREIGN KEY (log_id) " +
                            "REFERENCES " + ConstantesGenerales.ESQUEMA_PRINCIPAL + "log(id) ON DELETE CASCADE"))
    private LogEntity logEntity;

    @Column(name = "shortName", nullable = false, length = 500)
    private String shortName;

    @Column(name = "longName", length = 500)
    private String longName;

    @Column(name = "version", length = 500)
    private String version;

    @Column(name = "canonicalUri", length = 500)
    private String canonicalUri;

    @Column(name = "canonicalVersionUri", length = 500)
    private String canonicalVersionUri;

    @Column(name = "locationUri", length = 500)
    private String locationUri;

    @Override
    public String toString() {

        return "(" + shortName + "," + longName + "," + version + "," + canonicalUri + "," + canonicalVersionUri + "," + locationUri + ")";
    }
}
