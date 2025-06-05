package it.epicode.simposiodermedallo.utenti.servizi.gestorisaleprove.saleprove.prenotazioni;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import it.epicode.simposiodermedallo.utenti.persone.utentinormali.UtenteNormale;
import it.epicode.simposiodermedallo.utenti.servizi.gestorisaleprove.saleprove.SalaProve;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;
import java.util.UUID;

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
    @ManyToOne(cascade = CascadeType.REMOVE)
    @JsonIgnoreProperties
    @JoinColumn(name = "sala_id", nullable = false)
    private SalaProve salaProve;
    @Column(nullable = false)
    private int numMembri;
    @ManyToOne
    @JoinColumn(name = "utente_id", nullable = false)
    @JsonIgnoreProperties({"prenotazioni"})
    private UtenteNormale utente;
    private boolean pagata;
    private String codicePrenotazione;
    @PrePersist
    public void prePersist() {
        if (this.codicePrenotazione == null) {
            this.codicePrenotazione = UUID.randomUUID().toString();
        }
    }
}
