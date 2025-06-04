package it.epicode.simposiodermedallo.segnalazioni;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SegnalazioneRequest {
    private TipoSegnalazione tipoSegnalazione;
    private String descrizione;
}
