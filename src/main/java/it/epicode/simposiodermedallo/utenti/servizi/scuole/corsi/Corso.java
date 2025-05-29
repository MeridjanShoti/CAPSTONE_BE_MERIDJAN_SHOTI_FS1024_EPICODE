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
import java.time.LocalTime;
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
    private Insegnante insegnante;
    @ManyToOne
    private Scuola scuola;
    @ManyToMany
    private List<UtenteNormale> partecipanti;
    private int maxPartecipanti;
    private int minPartecipanti;
    @Enumerated(EnumType.STRING)
    private Livello livello;
    private double costo;
    private LocalDate dataInizio;
    private LocalDate dataFine;
    @Enumerated(EnumType.STRING)
    private TipoFrequenza frequenza;
    private StatoCorso statoCorso;
    private String note;
    private LocalTime orarioInizio;
    private LocalTime orarioFine;
    private String linkLezione;
    private String obiettivi;
    private String locandina;
}
