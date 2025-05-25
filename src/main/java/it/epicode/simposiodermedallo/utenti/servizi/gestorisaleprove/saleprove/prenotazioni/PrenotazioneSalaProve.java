package it.epicode.simposiodermedallo.utenti.servizi.gestorisaleprove.saleprove.prenotazioni;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import it.epicode.simposiodermedallo.utenti.persone.utentinormali.UtenteNormale;
import it.epicode.simposiodermedallo.utenti.servizi.gestorisaleprove.saleprove.SalaProve;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "prenotazioni_sale_prove")
public class PrenotazioneSalaProve {
    @Id
    @GeneratedValue (strategy = jakarta.persistence.GenerationType.IDENTITY)
    private Long id;
    @Column(nullable = false)
    private LocalDateTime inizio;
    @Column(nullable = false)
    private LocalDateTime fine;
    @ManyToOne
    @JsonIgnoreProperties
    @JoinColumn(name = "sala_id", nullable = false)
    private SalaProve salaProve;
    @Column(nullable = false)
    private int numMembri;
    @ManyToOne
    @JoinColumn(name = "utente_id", nullable = false)
    @JsonIgnoreProperties({"prenotazioni"})
    private UtenteNormale utente;
}
