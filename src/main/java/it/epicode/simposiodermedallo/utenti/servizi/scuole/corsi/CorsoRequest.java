package it.epicode.simposiodermedallo.utenti.servizi.scuole.corsi;

import it.epicode.simposiodermedallo.utenti.servizi.scuole.corsi.enums.Livello;
import it.epicode.simposiodermedallo.utenti.servizi.scuole.corsi.enums.StatoCorso;
import it.epicode.simposiodermedallo.utenti.servizi.scuole.corsi.enums.TipoFrequenza;
import jakarta.persistence.ElementCollection;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

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
    private TipoFrequenza frequenza;
    private String note;
    private String nomeCorso;
    private List<String> strumenti;
    private LocalTime orarioInizio;
    private LocalTime orarioFine;
    private String linkLezione;
    private String obiettivi;
}
