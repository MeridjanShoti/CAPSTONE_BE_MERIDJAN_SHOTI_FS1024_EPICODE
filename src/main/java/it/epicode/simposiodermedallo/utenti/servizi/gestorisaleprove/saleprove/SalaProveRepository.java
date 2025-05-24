package it.epicode.simposiodermedallo.utenti.servizi.gestorisaleprove.saleprove;


import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SalaProveRepository extends JpaRepository<SalaProve, Long> {
Page<SalaProve> findAll(Specification<SalaProve> spec, Pageable pageable);
Page<SalaProve> findAllByGestoreSalaId(Long id, Pageable pageable);
}