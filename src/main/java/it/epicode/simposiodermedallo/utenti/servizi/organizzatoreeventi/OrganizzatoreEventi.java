package it.epicode.simposiodermedallo.utenti.servizi.organizzatoreeventi;


import it.epicode.simposiodermedallo.utenti.servizi.Servizio;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.util.List;

@EqualsAndHashCode(callSuper = true)
@Data
@NoArgsConstructor
@Entity
@Table(name = "organizzatori_eventi")
public class OrganizzatoreEventi extends Servizio {

}
