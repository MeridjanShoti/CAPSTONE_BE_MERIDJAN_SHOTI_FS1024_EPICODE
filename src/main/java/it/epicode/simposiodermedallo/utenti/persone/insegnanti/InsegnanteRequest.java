package it.epicode.simposiodermedallo.utenti.persone.insegnanti;

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
    private String nome;
    private String cognome;
    private LocalDate dataNascita;
    private String email;
    private String password;
    private String username;
    private String bio;
    private String avatar;
    private String copertina;
    private List<String> strumenti;
    private MultipartFile curriculum;
    private double pagaOraria;
    private List<Long> corsiId;
}
