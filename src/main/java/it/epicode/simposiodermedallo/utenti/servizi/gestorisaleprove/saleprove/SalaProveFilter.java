package it.epicode.simposiodermedallo.utenti.servizi.gestorisaleprove.saleprove;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.DayOfWeek;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SalaProveFilter {
    private String citta;
    private Integer capienzaMin;
    private Double prezzoOrarioMax;
    private DayOfWeek giornoApertura;
}
