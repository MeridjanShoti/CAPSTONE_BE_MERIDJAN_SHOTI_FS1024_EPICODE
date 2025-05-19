package it.epicode.simposiodermedallo.utenti.persone.utentinormali;

import it.epicode.simposiodermedallo.auth.AppUser;
import jakarta.persistence.CascadeType;
import jakarta.persistence.OneToOne;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
@Data
@AllArgsConstructor
@NoArgsConstructor
    public class UtenteNormaleRequest {
    @NotBlank(message = "Il nome è obbligatorio")
    private String nome;
    @NotBlank(message = "Il cognome è obbligatorio")
    private String cognome;
    private LocalDate dataNascita;
    @NotBlank(message = "L'email è obbligatoria")
    private String email;
    private String bio;
    private String avatar;
    private String copertina;
    @NotBlank(message = "L'username è obbligatorio")
    private String username;
    @NotBlank(message = "La password è obbligatoria")
    private String password;
}
