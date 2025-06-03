package it.epicode.simposiodermedallo.utenti.servizi.organizzatoreeventi.eventi.prenotazioni;


import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PrenotazioneEventoRepository extends JpaRepository<PrenotazioneEvento, Long> {
    List<PrenotazioneEvento> findAllByEventoId(Long id);
    Page<PrenotazioneEvento> findAll(Specification<PrenotazioneEvento> spec, Pageable pageable);
    List<PrenotazioneEvento> findAllByUtenteId(Long id);
}