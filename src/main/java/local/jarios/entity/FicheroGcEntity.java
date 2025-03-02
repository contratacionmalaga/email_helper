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
public class FicheroGcEntity extends AuditablePlus implements Actualizable<FicheroGcEntity> {

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

    /**
     * Método encargado de devolver si un objeto es igual a la instancia de esta clase
     * @param obj Objeto que voy a comparar con la clase actual
     * @return Valor devuelto TRUE | FALSE
     */
    @Override
    public boolean equals(Object obj) {

        /// Caso base devuelvo TRUE
        if (this == obj) return true;

        /// En caso de que el objeto sea NULL o que no sea de la misma CLASE devuelvo FALSE
        if (obj == null || getClass() != obj.getClass()) return false;

        /// En otro caso realizo un CAST del objeto como un FicheroGcEntity
        FicheroGcEntity that = (FicheroGcEntity) obj;

        /// Devuelvo la comparación
        return comparar (that);
    }

    /**
     * Metodo utlizado para comparar un objeto FicheroGcEntity con la instancia actual de la clase
     * @param ficheroGcEntity Objeto que voy a comparar con la instancia actual de la clase
     * @return Devuelvo TRUE | FALSE si los objetos son iguales
     */
    private boolean comparar (FicheroGcEntity ficheroGcEntity) {
        return
                this.shortName.equalsIgnoreCase(ficheroGcEntity.getShortName()) &&
                this.longName.equalsIgnoreCase(ficheroGcEntity.getLongName()) &&
                this.version.equalsIgnoreCase(ficheroGcEntity.getVersion()) &&
                this.canonicalUri.equalsIgnoreCase(ficheroGcEntity.getCanonicalUri()) &&
                this.canonicalVersionUri.equalsIgnoreCase(ficheroGcEntity.getCanonicalVersionUri()) &&
                this.locationUri.equalsIgnoreCase(ficheroGcEntity.getLocationUri());
    }

    /**
     *
     * @return Devuel
     */
    @Override
    public int hashCode() {
        return Objects.hash(shortName, longName, version, canonicalUri, canonicalVersionUri, locationUri);
    }

    /**
     * Método que devuelve una cadena de caracteres con la representación del objeto
     * @return Cadena de caracteres con la representación del objeto
     */
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

    /**
     * Método que se tiene que implementar al extender la clase Actualizable. Devuelve el campo único que serivrá como
     *      Key para el Map
     * @return Devuelve el valor del campo único
     */
    @Override
    public String getUniqueKey() {

        /// Devuelve el valor del campo único (tiene definido un índice de tipo UNIQUE)
        return this.shortName;
    }

    /**
     * Método que actualiza la instancia actual de FicheroGcEntity con otro valor
     * @param otro El objeto que actualizará la instanacia actual del FicheroGcEntity
     */
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
