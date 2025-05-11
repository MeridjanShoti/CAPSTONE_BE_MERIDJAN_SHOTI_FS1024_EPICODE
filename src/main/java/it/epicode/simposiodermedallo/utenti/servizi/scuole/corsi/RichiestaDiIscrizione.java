package it.epicode.simposiodermedallo.utenti.servizi.scuole.corsi;

import it.epicode.simposiodermedallo.utenti.persone.utentinormali.UtenteNormale;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "richieste_di_iscrizione")
public class RichiestaDiIscrizione {
    @Id
    @OneToOne
    Corso corso;
    @OneToOne
    UtenteNormale utenteNormale;
}
