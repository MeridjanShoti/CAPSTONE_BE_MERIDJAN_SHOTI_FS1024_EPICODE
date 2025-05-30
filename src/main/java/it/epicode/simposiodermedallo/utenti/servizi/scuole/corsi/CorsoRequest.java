package it.epicode.simposiodermedallo.utenti.servizi.scuole.corsi;

import it.epicode.simposiodermedallo.utenti.servizi.scuole.corsi.enums.Livello;
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
public class CorsoRequest {
    private int maxPartecipanti;
    private int minPartecipanti;
    private Livello livello;
    private double costo;
    private LocalDate dataInizio;
    private LocalDate dataFine;
    private Set<DayOfWeek> giorniLezione;
    private String note;
    private String nomeCorso;
    private List<String> strumenti;
    private LocalTime orarioInizio;
    private LocalTime orarioFine;
    private String linkLezione;
    private String obiettivi;
    private String locandina;
}
