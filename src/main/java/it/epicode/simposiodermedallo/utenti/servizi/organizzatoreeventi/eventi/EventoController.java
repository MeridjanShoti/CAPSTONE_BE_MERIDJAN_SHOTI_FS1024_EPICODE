package it.epicode.simposiodermedallo.utenti.servizi.organizzatoreeventi.eventi;

import it.epicode.simposiodermedallo.auth.AppUser;
import it.epicode.simposiodermedallo.common.CommonResponse;
import jakarta.mail.MessagingException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;

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
    public Page<Evento> getEventi(@RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "10") int size, @RequestParam(defaultValue = "data") String sort, @AuthenticationPrincipal AppUser user, @RequestParam(required = false) String citta, @RequestParam(required = false) TipoEvento tipoEvento, @RequestParam(required = false) String nomeParziale, @RequestParam(required = false) LocalDate data1, @RequestParam(required = false) LocalDate data2, @RequestParam(required = false) String artista, @RequestParam(required = false) Boolean soloFuturi) {
        EventoFilter filter = new EventoFilter();
        filter.setCitta( citta);
        filter.setTipoEvento( tipoEvento);
        filter.setNomeParziale( nomeParziale);
        filter.setData1( data1);
        filter.setData2( data2);
        filter.setArtista( artista);
        filter.setSoloFuturi( soloFuturi);
        return eventoService.getEventi( page, size, sort, filter);
    }
    @GetMapping("/my-events")
    @PreAuthorize("isAuthenticated()")
    public Page<Evento> getEventiByOrganizzatore( @AuthenticationPrincipal AppUser user,@RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "10") int size, @RequestParam(defaultValue = "id") String sort) {
        return eventoService.getEventiByOrganizzatore(user, page, size, sort);
    }
}

