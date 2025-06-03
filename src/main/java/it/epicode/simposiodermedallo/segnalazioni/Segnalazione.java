package it.epicode.simposiodermedallo.segnalazioni;

import it.epicode.simposiodermedallo.auth.AppUser;
import jakarta.persistence.*;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "Segnalazioni")
public class Segnalazione {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Enumerated(EnumType.STRING)
    private TipoSegnalazione tipoSegnalazione;
    private String descrizione;
    @ManyToOne
    private AppUser autore;
    private Long idElemento;
}
