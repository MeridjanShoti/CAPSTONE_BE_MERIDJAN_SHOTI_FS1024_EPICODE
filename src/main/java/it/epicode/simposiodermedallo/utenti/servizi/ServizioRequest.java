package it.epicode.simposiodermedallo.utenti.servizi;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ServizioRequest {
    @NotBlank
    private String email;
    private String bio;
    private String avatar;
    private String copertina;
    private String username;
    private String password;
    @NotBlank
    private String ragioneSociale;
    @NotBlank
    private String indirizzoPrincipale;
    private List<String> altreSedi;
    @Pattern(regexp = "^\\+?[0-9\\s\\-]{7,20}$", message = "Numero di telefono non valido")
    private String numeroTelefono;
    private List<String> linkSocial;
    @NotBlank
    private String partitaIva;
}
