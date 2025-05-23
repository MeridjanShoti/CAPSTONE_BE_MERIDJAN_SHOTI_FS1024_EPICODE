package it.epicode.simposiodermedallo.utenti.servizi.organizzatoreeventi.eventi.prenotazioni;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
@Data
@AllArgsConstructor
@NoArgsConstructor
public class PrenotazioneFilter {
    private LocalDate data1;
    private LocalDate data2;
    private String artista;
    private Boolean soloFuturi;
    private String nomeParziale;
}
