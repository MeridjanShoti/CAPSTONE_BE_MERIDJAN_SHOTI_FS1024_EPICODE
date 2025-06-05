package it.epicode.simposiodermedallo.utenti.servizi.scuole.corsi;

import it.epicode.simposiodermedallo.utenti.persone.insegnanti.Insegnante;
import it.epicode.simposiodermedallo.utenti.persone.utentinormali.UtenteNormale;
import it.epicode.simposiodermedallo.utenti.servizi.scuole.Scuola;
import it.epicode.simposiodermedallo.utenti.servizi.scuole.corsi.enums.Livello;
import it.epicode.simposiodermedallo.utenti.servizi.scuole.corsi.enums.StatoCorso;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Set;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CorsoResponse {
    private Long id;
    private String nomeCorso;
    private List<String> strumenti;
    private Insegnante insegnante;
    private Scuola scuola;
    private List<UtenteNormale> partecipanti;
    private int maxPartecipanti;
    private int minPartecipanti;
    private Livello livello;
    private double costo;
    private LocalDate dataInizio;
    private LocalDate dataFine;
    private Set<DayOfWeek> giorniLezione;
    private StatoCorso statoCorso;
    private String note;
    private LocalTime orarioInizio;
    private LocalTime orarioFine;
    private String obiettivi;
    private String locandina;
}
