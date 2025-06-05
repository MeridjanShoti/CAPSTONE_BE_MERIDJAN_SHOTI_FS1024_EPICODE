package it.epicode.simposiodermedallo.utenti.servizi;

import it.epicode.simposiodermedallo.utenti.Utente;
import jakarta.persistence.MappedSuperclass;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.util.List;

@EqualsAndHashCode(callSuper = true)
@Data
@AllArgsConstructor
@NoArgsConstructor
@MappedSuperclass
public abstract class Servizio extends Utente {
    private String ragioneSociale;
    private String indirizzoPrincipale;
    private List<String> altreSedi;
    private String numeroTelefono;
    private List<String> linkSocial;
    private String partitaIva;
}
