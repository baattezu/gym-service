package org.saltaonelove.model.dto.trainer;

import jakarta.validation.constraints.NotBlank;

public record TrainerRequest(
        @NotBlank(message = "First name is required") String firstName,
        @NotBlank(message = "Last name is required") String lastName,
        @NotBlank(message = "Specialization is required") String specialization
) {
    public TrainerRequest(String firstName, String lastName){
        this(firstName, lastName, null);
    }
}
