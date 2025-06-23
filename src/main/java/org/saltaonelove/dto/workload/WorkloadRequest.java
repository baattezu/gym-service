package org.saltaonelove.dto.workload;

import java.time.LocalDate;

public record WorkloadRequest(
        String trainerUsername,
        String trainerFirstName,
        String trainerLastName,
        Boolean isActive,
        LocalDate trainingDate,
        Long trainingDuration,
        ActionType actionType
) { }