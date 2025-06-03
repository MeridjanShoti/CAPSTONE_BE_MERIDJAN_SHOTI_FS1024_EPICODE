package it.epicode.simposiodermedallo.utenti.servizi.scuole.corsi.iscrizioni;


import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface IscrizioneRepository extends JpaRepository<Iscrizione, Long> {
boolean existsByUtenteIdAndCorsoId(Long utenteId, Long corsoId);
Iscrizione findByUtenteIdAndCorsoId(Long utenteId, Long corsoId);
Page<Iscrizione> findAllByCorsoId(Long corsoId, Pageable pageable);
List<Iscrizione> findAllByCorsoId(Long corsoId);
Page<Iscrizione> findAllByUtenteId(Long utenteId, Pageable pageable);
List<Iscrizione> findAllByUtenteId(Long utenteId);
}