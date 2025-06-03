package it.epicode.simposiodermedallo.recensioni.scuole;


import it.epicode.simposiodermedallo.recensioni.sale.RecensioneSala;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface RecensioneScuolaRepository extends JpaRepository<RecensioneScuola, Long> {
    Page<RecensioneScuola> findAllByScuolaId(Long id, Pageable pageable);
    List<RecensioneScuola> findAllByScuolaId(Long id);
    Boolean existsByScuolaIdAndAutoreId(Long salaId, Long autoreId);
    List<RecensioneScuola> findAllByAutoreId(Long autoreId);
}