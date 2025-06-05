package it.epicode.simposiodermedallo.segnalazioni;

import lombok.Data;

@Data
public class SegnalazioneFilter {
    private TipoSegnalazione tipoSegnalazione;
    private String autore;
}
