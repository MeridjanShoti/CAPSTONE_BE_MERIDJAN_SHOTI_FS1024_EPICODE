package it.epicode.simposiodermedallo.utenti.persone.insegnanti;

import it.epicode.simposiodermedallo.auth.AppUser;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class InsegnanteService {
    @Autowired
    private InsegnanteRepository insegnanteRepository;
    @Transactional(readOnly = true)
    public InsegnanteResponse getInsegnante(Long id) {
        Insegnante insegnante = insegnanteRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("Insegnante non trovato"));
        InsegnanteResponse response = new InsegnanteResponse();
        BeanUtils.copyProperties(insegnante, response);
        return response;
    }
    @Transactional(readOnly = true)
    public Insegnante getInsegnanteComplete(Long id) {
        return insegnanteRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("Insegnante non trovato"));
    }
    @Transactional(readOnly = true)
    public List<Insegnante> getAllInsegnanti(AppUser user) {
        return insegnanteRepository.findAllByScuolaId(user.getId());
    }
    @Transactional(readOnly = true)
    public Page<Insegnante> getAllInsegnantiPage(AppUser user, int page, int size, String sort) {
        return insegnanteRepository.findAllByScuolaId(user.getId(), PageRequest.of(page, size, Sort.by(sort)));
    }
}