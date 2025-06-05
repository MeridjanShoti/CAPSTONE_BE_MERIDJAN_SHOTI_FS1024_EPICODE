package it.epicode.simposiodermedallo.utenti.servizi.gestorisaleprove.saleprove;

import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Embeddable
public class Strumentazione {
    private List<String> ampliETestate;
    private List<String> microfoni;
    private List<String> mixer;
    private List<String> setBatteria;
    private List<String> altro;
}
