package local.jarios.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.sql.Timestamp;

/**
 * Description: Clase que añade elementos de Auditorías a las clases que la extienden
 * Author: juan
 * Date: 04/06/2024
 * Team: Juan Antonio
 */
@Setter
@Getter
@NoArgsConstructor

// Indica que esta clase no será una entidad por sí misma,
// pero sus propiedades serán incluidas en cualquier entidad que la extienda.
@MappedSuperclass
public class Auditable {

    // Getters y Setters
    @Column(name = "created_at", nullable = false, updatable = false)
    private Timestamp createdAt;

    @PrePersist
    protected void onCreate() {
        createdAt = new Timestamp(System.currentTimeMillis());
    }

}
