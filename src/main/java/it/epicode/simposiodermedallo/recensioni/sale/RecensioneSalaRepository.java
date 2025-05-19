package it.epicode.simposiodermedallo.recensioni.sale;


import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RecensioneSalaRepository extends JpaRepository<RecensioneSala, Long> {
    Page<RecensioneSala> findAllBySalaProveId(Long id, Pageable pageable);
}