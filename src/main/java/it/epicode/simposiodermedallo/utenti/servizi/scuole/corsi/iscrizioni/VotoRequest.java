package it.epicode.simposiodermedallo.utenti.servizi.scuole.corsi.iscrizioni;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import lombok.Data;

@Data
public class VotoRequest {
    @Min(0)
    @Max(10)
    private Double valutazione;
}
