package it.epicode.simposiodermedallo.utenti.servizi.organizzatoreeventi.eventi;

import it.epicode.simposiodermedallo.auth.AppUser;
import it.epicode.simposiodermedallo.common.CommonResponse;
import jakarta.mail.MessagingException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/eventi")
public class EventoController {
    @Autowired
    private EventoService eventoService;
    @PostMapping("")
    @PreAuthorize("isAuthenticated()")
    public CommonResponse createEvento(@RequestBody EventoRequest eventoRequest, @AuthenticationPrincipal AppUser user) {
        Evento evento = eventoService.creaEvento(eventoRequest , user);
        return new CommonResponse(evento.getId());
    }
    @PutMapping("/{id}")
    @PreAuthorize("isAuthenticated()")
    public CommonResponse updateEvento(@RequestBody EventoRequest eventoRequest, @AuthenticationPrincipal AppUser user, @PathVariable Long id) throws MessagingException {
        Evento evento = eventoService.modificaEvento(id, eventoRequest, user);
        return new CommonResponse(evento.getId());
    }
    @GetMapping("/{id}")
    @PreAuthorize("isAuthenticated()")
    public Evento getEvento(@PathVariable Long id) {
        return eventoService.getEvento(id);
    }
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ROLE_ADMIN') or hasRole('ROLE_ORGANIZZATORE')")
    public void deleteEvento(@PathVariable Long id, @AuthenticationPrincipal AppUser user) {
        eventoService.deleteEvento(id, user);
    }
    @GetMapping("/")
    @PreAuthorize("isAuthenticated()")
    public Page<Evento> getEventi(@RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "10") int size, @RequestParam(defaultValue = "data") String sort, @AuthenticationPrincipal AppUser user) {
        return eventoService.getEventi( page, size, sort);
    }
    @GetMapping("/my-events")
    @PreAuthorize("isAuthenticated()")
    public Page<Evento> getEventiByOrganizzatore( @AuthenticationPrincipal AppUser user,@RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "10") int size, @RequestParam(defaultValue = "id") String sort) {
        return eventoService.getEventiByOrganizzatore(user, page, size, sort);
    }
}

