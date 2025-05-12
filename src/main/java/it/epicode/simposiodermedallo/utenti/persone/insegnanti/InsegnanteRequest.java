package it.epicode.simposiodermedallo.utenti.persone.insegnanti;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
@Data
@AllArgsConstructor
@NoArgsConstructor
public class InsegnanteRequest {
    private String nome;
    private String cognome;
    private String dataNascita;
    private String email;
    private String password;
    private String username;
    private String bio;
    private String avatar;
    private String copertina;
    private List<String> strumenti;
    private String curriculum;
    private double pagaOraria;
    private long scuolaId;
    private List<Long> corsiId;
}
