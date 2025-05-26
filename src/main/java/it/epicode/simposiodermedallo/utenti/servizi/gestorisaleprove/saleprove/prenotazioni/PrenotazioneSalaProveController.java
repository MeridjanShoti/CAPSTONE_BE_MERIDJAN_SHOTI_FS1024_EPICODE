package it.epicode.simposiodermedallo.utenti.servizi.gestorisaleprove.saleprove.prenotazioni;

import it.epicode.simposiodermedallo.auth.AppUser;
import it.epicode.simposiodermedallo.utenti.servizi.gestorisaleprove.saleprove.prenotazioni.slot.SlotDisponibile;
import jakarta.mail.MessagingException;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/prenotazioni-sala-prove")
public class PrenotazioneSalaProveController {
    @Autowired
    private PrenotazioneSalaProveService prenotazioneSalaProveService;

    @GetMapping("/gestore")
    @PreAuthorize("isAuthenticated()")
    @ResponseStatus(HttpStatus.OK)
    public Page<PrenotazioneSalaProve> getPrenotazioneSalaProve(@AuthenticationPrincipal AppUser user, @RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "10") int size, @RequestParam(defaultValue = "id") String sort, @RequestParam(defaultValue = "asc") String sortDir, @RequestParam(required=false) Long id, @RequestParam(required=false) String nomeSala, @RequestParam(required=false) LocalDate data1, @RequestParam(required=false) LocalDate data2, @RequestParam(required=false) Boolean soloFuturi) {
        Sort sortOrder = sortDir.equalsIgnoreCase("desc")
                ? Sort.by(sort).descending()
                : Sort.by(sort).ascending();
        Pageable pageable = PageRequest.of(page, size , sortOrder);
        return prenotazioneSalaProveService.getPrenotazioniGestore(user, new PrenotazioneSalaFilter(id, nomeSala, data1, data2, soloFuturi), pageable);
    }
    @GetMapping("")
    @PreAuthorize("isAuthenticated()")
    @ResponseStatus(HttpStatus.OK)
    public Page<PrenotazioneSalaProve> getPrenotazioneUtente(@AuthenticationPrincipal AppUser user, @RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "10") int size, @RequestParam(defaultValue = "id") String sort, @RequestParam(defaultValue = "asc") String sortDir, @RequestParam(required=false) Long id, @RequestParam(required=false) String nomeSala, @RequestParam(required=false) LocalDate data1, @RequestParam(required=false) LocalDate data2, @RequestParam(required=false) Boolean soloFuturi) {
        Sort sortOrder = sortDir.equalsIgnoreCase("desc")
                ? Sort.by(sort).descending()
                : Sort.by(sort).ascending();
        Pageable pageable = PageRequest.of(page, size , sortOrder);
        return prenotazioneSalaProveService.getPrenotazioniUtente(user, new PrenotazioneSalaFilter(id, nomeSala, data1, data2, soloFuturi), pageable);
    }
    @GetMapping("/{id}")
    @PreAuthorize("isAuthenticated()")
    @ResponseStatus(HttpStatus.OK)
    public PrenotazioneSalaProve getPrenotazione(@PathVariable Long id, @AuthenticationPrincipal AppUser user) {
        return prenotazioneSalaProveService.getPrenotazioneById(id, user);
    }
    @PostMapping("/{id}")
    @ResponseStatus(HttpStatus.CREATED)
    public PrenotazioneSalaProve prenotaSalaProve(@Valid @RequestBody PrenotazioneSalaRequest request, @PathVariable Long id, @AuthenticationPrincipal AppUser user) {
        return prenotazioneSalaProveService.creaPrenotazione(request, user, id);
    }
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deletePrenotazione(@PathVariable Long id, @AuthenticationPrincipal AppUser user) throws MessagingException {
        prenotazioneSalaProveService.deletePrenotazione(id, user);
    }
    @PutMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public PrenotazioneSalaProve updatePrenotazione(@RequestBody PrenotazioneSalaRequest request, @PathVariable Long id, @AuthenticationPrincipal AppUser user) {
        return prenotazioneSalaProveService.updatePrenotazione(id, request, user);
    }
    @GetMapping("/disponibilita/{id}")
    @PreAuthorize("isAuthenticated()")
    @ResponseStatus(HttpStatus.OK)
    public List<SlotDisponibile> getDisponibilita(
            @PathVariable Long id,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate giorno
    ) {
        return prenotazioneSalaProveService.getDisponibilita(id, giorno);
    }
}
