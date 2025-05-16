package it.epicode.simposiodermedallo.utenti.persone.insegnanti;

import it.epicode.simposiodermedallo.utenti.persone.Persona;
import it.epicode.simposiodermedallo.utenti.servizi.scuole.Scuola;
import it.epicode.simposiodermedallo.utenti.servizi.scuole.corsi.Corso;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@EqualsAndHashCode(callSuper = true)
@Data
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "Insegnanti")

public class Insegnante extends Persona {
    private List<String> strumenti;
    @OneToMany (mappedBy = "insegnante")
    private List<Corso> corsi;
    @Column(columnDefinition = "TEXT")
    @Lob
    private byte[] curriculum;
    private double pagaOraria;
    @ManyToOne
    private Scuola scuola;

}