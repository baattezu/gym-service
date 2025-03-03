package org.saltaonelove.dto;

import java.time.LocalDate;

public record TraineeDTO(
        String firstName, String lastName,
        String dateOfBirth, String address
) {
    public TraineeDTO(String firstName, String lastName){
        this(firstName, lastName, null, null);
    }
}
