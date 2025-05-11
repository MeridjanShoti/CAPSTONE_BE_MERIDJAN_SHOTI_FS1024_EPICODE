package it.epicode.simposiodermedallo.utenti.persone;

import it.epicode.simposiodermedallo.utenti.Utente;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@EqualsAndHashCode(callSuper = true)
@Data
@MappedSuperclass
@AllArgsConstructor
@NoArgsConstructor

public class Persona extends Utente {
    private String nome;
    private String cognome;
    private LocalDate dataNascita;
}