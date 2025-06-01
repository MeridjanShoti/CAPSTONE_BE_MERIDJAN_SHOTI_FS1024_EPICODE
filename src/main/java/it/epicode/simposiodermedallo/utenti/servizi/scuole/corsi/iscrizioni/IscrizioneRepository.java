package it.epicode.simposiodermedallo.utenti.servizi.scuole.corsi.iscrizioni;


import org.springframework.data.jpa.repository.JpaRepository;

public interface IscrizioneRepository extends JpaRepository<Iscrizione, Long> {
boolean existsByUtenteIdAndCorsoId(Long utenteId, Long corsoId);
}