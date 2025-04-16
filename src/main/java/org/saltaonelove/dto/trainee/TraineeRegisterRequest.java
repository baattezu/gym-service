package org.saltaonelove.dto.trainee;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.NotBlank;

import java.time.LocalDate;

public record TraineeRegisterRequest(
        @NotBlank(message = "First name is required") String firstName,
        @NotBlank(message = "Last name is required") String lastName,

        @JsonFormat(pattern = "yyyy-MM-dd")
        LocalDate dateOfBirth,
        String address
) {
    public TraineeRegisterRequest(String firstName, String lastName){
        this(firstName, lastName, null, null);
    }
}
