package it.epicode.simposiodermedallo.utenti.persone.insegnanti;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDate;
import java.util.List;
@Data
@AllArgsConstructor
@NoArgsConstructor
public class InsegnanteRequest {
    @NotBlank(message = "Il nome è obbligatorio")
    private String nome;
    @NotBlank(message = "Il cognome è obbligatorio")
    private String cognome;
    private LocalDate dataNascita;
    @NotBlank(message = "L'email è obbligatoria")
    private String email;
    @NotBlank(message = "La password è obbligatoria")
    private String password;
    @NotBlank(message = "L'username è obbligatorio")
    private String username;
    private String bio;
    private String avatar;
    private String copertina;
    private List<String> strumenti;
    private MultipartFile curriculum;
    @NotNull(message = "La paga oraria è obbligatoria")
    private double pagaOraria;
}
