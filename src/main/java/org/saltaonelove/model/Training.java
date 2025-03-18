package org.saltaonelove.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;

@Entity
@Table(name = "training")
public class Training {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "training_id")
    private Long trainingId;

    @ManyToOne
    @JoinColumn(name = "trainee_id")
    @NotNull(message = "Trainee should not be null")
    private Trainee trainee;

    @ManyToOne
    @JoinColumn(name = "trainer_id")
    @NotNull(message = "Trainer should not be null")
    private Trainer trainer;

    @Column(name = "training_name")
    @NotNull(message = "Training name should not be null")
    private String trainingName;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "training_type_id")
    @NotNull(message = "Training Type should not be null")
    private TrainingType trainingType;

    @Column(name = "training_date")
    @NotNull(message = "Date of training should not be null")
    private LocalDate date;

    @Column(name = "training_duration")
    @NotNull(message = "Duration of training should not be null")
    private Long duration;

    public Training() {
    }

    @Override
    public String toString() {
        return String.format("Training { Training Id: %s | Trainer: %s | Trainee: %s | Name: %s | Training Type: %s | Date: %s | Duration: %s }",
                trainingId, trainer.getUsername(), trainee.getUsername(), trainingName, trainingType.getName(), date, duration);
    }

    public Long getTrainingId() {
        return trainingId;
    }

    public void setTrainingId(Long trainingId) {
        this.trainingId = trainingId;
    }

    public Trainee getTrainee() {
        return trainee;
    }

    public void setTrainee(Trainee trainee) {
        this.trainee = trainee;
    }

    public Trainer getTrainer() {
        return trainer;
    }

    public void setTrainer(Trainer trainer) {
        this.trainer = trainer;
    }

    public String getTrainingName() {
        return trainingName;
    }

    public void setTrainingName(String trainingName) {
        this.trainingName = trainingName;
    }

    public TrainingType getTrainingType() {
        return trainingType;
    }

    public void setTrainingType(TrainingType trainingType) {
        this.trainingType = trainingType;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public Long getDuration() {
        return duration;
    }

    public void setDuration(Long duration) {
        this.duration = duration;
    }
}
