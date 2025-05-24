package it.epicode.simposiodermedallo.utenti.servizi.organizzatoreeventi.eventi;

import lombok.Data;

import java.time.LocalDate;
@Data
public class EventoFilter {
    private String nomeParziale;
    private String citta;
    private LocalDate data1;
    private LocalDate data2;
    private TipoEvento tipoEvento;
    private String artista;
    private Boolean soloFuturi;
}
