package it.epicode.simposiodermedallo.utenti.servizi.organizzatoreeventi.eventi.prenotazioni;

import it.epicode.simposiodermedallo.auth.AppUser;
import jakarta.mail.MessagingException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;

@RestController
@RequestMapping("/prenotazioni-eventi")
public class PrenotazioneEventoController {
    @Autowired
    private PrenotazioneService prenotazioneService;
    @PostMapping("/{id}")
    @PreAuthorize("hasAuthority('ROLE_USER')")
    public PrenotazioneEvento prenotaEvento(@RequestBody PrenotazioneEventoRequest request, @PathVariable Long id, @AuthenticationPrincipal AppUser user) throws MessagingException {
        return prenotazioneService.save(request, id, user);
    }
    @GetMapping("/{id}")
    @PreAuthorize("hasAuthority('ROLE_USER')")
    public PrenotazioneEvento getPrenotazione(@PathVariable Long id, @AuthenticationPrincipal AppUser user) {
        return prenotazioneService.getPrenotazione(id, user);
    }
    @GetMapping("")
    @PreAuthorize("hasAuthority('ROLE_USER')")
    public Page<PrenotazioneEvento> getPrenotazioni(@AuthenticationPrincipal AppUser user, @RequestParam(required = false) LocalDate data1, @RequestParam(required = false)LocalDate data2, @RequestParam(required = false) String artista, @RequestParam(required = false) Boolean soloFuturi, @RequestParam(required = false) String nomeParziale, @RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "10") int size, @RequestParam(defaultValue = "dataEvento") String sort, @RequestParam(defaultValue = "asc") String sortDir) {
        Sort sortOrder = sortDir.equalsIgnoreCase("desc")
                ? Sort.by(sort).descending()
                : Sort.by(sort).ascending();
        Pageable pageable = PageRequest.of(page, size , sortOrder);
        return prenotazioneService.getPrenotazioniUtenteFiltrate(user, new PrenotazioneFilter(data1, data2, artista, soloFuturi, nomeParziale), pageable);
    }


}
