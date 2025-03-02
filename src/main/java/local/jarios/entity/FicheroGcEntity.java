package local.jarios.entity;

import jakarta.persistence.*;
import local.jarios.interfaces.Actualizable;
import local.jarios.utils.TamanoCampos;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Objects;

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
        name = "ficheros_gc",
        indexes = {
                @Index(name = "idx_ficheros_gc_shortname", columnList = "shortName", unique = true)
        }
)
public class FicheroGcEntity extends Auditable implements Actualizable<FicheroGcEntity> {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", updatable = false, nullable = false)
    private int id;

    @ManyToOne(
            fetch = FetchType.LAZY)
    @JoinColumn(
            name = "log_id",
            nullable = false,
            referencedColumnName = "id",
            foreignKey = @ForeignKey(name = "fk_lista_ficheros_gc_log"))
    private LogEntity logEntity;

    @Column(name = "shortName", nullable = false, length = TamanoCampos.TAMANO_CAMPOS_FICHERO)
    private String shortName;

    @Column(name = "longName", nullable = false, length = TamanoCampos.TAMANO_CAMPOS_FICHERO)
    private String longName;

    @Column(name = "version", nullable = false, length = TamanoCampos.TAMANO_CAMPOS_FICHERO)
    private String version;

    @Column(name = "canonicalUri", nullable = false, length = TamanoCampos.TAMANO_CAMPOS_FICHERO)
    private String canonicalUri;

    @Column(name = "canonicalVersionUri", nullable = false, length = TamanoCampos.TAMANO_CAMPOS_FICHERO)
    private String canonicalVersionUri;

    @Column(name = "locationUri", nullable = false, length = TamanoCampos.TAMANO_CAMPOS_FICHERO)
    private String locationUri;

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        FicheroGcEntity that = (FicheroGcEntity) obj;
        return comparar (that);
    }

    private boolean comparar (FicheroGcEntity ficheroGcEntity) {
        return
                this.shortName.equalsIgnoreCase(ficheroGcEntity.getShortName()) &&
                this.longName.equalsIgnoreCase(ficheroGcEntity.getLongName()) &&
                this.version.equalsIgnoreCase(ficheroGcEntity.getVersion()) &&
                this.canonicalUri.equalsIgnoreCase(ficheroGcEntity.getCanonicalUri()) &&
                this.canonicalVersionUri.equalsIgnoreCase(ficheroGcEntity.getCanonicalVersionUri()) &&
                this.locationUri.equalsIgnoreCase(ficheroGcEntity.getLocationUri());
    }

    @Override
    public int hashCode() {
        return Objects.hash(shortName, longName, version, canonicalUri, canonicalVersionUri, locationUri);
    }

    @Override
    public String toString() {
        return
                this.shortName + "; " +
                this.longName + "; " +
                this.version + "; " +
                this.canonicalUri + "; " +
                this.canonicalVersionUri + "; " +
                this.locationUri;
    }

    @Override
    public String getUniqueKey() {
        return this.shortName;
    }

    @Override
    public void actualizarCon(FicheroGcEntity otro) {
        /// Actualiza los campos de este objeto con los valores del objeto otro (OcEntity)
        this.longName = otro.getLongName();
        this.version = otro.getVersion();
        this.canonicalUri = otro.getCanonicalUri();
        this.canonicalVersionUri = otro.getCanonicalVersionUri();
        this.locationUri = otro.getLocationUri();
    }
}
