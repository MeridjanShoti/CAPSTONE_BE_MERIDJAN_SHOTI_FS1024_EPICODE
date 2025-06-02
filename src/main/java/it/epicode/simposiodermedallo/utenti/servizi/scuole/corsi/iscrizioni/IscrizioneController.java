package it.epicode.simposiodermedallo.utenti.servizi.scuole.corsi.iscrizioni;

import it.epicode.simposiodermedallo.auth.AppUser;
import it.epicode.simposiodermedallo.common.CommonResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/iscrizioni")
public class IscrizioneController {
    @Autowired
    private IscrizioneService iscrizioneService;
    @PostMapping("/{id}")
    @PreAuthorize("hasRole('ROLE_USER')")
    public Iscrizione iscriviti(@PathVariable Long id, @AuthenticationPrincipal AppUser user) {
        return iscrizioneService.save(id, user);
    }
    @PatchMapping("/presenze/{idCorso}/{idUtente}")
    public Iscrizione takePresenze(@PathVariable Long idCorso, @PathVariable Long idUtente, @RequestBody PresenzaRequest isPresente, @AuthenticationPrincipal AppUser user) {
        return iscrizioneService.takePresenze(idCorso, idUtente, isPresente.getIsPresente(), user);
    }
    @PatchMapping("/voto/{idCorso}/{idUtente}")
    public Iscrizione assegnaVoto(@PathVariable Long idCorso, @PathVariable Long idUtente, @RequestBody VotoRequest voto, @AuthenticationPrincipal AppUser user) {
        return iscrizioneService.assegnaVoto(idCorso, idUtente, voto.getValutazione(), user);
    }
    @DeleteMapping("/{id}")
    @PreAuthorize("isAuthenticated()")
    public CommonResponse delete(@PathVariable Long id, @AuthenticationPrincipal AppUser user) {
        return iscrizioneService.delete(id, user);
    }
    @GetMapping("")
    @PreAuthorize("isAuthenticated()")
    public Page<Iscrizione> getAllIscrizioniByUtente(@RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "10") int size, @AuthenticationPrincipal AppUser user) {
        return iscrizioneService.getAllIscrizioniByUtente(page, size, user);
    }
    @GetMapping("/scuole/{id}")
    @PreAuthorize("isAuthenticated()")
    public Page<Iscrizione> getAllIscrizioniByCorso(@RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "10") int size, @AuthenticationPrincipal AppUser user, @PathVariable Long id) {
        return iscrizioneService.getAllIscrizioniByCorso(page, size, id, user);
    }
    @GetMapping("/{id}")
    @PreAuthorize("isAuthenticated()")
    public Iscrizione getIscrizione(@PathVariable Long id, @AuthenticationPrincipal AppUser user) {
        return iscrizioneService.getIscrizioneById(id, user);
    }
    @GetMapping("/corso/{id}")
    @PreAuthorize("isAuthenticated()")
    public Iscrizione getIscrizioneByUtenteAndCorso(@PathVariable Long id, @AuthenticationPrincipal AppUser user) {
        return iscrizioneService.getIscrizioneByUtenteAndCorso(id, user);
    }
}
