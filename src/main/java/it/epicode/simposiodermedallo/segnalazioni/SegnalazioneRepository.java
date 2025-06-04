package it.epicode.simposiodermedallo.segnalazioni;


import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SegnalazioneRepository extends JpaRepository<Segnalazione, Long> {
    Page<Segnalazione> findAll(Specification<Segnalazione> spec, Pageable pageable);
    List<Segnalazione> findAllByAutoreId(Long autoreId);
    List<Segnalazione> findAllByTipoSegnalazioneAndIdElemento(TipoSegnalazione tipoSegnalazione, Long idElemento);
}