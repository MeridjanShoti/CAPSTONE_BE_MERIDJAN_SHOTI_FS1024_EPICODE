package it.epicode.simposiodermedallo.auth;

import lombok.Data;

@Data
public class RegisterRequest {
    private String username;
    private String password;
}
