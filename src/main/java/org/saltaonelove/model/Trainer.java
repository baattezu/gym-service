package org.saltaonelove.model;

public class Trainer extends User {
    private Long trainerId;
    private String specialization;

    public Trainer(){
    }

    public Trainer(String firstName, String lastName) {
        super(firstName, lastName);
    }

    public Trainer(String firstName, String lastName, String specialization) {
        super(firstName, lastName);
        this.specialization = specialization;
    }

    @Override
    public String toString() {
        return String.format(
                "Trainer { User ID: %s | Trainer ID: %s | Username: %s | First Name: %s | Last Name: %s | Specialization: %s }",
                getUserId(), trainerId, getUsername(), getFirstName(), getLastName(), specialization
        );
    }

    public Long getTrainerId() {
        return trainerId;
    }

    public void setTrainerId(Long trainerId) {
        this.trainerId = trainerId;
    }

    public String getSpecialization() {
        return specialization;
    }

    public void setSpecialization(String specialization) {
        this.specialization = specialization;
    }

}
