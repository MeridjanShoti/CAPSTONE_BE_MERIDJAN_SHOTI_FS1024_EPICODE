package it.epicode.simposiodermedallo.recensioni.scuole;

import it.epicode.simposiodermedallo.auth.AppUser;
import it.epicode.simposiodermedallo.auth.Role;
import it.epicode.simposiodermedallo.recensioni.sale.RecensioneSala;
import it.epicode.simposiodermedallo.recensioni.sale.RecensioneSalaRepository;
import it.epicode.simposiodermedallo.recensioni.sale.RecensioneSalaRequest;
import it.epicode.simposiodermedallo.utenti.servizi.gestorisaleprove.saleprove.SalaProveRepository;
import it.epicode.simposiodermedallo.utenti.servizi.scuole.ScuolaRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

@Service
@Validated
public class RecensioneScuolaService {
    @Autowired
    private RecensioneScuolaRepository recensioneScuolaRepository;
    @Autowired
    private ScuolaRepository scuolaRepository;
    public RecensioneScuola save(Long id, RecensioneScuolaRequest request, AppUser appUser) {
        if(recensioneScuolaRepository.existsByScuolaIdAndAutoreId(id, appUser.getId())) {
            throw new IllegalArgumentException("Hai già lasciato una recensione per questa scuola");
        }
        RecensioneScuola recensioneScuola = new RecensioneScuola();
        recensioneScuola.setScuola(scuolaRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("Scuola non trovata")));
        recensioneScuola.setTesto(request.getTesto());
        recensioneScuola.setVoto(request.getVoto());
        recensioneScuola.setAutore(appUser);
        return recensioneScuolaRepository.save(recensioneScuola);
    }
    public RecensioneScuola update(Long id, RecensioneScuolaRequest request, AppUser appUser) {
        RecensioneScuola recensioneScuola = recensioneScuolaRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("Recensione non trovata"));
        if  (!recensioneScuola.getAutore().getId().equals(appUser.getId())) {
            throw new IllegalArgumentException("Non sei l'autore della recensione");
        }
        recensioneScuola.setTesto(request.getTesto());
        recensioneScuola.setVoto(request.getVoto());
        return recensioneScuolaRepository.save(recensioneScuola);
    }
    public RecensioneScuola getRecensioneScuola(Long id) {
        return recensioneScuolaRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("Recensione non trovata"));
    }
    public void deleteRecensioneScuola(Long id, AppUser appUser) {
        RecensioneScuola recensioneScuola = recensioneScuolaRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("Recensione non trovata"));
        if (!recensioneScuola.getAutore().getId().equals(appUser.getId()) && !appUser.getRoles().contains(Role.ROLE_ADMIN)) {
            throw new IllegalArgumentException("Non sei l'autore della recensione");
        }
        recensioneScuolaRepository.delete(recensioneScuola);
    }
    public Page<RecensioneScuola> getAll(Long scuolaId, int page, int size, String sort, String sortDir) {
        Sort.Direction direction = sortDir.equalsIgnoreCase("desc") ? Sort.Direction.DESC : Sort.Direction.ASC;
        PageRequest pageRequest = PageRequest.of(page, size, Sort.by(direction, sort));
        return recensioneScuolaRepository.findAllByScuolaId(scuolaId, pageRequest);
    }
}

