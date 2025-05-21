package it.epicode.simposiodermedallo.commenti;

import com.github.javafaker.App;
import it.epicode.simposiodermedallo.auth.AppUser;
import it.epicode.simposiodermedallo.auth.AppUserRepository;
import it.epicode.simposiodermedallo.auth.Role;
import it.epicode.simposiodermedallo.common.CommonResponse;
import it.epicode.simposiodermedallo.utenti.servizi.organizzatoreeventi.eventi.EventoRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

@Service
@Validated
public class CommentoService {
    @Autowired
    private CommentoRepository commentoRepository;
    @Autowired
    private AppUserRepository appUserRepository;
    @Autowired
    private EventoRepository eventoRepository;
    public Commento save(Long id, CommentoRequest request, AppUser user) {
        Commento commento = new Commento();
        commento.setTesto(request.getTesto());
        commento.setAutore(user);
        commento.setEvento(eventoRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("Evento non trovato")));
        return commentoRepository.save(commento);
    }
    public Commento update(Long id, CommentoRequest request, AppUser user) {
        Commento commento = commentoRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("Commento non trovato"));
        if (!user.getId().equals(commento.getAutore().getId())) {
            throw new RuntimeException("Non sei l'autore del commento");
        }
        commento.setTesto(request.getTesto());
        return commentoRepository.save(commento);
    }
    public CommonResponse delete(Long id, AppUser user) {
        Commento commento = commentoRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("Commento non trovato"));
        if (!user.getId().equals(commento.getAutore().getId()) && !user.getRoles().contains(Role.ROLE_ADMIN)) {
            throw new RuntimeException("Non sei l'autore del commento");
        }
        commentoRepository.delete(commento);
        return new CommonResponse(id);
    }
    public Commento getById(Long id) {
        return commentoRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("Commento non trovato"));
    }
    public Page<Commento> getAllByEvento(Long eventoId, int page, int size, String sort) {
        return commentoRepository.findAllByEventoId(eventoId, org.springframework.data.domain.PageRequest.of(page, size));
    }
}


