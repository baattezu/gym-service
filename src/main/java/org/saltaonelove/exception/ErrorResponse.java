package org.saltaonelove.exception;

import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.validation.constraints.NotNull;

import java.util.Map;

@JsonInclude(JsonInclude.Include.NON_NULL)
public record ErrorResponse(
        @NotNull int status,
        @NotNull String message,
        Map<String, String> errors) {
    public ErrorResponse(int status, String message){
        this(status, message, null);
    }
}
