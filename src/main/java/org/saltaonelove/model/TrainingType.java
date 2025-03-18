package org.saltaonelove.model;


import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import org.hibernate.annotations.Immutable;

import java.util.List;

@Entity
@Table(name = "training_types")
@Immutable
@NamedQuery(
        name="findByName",
        query="SELECT tt FROM TrainingType tt WHERE tt.name LIKE :ttName"
)
public class TrainingType {

    @Id
    @Column(name = "training_type_id")
    private Long trainingTypeId;

    @Column(name = "training_type_name")
    @NotNull
    private String name;

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

    public Long getTrainingTypeId() {
        return trainingTypeId;
    }

    public void setTrainingTypeId(Long trainingTypeId) {
        this.trainingTypeId = trainingTypeId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
