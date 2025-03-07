package org.saltaonelove.dto;

public record TrainerDTO(
        String firstName, String lastName,
        String specialization
) {
    public TrainerDTO(String firstName, String lastName){
        this(firstName, lastName, null);
    }
}
