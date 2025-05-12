package it.epicode.simposiodermedallo.utenti.persone.utentinormali;

import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service

public class UtenteNormaleService {
    @Autowired
    UtenteNormaleRepository utenteNormaleRepository;
    public UtenteNormale save(UtenteNormale utenteNormale) {
        return utenteNormaleRepository.save(utenteNormale);
    }
    public UtenteNormale findById(long id) {
        return utenteNormaleRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("Utente non trovato"));
    }
    public void deleteById(long id) {
        utenteNormaleRepository.deleteById(id);
    }


}
