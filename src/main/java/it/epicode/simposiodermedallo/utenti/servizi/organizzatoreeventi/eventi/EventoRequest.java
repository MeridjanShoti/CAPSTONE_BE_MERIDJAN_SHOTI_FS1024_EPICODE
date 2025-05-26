package it.epicode.simposiodermedallo.utenti.servizi.organizzatoreeventi.eventi;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
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
    @NotBlank
    private String nomeEvento;
    @NotNull
    private int maxPartecipanti;
    @NotNull
    private int minPartecipanti;
    @NotNull
    private LocalDate dataEvento;
    private String note;
    private List<String> artistiPartecipanti;
    @NotBlank
    private String luogo;
    @NotBlank
    private String citta;
    @NotNull
    private TipoEvento tipoEvento;
    @NotNull
    private double prezzoBiglietto;
    private String locandina;
    @NotNull
    private LocalTime aperturaPorte;
    @NotNull
    private LocalTime fineEvento;
}
