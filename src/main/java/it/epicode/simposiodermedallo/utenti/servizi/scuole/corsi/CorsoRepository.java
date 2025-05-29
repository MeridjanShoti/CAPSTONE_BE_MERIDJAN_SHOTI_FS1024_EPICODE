package it.epicode.simposiodermedallo.utenti.servizi.scuole.corsi;


import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;

public interface CorsoRepository extends JpaRepository<Corso, Long> {
    @Query("SELECT c FROM Corso c WHERE (c.dataInizio = :oggi AND c.statoCorso = 'IN_PROGRAMMA') OR (c.dataFine = :oggi AND c.statoCorso = 'IN_CORSO')")
    List<Corso> findCorsiDaAggiornare(@Param("oggi") LocalDate oggi);
    boolean existsByIdAndPartecipantiId(Long corsoId, Long utenteId);
    Page<Corso> findAll(Specification<Corso> spec, Pageable pageable);
}