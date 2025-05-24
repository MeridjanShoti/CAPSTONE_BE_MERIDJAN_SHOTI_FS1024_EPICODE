package it.epicode.simposiodermedallo.utenti.servizi.organizzatoreeventi.eventi;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class EventoRequest {
    private String nomeEvento;
    private int maxPartecipanti;
    private int minPartecipanti;
    private LocalDate dataEvento;
    private String note;
    private List<String> artistiPartecipanti;
    private String luogo;
    private String citta;
    private TipoEvento tipoEvento;
    private double prezzoBiglietto;
    private String locandina;
    private LocalTime aperturaPorte;
    private LocalTime fineEvento;
}
