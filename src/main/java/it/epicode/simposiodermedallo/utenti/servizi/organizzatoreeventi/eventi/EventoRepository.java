package it.epicode.simposiodermedallo.utenti.servizi.organizzatoreeventi.eventi;


import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EventoRepository extends JpaRepository<Evento, Long> {
    Page<Evento> findByOrganizzatoreId(Long organizzatoreId, Pageable pageable);
    Page<Evento> findAll(Specification<Evento> spec, Pageable pageable);
}