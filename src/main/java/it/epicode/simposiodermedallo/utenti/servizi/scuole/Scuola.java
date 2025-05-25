package it.epicode.simposiodermedallo.utenti.servizi.scuole;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import it.epicode.simposiodermedallo.utenti.persone.insegnanti.Insegnante;
import it.epicode.simposiodermedallo.utenti.servizi.Servizio;
import it.epicode.simposiodermedallo.utenti.servizi.scuole.corsi.Corso;
import jakarta.persistence.Entity;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.util.List;

@EqualsAndHashCode(callSuper = true)
@Data
@NoArgsConstructor
@Entity
@Table(name = "scuole")

public class Scuola extends Servizio {

}
