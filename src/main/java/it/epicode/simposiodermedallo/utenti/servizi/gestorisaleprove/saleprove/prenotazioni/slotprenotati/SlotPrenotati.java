package it.epicode.simposiodermedallo.utenti.servizi.gestorisaleprove.saleprove.prenotazioni.slotprenotati;

import it.epicode.simposiodermedallo.utenti.servizi.gestorisaleprove.saleprove.SalaProve;
import it.epicode.simposiodermedallo.utenti.servizi.gestorisaleprove.saleprove.prenotazioni.PrenotazioneSalaProve;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Data
public class SlotPrenotati {
    @Id
    @GeneratedValue (strategy = GenerationType.IDENTITY)
    private Long id;
    private LocalDateTime inizio;
    private LocalDateTime fine;
    @ManyToOne
    private SalaProve salaProve;
}
