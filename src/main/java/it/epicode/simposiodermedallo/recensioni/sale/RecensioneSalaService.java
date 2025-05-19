package it.epicode.simposiodermedallo.recensioni.sale;
import it.epicode.simposiodermedallo.auth.AppUser;
import it.epicode.simposiodermedallo.auth.Role;
import it.epicode.simposiodermedallo.utenti.servizi.gestorisaleprove.saleprove.SalaProveRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

@Service
@Validated
public class RecensioneSalaService {
    @Autowired
    private RecensioneSalaRepository recensioneSalaRepository;
    @Autowired
    private SalaProveRepository salaRepository;
    public RecensioneSala save(Long id, RecensioneSalaRequest request, AppUser appUser) {
        RecensioneSala recensioneSala = new RecensioneSala();
        recensioneSala.setSalaProve(salaRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("Sala non trovata")));
        recensioneSala.setTesto(request.getTesto());
        recensioneSala.setVoto(request.getVoto());
        recensioneSala.setAutore(appUser);
        return recensioneSala;
    }
    public RecensioneSala update(Long id, RecensioneSalaRequest request, AppUser appUser) {
        RecensioneSala recensioneSala = recensioneSalaRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("Recensione non trovata"));
        if  (!recensioneSala.getAutore().getId().equals(appUser.getId())) {
            throw new IllegalArgumentException("Non sei l'autore della recensione");
        }
        recensioneSala.setTesto(request.getTesto());
        recensioneSala.setVoto(request.getVoto());
        return recensioneSalaRepository.save(recensioneSala);
    }
    public RecensioneSala getRecensioneSala(Long id) {
        return recensioneSalaRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("Recensione non trovata"));
    }
    public void deleteRecensioneSala(Long id, AppUser appUser) {
        RecensioneSala recensioneSala = recensioneSalaRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("Recensione non trovata"));
        if (!recensioneSala.getAutore().getId().equals(appUser.getId()) && !appUser.getRoles().contains(Role.ROLE_ADMIN)) {
            throw new IllegalArgumentException("Non sei l'autore della recensione");
        }
        recensioneSalaRepository.delete(recensioneSala);
    }
    public Page<RecensioneSala> getAll(Long salaId, int page, int size) {
        return recensioneSalaRepository.findAllBySalaProveId(salaId, PageRequest.of(page, size));
    }
}
