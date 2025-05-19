package it.epicode.simposiodermedallo.recensioni.scuole;

import it.epicode.simposiodermedallo.auth.AppUser;
import it.epicode.simposiodermedallo.utenti.persone.utentinormali.UtenteNormale;
import it.epicode.simposiodermedallo.utenti.servizi.Servizio;
import it.epicode.simposiodermedallo.utenti.servizi.scuole.Scuola;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "RecensioniScuole")

public class RecensioneScuola {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne(optional = false)
    private AppUser autore;
    @ManyToOne(optional = false)
    private Scuola scuola;
    @Column(columnDefinition = "TEXT")
    private String testo;
    @Column(nullable = false)
    private int voto;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
    }
    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }
}