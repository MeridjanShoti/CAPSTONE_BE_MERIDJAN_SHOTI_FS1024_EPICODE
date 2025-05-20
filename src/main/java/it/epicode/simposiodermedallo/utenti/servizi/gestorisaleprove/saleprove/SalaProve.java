package it.epicode.simposiodermedallo.utenti.servizi.gestorisaleprove.saleprove;

import it.epicode.simposiodermedallo.utenti.servizi.gestorisaleprove.GestoreSala;
import it.epicode.simposiodermedallo.utenti.servizi.gestorisaleprove.saleprove.prenotazioni.PrenotazioneSalaProve;
import it.epicode.simposiodermedallo.utenti.servizi.gestorisaleprove.saleprove.prenotazioni.slotprenotati.SlotPrenotati;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "sale_prove")
public class SalaProve {
    @Id
    @GeneratedValue (strategy = GenerationType.IDENTITY)
    private Long id;
    @OneToMany (mappedBy = "salaProve")
    private List<SlotPrenotati> slotPrenotati;
    @ManyToOne
    private GestoreSala gestoreSala;
    private String indirizzoSala;
    private String citta;
    private String nomeSala;
    private int capienzaMax;
    private double prezzoOrario;
    private List<String> fotoSala;
    @Column(columnDefinition = "TEXT")
    private String descrizione;
    @Column(columnDefinition = "TEXT")
    private String regolamento;
    @OneToMany
    private List<PrenotazioneSalaProve> prenotazioni;
    @Embedded
    private Strumentazione strumentazione;

}
