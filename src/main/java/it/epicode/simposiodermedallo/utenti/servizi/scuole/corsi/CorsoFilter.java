package it.epicode.simposiodermedallo.utenti.servizi.scuole.corsi;

import it.epicode.simposiodermedallo.utenti.servizi.scuole.corsi.enums.Livello;
import it.epicode.simposiodermedallo.utenti.servizi.scuole.corsi.enums.StatoCorso;
import it.epicode.simposiodermedallo.utenti.servizi.scuole.corsi.enums.TipoFrequenza;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
@Data
@AllArgsConstructor
@NoArgsConstructor
public class CorsoFilter {
    private String nomeCorso;
    private Livello livello;
    private TipoFrequenza frequenza;
    private Double costo;
    private String strumenti;
    private LocalDate dataInizio;
    private LocalDate dataFine;
    private StatoCorso statoCorso;
    private Long insegnanteId;
    private Long scuolaId;
    private Long partecipanteId;
}
