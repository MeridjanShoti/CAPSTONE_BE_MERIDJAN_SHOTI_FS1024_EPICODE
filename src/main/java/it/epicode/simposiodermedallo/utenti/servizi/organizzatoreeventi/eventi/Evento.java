package it.epicode.simposiodermedallo.utenti.servizi.organizzatoreeventi.eventi;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import it.epicode.simposiodermedallo.utenti.persone.utentinormali.UtenteNormale;
import it.epicode.simposiodermedallo.utenti.servizi.organizzatoreeventi.OrganizzatoreEventi;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

@Data
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "Eventi")

public class Evento {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;
    @Column(nullable = false)
    private String nomeEvento;
    @Column(nullable = false)
    private int maxPartecipanti;
    @Column(nullable = false)
    private int minPartecipanti;
    @Column(nullable = false)
    private LocalDate dataEvento;
    private String locandina;
    @Column(columnDefinition = "TEXT")
    private String note;
    @ManyToMany
    @JsonIgnoreProperties("eventi")
    private List<UtenteNormale> partecipanti;
    @Column(columnDefinition = "TEXT")
    private List<String> fotoEvento;
    @ManyToOne
    @JsonIgnoreProperties("eventi")
    private OrganizzatoreEventi organizzatore;
    private LocalTime aperturaPorte;
    private LocalTime fineEvento;
    @ElementCollection
    private List<String> artistiPartecipanti;
    private String luogo;
    private String citta;
    @Enumerated(EnumType.STRING)
    private TipoEvento tipoEvento;
    private double prezzoBiglietto;
}