package it.epicode.simposiodermedallo.utenti.servizi.organizzatoreeventi.eventi;


import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface EventoRepository extends JpaRepository<Evento, Long> {
    Page<Evento> findByOrganizzatoreId(Long organizzatoreId, Pageable pageable);
    Page<Evento> findAll(Specification<Evento> spec, Pageable pageable);
    @Query("SELECT e FROM Evento e LEFT JOIN FETCH e.partecipanti WHERE e.id = :id")
    Optional<Evento> findByIdWithPartecipanti(@Param("id") Long id);
}