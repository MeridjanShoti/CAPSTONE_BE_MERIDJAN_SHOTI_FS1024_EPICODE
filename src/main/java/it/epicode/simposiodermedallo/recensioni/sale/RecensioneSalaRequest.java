package it.epicode.simposiodermedallo.recensioni.sale;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RecensioneSalaRequest {
    private String testo;
    @Min(value = 1, message = "Il voto deve essere almeno 1")
    @Max(value = 5, message = "Il voto massimo è 5")
    @NotNull(message = "Il voto è obbligatorio")
    private Integer voto;
}
