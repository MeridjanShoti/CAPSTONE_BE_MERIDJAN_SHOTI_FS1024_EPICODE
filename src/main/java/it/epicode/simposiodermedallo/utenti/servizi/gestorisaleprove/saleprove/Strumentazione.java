package it.epicode.simposiodermedallo.utenti.servizi.gestorisaleprove.saleprove;

import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Strumentazione {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private List<String> ampliETestate;
    private List<String> microfoni;
    private List<String> mixer;
    private List<String> setBatteria;
    private List<String> altro;
}
