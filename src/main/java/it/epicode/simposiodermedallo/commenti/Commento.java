package it.epicode.simposiodermedallo.commenti;

import it.epicode.simposiodermedallo.auth.AppUser;
import it.epicode.simposiodermedallo.utenti.persone.utentinormali.UtenteNormale;
import it.epicode.simposiodermedallo.utenti.servizi.organizzatoreeventi.eventi.Evento;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "Commenti")

public class Commento {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private  Long id;
    @ManyToOne (optional = false)
    private AppUser autore;
    @ManyToOne (optional = false)
    private Evento evento;
    @Column(columnDefinition = "TEXT", nullable = false)
    private String testo;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private boolean nascosto = false;
    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
    }
    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }
}