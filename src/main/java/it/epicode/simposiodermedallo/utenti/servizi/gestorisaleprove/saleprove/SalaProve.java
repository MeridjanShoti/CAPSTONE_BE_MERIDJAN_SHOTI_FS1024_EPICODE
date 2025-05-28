package it.epicode.simposiodermedallo.utenti.servizi.gestorisaleprove.saleprove;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import it.epicode.simposiodermedallo.utenti.servizi.gestorisaleprove.GestoreSala;
import it.epicode.simposiodermedallo.utenti.servizi.gestorisaleprove.saleprove.prenotazioni.PrenotazioneSalaProve;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.DayOfWeek;
import java.time.LocalTime;
import java.util.List;
import java.util.Set;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "sale_prove")
public class SalaProve {
    @Id
    @GeneratedValue (strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne
    @JsonIgnoreProperties({"saleProve"})
    private GestoreSala gestoreSala;
    private String indirizzoSala;
    private String citta;
    private String nomeSala;
    private int capienzaMax;
    private double prezzoOrario;
    private String copertinaSala;
    @ElementCollection(targetClass = DayOfWeek.class)
    @Enumerated(EnumType.STRING)
    private Set<DayOfWeek> giorniApertura;
    private LocalTime orarioApertura;
    private LocalTime orarioChiusura;
    @Column(columnDefinition = "TEXT")
    private String descrizione;
    @Column(columnDefinition = "TEXT")
    private String regolamento;
    @Embedded
    private Strumentazione strumentazione;

}
