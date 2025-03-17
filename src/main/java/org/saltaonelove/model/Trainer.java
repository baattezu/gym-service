package org.saltaonelove.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;

import java.util.List;

@Entity
@Table(name = "trainer")
@PrimaryKeyJoinColumn(name = "trainer_id", referencedColumnName = "user_id")
@NamedQuery(
        name="findTrainer_ByUsername",
        query="SELECT tr FROM Trainer tr WHERE tr.username LIKE :username"
)
public class Trainer extends User {
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "specialization", referencedColumnName = "training_type_id", nullable = false)
    @NotNull(message = "Specialization should not be null")
    private TrainingType specialization;

    @ManyToMany(mappedBy = "trainers", fetch = FetchType.EAGER)
    private List<Trainee> trainees;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "trainer", orphanRemoval = true, fetch = FetchType.LAZY)
    private List<Training> trainings;

    public Trainer(){
    }

    public Trainer(String firstName, String lastName) {
        super(firstName, lastName);
    }

    public Trainer(String firstName, String lastName, TrainingType specialization) {
        super(firstName, lastName);
        this.specialization = specialization;
    }

    @Override
    public String toString() {
        return String.format(
                "Trainer { Trainer ID: %s | Username: %s | First Name: %s | Last Name: %s | Specialization: %s }",
                getUserId(), getUsername(), getFirstName(), getLastName(), specialization.getName()
        );
    }

    public TrainingType getSpecialization() {
        return specialization;
    }

    public void setSpecialization(TrainingType specialization) {
        this.specialization = specialization;
    }

    public List<Trainee> getTrainees() {
        return trainees;
    }

    public void setTrainees(List<Trainee> trainees) {
        this.trainees = trainees;
    }

    public List<Training> getTrainings() {
        return trainings;
    }

    public void setTrainings(List<Training> trainings) {
        this.trainings = trainings;
    }
}
