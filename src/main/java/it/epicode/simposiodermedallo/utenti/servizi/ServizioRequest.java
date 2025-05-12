package it.epicode.simposiodermedallo.utenti.servizi;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ServizioRequest {
    private String email;
    private String bio;
    private String avatar;
    private String copertina;
    private String username;
    private String password;
    private String ragioneSociale;
    private String indirizzoPrincipale;
    private List<String> altreSedi;
    private String numeroTelefono;
    private List<String> linkSocial;
    private String partitaIva;
}
