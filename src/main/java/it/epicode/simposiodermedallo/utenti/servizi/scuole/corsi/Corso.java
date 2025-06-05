package it.epicode.simposiodermedallo.utenti.servizi.scuole.corsi;

import it.epicode.simposiodermedallo.utenti.persone.insegnanti.Insegnante;
import it.epicode.simposiodermedallo.utenti.persone.utentinormali.UtenteNormale;
import it.epicode.simposiodermedallo.utenti.servizi.scuole.Scuola;
import it.epicode.simposiodermedallo.utenti.servizi.scuole.corsi.enums.Livello;
import it.epicode.simposiodermedallo.utenti.servizi.scuole.corsi.enums.StatoCorso;
import jakarta.persistence.*;
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
    @ElementCollection(targetClass = DayOfWeek.class)
    private Set<DayOfWeek> giorniLezione;
    @Enumerated(EnumType.STRING)
    private StatoCorso statoCorso;
    @Column(columnDefinition = "TEXT")
    private String note;
    private LocalTime orarioInizio;
    private LocalTime orarioFine;
    private String linkLezione;
    @Column(columnDefinition = "TEXT")
    private String obiettivi;
    private String locandina;
}
