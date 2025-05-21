package it.epicode.simposiodermedallo.utenti;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import it.epicode.simposiodermedallo.auth.AppUser;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
@Data
@AllArgsConstructor
@NoArgsConstructor
@MappedSuperclass
public abstract class Utente {
    @Id
    private Long id;
    private String email;
    @Column(columnDefinition = "TEXT")
    private String bio;
    private String avatar;
    private String copertina;
    private LocalDate dataRegistrazione;
    @OneToOne(cascade={CascadeType.REMOVE, CascadeType.PERSIST})
    @MapsId
    private AppUser appUser;
}
