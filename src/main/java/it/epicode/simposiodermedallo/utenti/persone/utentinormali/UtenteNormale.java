package it.epicode.simposiodermedallo.utenti.persone.utentinormali;

import it.epicode.simposiodermedallo.utenti.persone.Persona;
import it.epicode.simposiodermedallo.utenti.servizi.gestorisaleprove.saleprove.prenotazioni.PrenotazioneSalaProve;
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
    private List<Corso> corsi;
    @OneToMany (mappedBy = "utente")
    private List<PrenotazioneSalaProve> prenotazioniEventi;
    @OneToMany (mappedBy = "utente")
    private List<PrenotazioneSalaProve> prenotazioniSale;

}
