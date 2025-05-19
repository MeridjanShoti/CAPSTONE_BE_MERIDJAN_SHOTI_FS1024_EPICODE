package it.epicode.simposiodermedallo.utenti.servizi.organizzatoreeventi.eventi;


import org.springframework.data.jpa.repository.JpaRepository;

public interface EventoRepository extends JpaRepository<Evento, Long> {
}