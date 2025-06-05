package it.epicode.simposiodermedallo.utenti.servizi.scuole.corsi.iscrizioni;

import it.epicode.simposiodermedallo.utenti.persone.utentinormali.UtenteNormale;
import it.epicode.simposiodermedallo.utenti.servizi.scuole.corsi.Corso;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "iscrizioni")
public class Iscrizione {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne
    private Corso corso;
    @ManyToOne
    private UtenteNormale utente;
    @ElementCollection
    private List<Double> valutazioni;
    @ElementCollection
    private List<Presenze> presenze;
}
