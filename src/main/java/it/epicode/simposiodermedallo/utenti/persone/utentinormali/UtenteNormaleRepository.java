package it.epicode.simposiodermedallo.utenti.persone.utentinormali;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository
public interface UtenteNormaleRepository extends JpaRepository<UtenteNormale, Long> {
    Optional<UtenteNormale> findByAppUserId(Long appUserId);
}