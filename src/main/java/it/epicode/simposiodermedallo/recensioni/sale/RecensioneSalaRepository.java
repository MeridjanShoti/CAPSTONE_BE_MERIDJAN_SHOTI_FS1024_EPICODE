package it.epicode.simposiodermedallo.recensioni.sale;


import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface RecensioneSalaRepository extends JpaRepository<RecensioneSala, Long> {
    Page<RecensioneSala> findAllBySalaProveId(Long id, Pageable pageable);
    List<RecensioneSala> findAllBySalaProveId(Long id);
    Boolean existsBySalaProveIdAndAutoreId(Long salaId, Long autoreId);
    List<RecensioneSala> findAllByAutoreId(Long autoreId);
}