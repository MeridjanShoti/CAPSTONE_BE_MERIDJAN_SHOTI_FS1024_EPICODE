package it.epicode.simposiodermedallo.utenti.servizi.gestorisaleprove;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class GestoreSalaService {
    @Autowired
    private GestoreSalaRepository gestoreSalaRepository;
    public GestoreSala getGestoreSala(Long id) {
        return gestoreSalaRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("Gestore Sala non trovato"));
    }
}
