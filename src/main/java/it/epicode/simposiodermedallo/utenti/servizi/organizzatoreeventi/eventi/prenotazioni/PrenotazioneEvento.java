package it.epicode.simposiodermedallo.utenti.servizi.organizzatoreeventi.eventi.prenotazioni;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import it.epicode.simposiodermedallo.utenti.persone.utentinormali.UtenteNormale;
import it.epicode.simposiodermedallo.utenti.servizi.organizzatoreeventi.eventi.Evento;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table (name = "prenotazioni_eventi")
public class PrenotazioneEvento {
    @Id
    @GeneratedValue (strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne
    @JsonIgnoreProperties({"prenotazioniEventi"})
    private UtenteNormale utenteNormale;
    @ManyToOne
    @JsonIgnoreProperties({"prenotazioniEventi"})
    private Evento evento;
    private int numeroBiglietti;
    @GeneratedValue (strategy = GenerationType.UUID)
    private String codicePrenotazione;
    private double prezzoPagato;
}
