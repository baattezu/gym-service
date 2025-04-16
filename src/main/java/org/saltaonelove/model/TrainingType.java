package org.saltaonelove.model;


import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.Immutable;

import java.util.List;

@Entity
@Table(name = "training_types")
@Immutable
@NamedQuery(
        name="TrainingType.findByName",
        query="SELECT tt FROM TrainingType tt WHERE tt.name LIKE :ttName"
)
@Getter
@Setter
public class TrainingType {

    @Id
    @Column(name = "training_type_id")
    private Long trainingTypeId;

    @Column(name = "training_type_name")
    @NotNull
    private String name;

    @JsonIgnore
    @OneToMany(mappedBy = "trainingType")
    List<Training> trainingList;

    public TrainingType() {
    }

    public TrainingType(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return String.format("Training Type { Training Type Id: %s | Training Type Name: %s",
                trainingTypeId, name );
    }

}
