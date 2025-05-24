package it.epicode.simposiodermedallo.utenti.servizi.organizzatoreeventi.eventi.prenotazioni;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import it.epicode.simposiodermedallo.utenti.persone.utentinormali.UtenteNormale;
import it.epicode.simposiodermedallo.utenti.servizi.organizzatoreeventi.eventi.Evento;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.UUID;

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
    private String codicePrenotazione;
    private double prezzoPagato;
    private LocalDate dataPrenotazione;
    private LocalDate dataEvento;
    @PrePersist
    public void prePersist() {
        if (this.codicePrenotazione == null) {
            this.codicePrenotazione = UUID.randomUUID().toString();
        }
        if (this.dataPrenotazione == null) {
            this.dataPrenotazione = LocalDate.now();
        }
    }

}
