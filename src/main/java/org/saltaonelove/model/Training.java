package org.saltaonelove.model;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalTime;

public class Training {
    private Long trainingId;
    private Long trainerId;
    private Long traineeId;
    private String name;
    private TrainingType trainingType;
    private LocalDate date;
    private Duration duration;

    public Training(Long trainerId, Long traineeId, String name, TrainingType trainingType, LocalDate date, Duration duration) {
        this.trainerId = trainerId;
        this.traineeId = traineeId;
        this.name = name;
        this.trainingType = trainingType;
        this.date = date;
        this.duration = duration;
    }

    public Training() {
    }

    @Override
    public String toString() {
        return "Training{ \n" +
                "trainingId=" + trainingId +
                ",\n trainerId=" + trainerId +
                ",\n traineeId=" + traineeId +
                ",\n name='" + name + '\'' +
                ",\n trainingType=" + trainingType.getName() +
                ",\n date=" + date +
                ",\n duration=" + duration +
                '}';
    }

    public Long getTrainingId() {
        return trainingId;
    }

    public void setTrainingId(Long trainingId) {
        this.trainingId = trainingId;
    }

    public Long getTrainerId() {
        return trainerId;
    }

    public void setTrainerId(Long trainerId) {
        this.trainerId = trainerId;
    }

    public Long getTraineeId() {
        return traineeId;
    }

    public void setTraineeId(Long traineeId) {
        this.traineeId = traineeId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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

    public Duration getDuration() {
        return duration;
    }

    public void setDuration(Duration duration) {
        this.duration = duration;
    }
}
