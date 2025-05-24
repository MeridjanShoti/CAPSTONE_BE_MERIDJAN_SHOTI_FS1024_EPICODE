package it.epicode.simposiodermedallo.utenti.persone.utentinormali;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import it.epicode.simposiodermedallo.utenti.persone.Persona;
import it.epicode.simposiodermedallo.utenti.servizi.gestorisaleprove.saleprove.prenotazioni.PrenotazioneSalaProve;
import it.epicode.simposiodermedallo.utenti.servizi.organizzatoreeventi.eventi.prenotazioni.PrenotazioneEvento;
import it.epicode.simposiodermedallo.utenti.servizi.scuole.corsi.Corso;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.util.List;

@EqualsAndHashCode(callSuper = true)
@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "utenti_normali")

public class UtenteNormale extends Persona {
    @ManyToMany (mappedBy = "partecipanti")
    @JsonIgnoreProperties({"partecipanti"})
    private List<Corso> corsi;
    @OneToMany (mappedBy = "utenteNormale")
    @JsonIgnoreProperties({"utenteNormale"})
    private List<PrenotazioneEvento> prenotazioniEventi;
    @OneToMany (mappedBy = "utente")
    @JsonIgnoreProperties({"utente"})
    private List<PrenotazioneSalaProve> prenotazioniSale;
}
