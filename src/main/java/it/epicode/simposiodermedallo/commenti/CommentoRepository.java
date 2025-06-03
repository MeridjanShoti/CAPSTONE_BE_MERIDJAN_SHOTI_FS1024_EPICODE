package it.epicode.simposiodermedallo.commenti;


import it.epicode.simposiodermedallo.utenti.servizi.organizzatoreeventi.eventi.Evento;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface CommentoRepository extends JpaRepository<Commento, Long> {
    Page<Commento> findAllByEventoId(Long id, Pageable pageable);
    List<Commento> findAllByEventoId(Long eventoId);
    List<Commento> findAllByAutoreId(Long autoreId);
}