package it.epicode.simposiodermedallo.utenti.servizi.scuole.corsi;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import it.epicode.simposiodermedallo.utenti.persone.insegnanti.Insegnante;
import it.epicode.simposiodermedallo.utenti.persone.utentinormali.UtenteNormale;
import it.epicode.simposiodermedallo.utenti.servizi.scuole.Scuola;
import it.epicode.simposiodermedallo.utenti.servizi.scuole.corsi.enums.Livello;
import it.epicode.simposiodermedallo.utenti.servizi.scuole.corsi.enums.StatoCorso;
import it.epicode.simposiodermedallo.utenti.servizi.scuole.corsi.enums.TipoFrequenza;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "corsi")
public class Corso {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;
    private String nomeCorso;
    @ElementCollection
    private List<String> strumenti;
    @ManyToOne
    @JsonIgnoreProperties({"corsiInsegnati"})
    private Insegnante insegnante;
    @ManyToOne
    @JsonIgnoreProperties({"corsi"})
    private Scuola scuola;
    @ManyToMany
    @JsonIgnoreProperties({"corsi"})
    private List<UtenteNormale> partecipanti;
    private int maxPartecipanti;
    private int minPartecipanti;
    private Livello livello;
    private double costo;
    private int durataInMesi;
    private LocalDate dataInizio;
    private LocalDate dataFine;
    private TipoFrequenza frequenza;
    private StatoCorso statoCorso;
    private String note;
}
