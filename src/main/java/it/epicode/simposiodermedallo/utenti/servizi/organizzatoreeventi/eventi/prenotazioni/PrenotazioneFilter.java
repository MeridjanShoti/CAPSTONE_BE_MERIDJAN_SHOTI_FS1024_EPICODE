package it.epicode.simposiodermedallo.utenti.servizi.organizzatoreeventi.eventi.prenotazioni;

import lombok.Data;

import java.time.LocalDate;
@Data
public class PrenotazioneFilter {
    private LocalDate data1;
    private LocalDate data2;
    private String artista;
    private Boolean soloFuturi;
    private String nomeParziale;
}
