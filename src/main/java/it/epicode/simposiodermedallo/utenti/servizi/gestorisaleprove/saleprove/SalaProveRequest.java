package it.epicode.simposiodermedallo.utenti.servizi.gestorisaleprove.saleprove;

import jakarta.annotation.Nullable;
import jakarta.persistence.Column;
import jakarta.validation.constraints.NotBlank;
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
    @NotBlank
    private String descrizione;
    private String regolamento;
    @NotBlank
    private Integer capienzaMax;
    @NotBlank
    private Double prezzoOrario;
    private String copertinaSala;
    @NotBlank
    private Set<DayOfWeek> giorniApertura;
    @NotBlank
    private LocalTime orarioApertura;
    @NotBlank
    private LocalTime orarioChiusura;
    private List<String> ampliETestate;
    private List<String> microfoni;
    private List<String> mixer;
    private List<String> setBatteria;
    private List<String> altro;

}
