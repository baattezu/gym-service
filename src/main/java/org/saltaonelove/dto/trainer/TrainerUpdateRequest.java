package org.saltaonelove.dto.trainer;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import org.saltaonelove.dto.auth.AuthRequest;

public record TrainerUpdateRequest(
        @NotBlank(message = "Username is required") String username,
        @NotBlank(message = "First name is required") String firstName,
        @NotBlank(message = "Last name is required") String lastName,
        @NotBlank(message = "Specialization is required") String specialization,
        @NotNull(message = "IsActive boolean is required") Boolean isActive
) { }
