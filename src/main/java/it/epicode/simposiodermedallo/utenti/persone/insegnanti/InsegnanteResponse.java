package it.epicode.simposiodermedallo.utenti.persone.insegnanti;

import it.epicode.simposiodermedallo.auth.AppUser;
import it.epicode.simposiodermedallo.utenti.servizi.scuole.Scuola;
import it.epicode.simposiodermedallo.utenti.servizi.scuole.corsi.Corso;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class InsegnanteResponse {
    private Long id;
    private String nome;
    private String cognome;
    private LocalDate dataNascita;
    private String email;
    private String bio;
    private String avatar;
    private String copertina;
    private LocalDate dataRegistrazione;
    private List<String> strumenti;
    private List<Corso> corsi;
    private byte[] curriculum;
    private Scuola scuola;
    private AppUser appUser;

}
