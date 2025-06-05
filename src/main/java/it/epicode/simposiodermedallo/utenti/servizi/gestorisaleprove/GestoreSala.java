package it.epicode.simposiodermedallo.utenti.servizi.gestorisaleprove;

import it.epicode.simposiodermedallo.utenti.servizi.Servizio;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@EqualsAndHashCode(callSuper = true)
@Data
@NoArgsConstructor
@Entity
@Table(name = "gestori_sala")
public class GestoreSala extends Servizio {

}
