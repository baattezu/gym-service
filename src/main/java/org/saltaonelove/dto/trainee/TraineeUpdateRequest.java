package org.saltaonelove.dto.trainee;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import org.saltaonelove.dto.auth.AuthRequest;

import java.time.LocalDate;

@JsonInclude(JsonInclude.Include.NON_NULL)
public record TraineeUpdateRequest(
        @NotNull(message = "Auth creds are required") AuthRequest auth,
        @NotBlank(message = "Username is required") String username,
        @NotBlank(message = "First name is required") String firstName,
        @NotBlank(message = "Last name is required") String lastName,
        @JsonFormat(pattern = "yyyy-MM-dd") LocalDate dateOfBirth, String address,
        @NotNull(message = "IsActive boolean is required") Boolean isActive
) { }
