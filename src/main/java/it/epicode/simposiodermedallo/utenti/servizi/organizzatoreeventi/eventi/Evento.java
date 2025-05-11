package it.epicode.simposiodermedallo.utenti.servizi.organizzatoreeventi.eventi;

import it.epicode.simposiodermedallo.utenti.persone.utentinormali.UtenteNormale;
import it.epicode.simposiodermedallo.utenti.servizi.organizzatoreeventi.OrganizzatoreEventi;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Data
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "Evento")

public class Evento {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;
    private String nomeEvento;
    private int maxPartecipanti;
    private int minPartecipanti;
    private LocalDate dataEvento;
    private String note;
    @ManyToMany
    private List<UtenteNormale> partecipanti;
    private List<String> fotoEvento;
    @ManyToOne
    private OrganizzatoreEventi organizzatore;
    private LocalDateTime aperturaPorte;
    private LocalDateTime fineEvento;
    private List<String> artistiPartecipanti;
    private String luogo;
    private TipoEvento tipoEvento;
    private double prezzoBiglietto;
}