package it.epicode.simposiodermedallo.utenti.persone.insegnanti;


import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface InsegnanteRepository extends JpaRepository<Insegnante, Long> {
    Page<Insegnante> findAllByScuolaId(Long id, Pageable pageable);
    List<Insegnante> findAllByScuolaId(Long id);
    Boolean existsByScuolaIdAndId(Long scuolaId, Long id);
}