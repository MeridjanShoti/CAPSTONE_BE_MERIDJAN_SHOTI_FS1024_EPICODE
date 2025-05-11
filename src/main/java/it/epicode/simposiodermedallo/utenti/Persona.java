package it.epicode.simposiodermedallo.utenti;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@MappedSuperclass
@AllArgsConstructor
@NoArgsConstructor

public class Persona extends Utente{
    private String nome;
    private String cognome;
    private LocalDate dataNascita;
}