package it.epicode.simposiodermedallo.utenti.servizi.scuole.corsi.iscrizioni;

import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Embeddable
public class Presenze {
    private LocalDate data;
    private boolean presenza;
}
