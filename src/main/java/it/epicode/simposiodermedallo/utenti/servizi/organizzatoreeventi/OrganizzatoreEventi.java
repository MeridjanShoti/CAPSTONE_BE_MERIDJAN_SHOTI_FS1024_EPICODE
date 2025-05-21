package it.epicode.simposiodermedallo.utenti.servizi.organizzatoreeventi;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import it.epicode.simposiodermedallo.utenti.servizi.Servizio;
import it.epicode.simposiodermedallo.utenti.servizi.organizzatoreeventi.eventi.Evento;
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
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "organizzatori_eventi")
public class OrganizzatoreEventi extends Servizio {
    @OneToMany (mappedBy = "organizzatore")
    @JsonIgnoreProperties({"organizzatore"})
    private List<Evento> eventi;
}
