package it.epicode.simposiodermedallo.utenti.persone.utentinormali;

import it.epicode.simposiodermedallo.utenti.persone.Persona;
import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;


@EqualsAndHashCode(callSuper = true)
@Entity
@Data
@Table(name = "utenti_normali")
@NoArgsConstructor
public class UtenteNormale extends Persona {

}
