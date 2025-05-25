package it.epicode.simposiodermedallo.utenti.servizi.gestorisaleprove.saleprove.prenotazioni;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PrenotazioneSalaFilter {
    private Long idSala;
    private String nomeSala;
    private LocalDate data1;
    private LocalDate data2;
    private Boolean soloFuturi;
}
