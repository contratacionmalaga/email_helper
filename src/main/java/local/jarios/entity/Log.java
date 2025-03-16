package local.jarios.entity;

import com.fasterxml.uuid.Generators;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.List;
import java.util.UUID;

/**
 * Description: LogEntity
 * Author: Juan Antonio
 * Date: 04/06/2024
 * Team: Juan Antonio
 */

@Setter
@Getter
@Entity
@Table(
        name = "log"
)
public class Log extends Auditable {

    @Id
    @Column(name = "id", updatable = false, nullable = false)
    private UUID id;

    @OneToMany(mappedBy = "logEntity", orphanRemoval = true)
    private List<FicheroGc> ficherosGc;

    @OneToOne(mappedBy = "logEntity", orphanRemoval = true)
    private Estadistica estadistica;

    @Override
    public String toString() {

        return this.id.toString();
    }

    public Log() {

        this.id = Generators.timeBasedEpochGenerator().generate();
    }

}
