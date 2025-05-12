package it.epicode.simposiodermedallo.utenti.persone.utentinormali;

import it.epicode.simposiodermedallo.auth.AppUser;
import jakarta.persistence.CascadeType;
import jakarta.persistence.OneToOne;

import java.time.LocalDate;

public class UtenteNormaleRequest {
    private String nome;
    private String cognome;
    private LocalDate dataNascita;
    private String email;
    private String bio;
    private String avatar;
    private String copertina;
}
