package it.epicode.simposiodermedallo.utenti.servizi.scuole.corsi;

import it.epicode.simposiodermedallo.auth.AppUser;
import it.epicode.simposiodermedallo.common.CommonResponse;
import it.epicode.simposiodermedallo.utenti.servizi.scuole.corsi.enums.Livello;
import it.epicode.simposiodermedallo.utenti.servizi.scuole.corsi.enums.StatoCorso;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/corsi")
public class CorsoController {
    @Autowired
    private CorsoService corsoService;

    @PostMapping
    @PreAuthorize("hasRole('ROLE_SCUOLA')")
    @ResponseStatus(HttpStatus.CREATED)
    public Corso createCorso(@RequestBody CorsoRequest request, @AuthenticationPrincipal AppUser user) {
        return corsoService.save(request, user);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ROLE_SCUOLA')")
    @ResponseStatus(HttpStatus.OK)
    public Corso updateCorso(@RequestBody CorsoRequest request, @PathVariable Long id, @AuthenticationPrincipal AppUser user) {
        return corsoService.update(request, id, user);
    }

    @GetMapping("/complete/{id}")
    @PreAuthorize("isAuthenticated()")
    @ResponseStatus(HttpStatus.OK)
    public Corso getCorsoComplete(@PathVariable Long id, @AuthenticationPrincipal AppUser user) {
        return corsoService.findByIdCorso(id, user);
    }

    @GetMapping("/{id}")
    @PreAuthorize("isAuthenticated()")
    @ResponseStatus(HttpStatus.OK)
    public CorsoResponse getCorso(@PathVariable Long id) {
        return corsoService.findByIdNoLink(id);
    }

    @GetMapping("miei-corsi")
    @PreAuthorize("isAuthenticated()")
    @ResponseStatus(HttpStatus.OK)
    public Page<Corso> getMieiCorsi(@RequestParam(defaultValue = "0") int page,
                                    @RequestParam(defaultValue = "10") int size,
                                    @RequestParam(defaultValue = "dataInizio") String sortBy,
                                    @RequestParam(defaultValue = "asc") String sortDir,
                                    @AuthenticationPrincipal AppUser user,
                                    @RequestParam(required = false) String nomeCorso,
                                    @RequestParam(required = false) Livello livello,
                                    @RequestParam(required = false) LocalDate dataInizio,
                                    @RequestParam(required = false) LocalDate dataFine,
                                    @RequestParam(required = false) Integer giorniASettimana,
                                    @RequestParam(required = false) StatoCorso statoCorso,
                                    @RequestParam(required = false) String strumenti,
                                    @RequestParam(required = false) Double costo
    ) {
        Sort sort = sortDir.equalsIgnoreCase("desc")
                ? Sort.by(sortBy).descending()
                : Sort.by(sortBy).ascending();
        return corsoService.getAllCorsiByUser(page, size, nomeCorso, livello, giorniASettimana, costo, strumenti, dataInizio, dataFine, statoCorso, sort, user);
    }

    @GetMapping("")
    @PreAuthorize("isAuthenticated()")
    @ResponseStatus(HttpStatus.OK)
    public Page<CorsoResponse> getAllCorsi(@RequestParam(defaultValue = "0") int page,
                                           @RequestParam(defaultValue = "10") int size,
                                           @RequestParam(defaultValue = "dataInizio") String sortBy,
                                           @RequestParam(defaultValue = "asc") String sortDir,
                                           @RequestParam(required = false) String nomeCorso,
                                           @RequestParam(required = false) Livello livello,
                                           @RequestParam(required = false) LocalDate dataInizio,
                                           @RequestParam(required = false) LocalDate dataFine,
                                           @RequestParam(required = false) Integer giorniASettimana,
                                           @RequestParam(required = false) String strumenti,
                                           @RequestParam(required = false) Double costo
    ) {
        Sort sort = sortDir.equalsIgnoreCase("desc")
                ? Sort.by(sortBy).descending()
                : Sort.by(sortBy).ascending();
        return corsoService.getAllCorsi(page, size, nomeCorso, livello, giorniASettimana, costo, strumenti, dataInizio, dataFine, sort);
    }
    @GetMapping("/{id}/date-lezione")
    public List<LocalDate> getDateLezione(@PathVariable Long id) {
        return corsoService.getGiorniLezione(id);
    }
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('SCUOLA')")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteCorso(@PathVariable Long id, @AuthenticationPrincipal AppUser user) {
        corsoService.delete(id, user);
    }
    @PatchMapping("/assegna-insegnante/{idCorso}/{idInsegnante}")
    @PreAuthorize("hasRole('SCUOLA')")
    @ResponseStatus(HttpStatus.OK)
    public CommonResponse assegnaInsegnante(@PathVariable Long idCorso, @PathVariable Long idInsegnante, @AuthenticationPrincipal AppUser user) {
        return corsoService.assegnaInsegnante(idCorso, idInsegnante ,user);
    }
    @Transactional(readOnly = true)
    @GetMapping("/insegnante")
    @PreAuthorize("hasRole('INSEGNANTE')")
    @ResponseStatus(HttpStatus.OK)
    public List<Corso> getCorsiByInsegnante(@AuthenticationPrincipal AppUser user) {
        return corsoService.getCorsiByInsegnante(user);
    }
}
