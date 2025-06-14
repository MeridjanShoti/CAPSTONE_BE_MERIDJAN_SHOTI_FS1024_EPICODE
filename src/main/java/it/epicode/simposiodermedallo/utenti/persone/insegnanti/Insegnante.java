package it.epicode.simposiodermedallo.utenti.persone.insegnanti;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import it.epicode.simposiodermedallo.utenti.persone.Persona;
import it.epicode.simposiodermedallo.utenti.servizi.scuole.Scuola;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.util.List;

@EqualsAndHashCode(callSuper = true)
@Data
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "Insegnanti")

public class Insegnante extends Persona {
    @ElementCollection
    private List<String> strumenti;
    @Lob
    private byte[] curriculum;
    private double pagaOraria;
    @ManyToOne
    @JsonIgnoreProperties({"insegnanti"})
    private Scuola scuola;
}