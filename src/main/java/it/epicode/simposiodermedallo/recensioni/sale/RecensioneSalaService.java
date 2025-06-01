package it.epicode.simposiodermedallo.recensioni.sale;
import it.epicode.simposiodermedallo.auth.AppUser;
import it.epicode.simposiodermedallo.auth.Role;
import it.epicode.simposiodermedallo.utenti.servizi.gestorisaleprove.saleprove.SalaProveRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import java.util.List;
import java.util.Optional;
import java.util.OptionalDouble;

@Service
@Validated
public class RecensioneSalaService {
    @Autowired
    private RecensioneSalaRepository recensioneSalaRepository;
    @Autowired
    private SalaProveRepository salaRepository;
    public RecensioneSala save(Long id, RecensioneSalaRequest request, AppUser appUser) {
        if(recensioneSalaRepository.existsBySalaProveIdAndAutoreId(id, appUser.getId())) {
            throw new IllegalArgumentException("Hai già lasciato una recensione per questa sala");
        }
        RecensioneSala recensioneSala = new RecensioneSala();
        recensioneSala.setSalaProve(salaRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("Sala non trovata")));
        recensioneSala.setTesto(request.getTesto());
        recensioneSala.setVoto(request.getVoto());
        recensioneSala.setAutore(appUser);
        return recensioneSalaRepository.save(recensioneSala);
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
    public Page<RecensioneSala> getAll(Long salaId, int page, int size, String sort, String sortDir) {
        Sort.Direction direction = sortDir.equalsIgnoreCase("desc") ? Sort.Direction.DESC : Sort.Direction.ASC;
        PageRequest pageRequest = PageRequest.of(page, size, Sort.by(direction, sort));
        return recensioneSalaRepository.findAllBySalaProveId(salaId, pageRequest);
    }
    public MediaRecensioniResponse calcolaMediaRecensioni(Long salaId) {
        List<RecensioneSala> recensioni = recensioneSalaRepository.findAllBySalaProveId(salaId);

        OptionalDouble media = recensioni.stream()
                .mapToInt(RecensioneSala::getVoto)
                .average();

        return new MediaRecensioniResponse(media.isPresent() ? Optional.of(media.getAsDouble()) : Optional.empty());
    }
}
