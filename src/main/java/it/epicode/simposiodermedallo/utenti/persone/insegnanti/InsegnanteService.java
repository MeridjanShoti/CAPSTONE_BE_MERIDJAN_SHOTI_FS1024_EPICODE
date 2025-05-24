package it.epicode.simposiodermedallo.utenti.persone.insegnanti;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class InsegnanteService {
    @Autowired
    private InsegnanteRepository insegnanteRepository;

    public InsegnanteResponse getInsegnante(Long id) {
        Insegnante insegnante = insegnanteRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("Insegnante non trovato"));
        InsegnanteResponse response = new InsegnanteResponse();
        BeanUtils.copyProperties(insegnante, response);
        return response;
    }
}