package org.saltaonelove.model.dto.auth;


import com.fasterxml.jackson.annotation.JsonInclude;

@JsonInclude(JsonInclude.Include.NON_NULL)
public record AuthResponse(
        String username,
        String password,
        String token
) {
    public AuthResponse(String username, String password) {
        this(username, password, null);
    }

    public AuthResponse(String token){
        this(null, null, token);
    }
}
