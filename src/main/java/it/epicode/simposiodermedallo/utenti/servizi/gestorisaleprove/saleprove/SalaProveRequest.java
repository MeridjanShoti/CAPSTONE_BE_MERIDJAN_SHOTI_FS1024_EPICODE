package it.epicode.simposiodermedallo.utenti.servizi.gestorisaleprove.saleprove;


import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.DayOfWeek;
import java.time.LocalTime;
import java.util.List;
import java.util.Set;
@Data
@AllArgsConstructor
@NoArgsConstructor
public class SalaProveRequest {
    @NotBlank
    private String nomeSala;
    @NotBlank
    private String indirizzoSala;
    @NotBlank
    private String citta;
    private String descrizione;
    private String regolamento;
    @NotNull
    private Integer capienzaMax;
    @NotNull
    private Double prezzoOrario;
    private String copertinaSala;
    @NotNull
    private Set<DayOfWeek> giorniApertura;
    @NotNull
    private LocalTime orarioApertura;
    @NotNull
    private LocalTime orarioChiusura;
    private List<String> ampliETestate;
    private List<String> microfoni;
    private List<String> mixer;
    private List<String> setBatteria;
    private List<String> altro;

}
