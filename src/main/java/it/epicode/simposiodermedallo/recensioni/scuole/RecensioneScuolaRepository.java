package it.epicode.simposiodermedallo.recensioni.scuole;


import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RecensioneScuolaRepository extends JpaRepository<RecensioneScuola, Long> {
    Page<RecensioneScuola> findAllByScuolaId(Long id, Pageable pageable);
}