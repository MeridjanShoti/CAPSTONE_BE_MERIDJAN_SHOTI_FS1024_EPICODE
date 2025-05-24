package it.epicode.simposiodermedallo.utenti.servizi.scuole;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ScuolaService {
        @Autowired
        private ScuolaRepository scuolaRepository;
        public Scuola getScuola(Long id) {
            return scuolaRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("Scuola non trovata"));
        }
    }

