package it.epicode.simposiodermedallo.utenti.servizi.gestorisaleprove.saleprove.prenotazioni;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PrenotazioneSalaRequest {
    @NotNull
    private LocalDateTime inizio;
    @NotNull
    private LocalDateTime fine;
    @NotBlank
    @Min(1)
    private int numMembri;
}
