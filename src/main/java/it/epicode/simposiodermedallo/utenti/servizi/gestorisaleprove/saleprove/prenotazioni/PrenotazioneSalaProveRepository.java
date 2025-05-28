package it.epicode.simposiodermedallo.utenti.servizi.gestorisaleprove.saleprove.prenotazioni;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.time.LocalDateTime;
import java.util.List;
@Repository
public interface PrenotazioneSalaProveRepository extends JpaRepository<PrenotazioneSalaProve, Long> {
    Page<PrenotazioneSalaProve> findAll(Specification<PrenotazioneSalaProve> spec, Pageable pageable);
    @Query("""
    SELECT p FROM PrenotazioneSalaProve p
    WHERE p.salaProve.id = :salaId
    AND (
        (p.inizio < :fine AND p.fine > :inizio)
    )
""")
    List<PrenotazioneSalaProve> findPrenotazioniConflittuali(
            @Param("salaId") Long salaId,
            @Param("inizio") LocalDateTime inizio,
            @Param("fine") LocalDateTime fine
    );
    boolean existsBySalaProveId(Long id);
    List<PrenotazioneSalaProve> findAllBySalaProveId(Long id);
}