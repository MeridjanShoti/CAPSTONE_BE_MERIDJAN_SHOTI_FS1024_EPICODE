package it.epicode.simposiodermedallo.segnalazioni;

import it.epicode.simposiodermedallo.auth.AppUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/segnalazioni")
public class SegnalazioneController {
 @Autowired
    SegnalazioneService segnalazioneService;
 @PostMapping("/{idElemento}")
 @PreAuthorize("isAuthenticated()")
 @ResponseStatus(HttpStatus.CREATED)
    public Segnalazione createSegnalazione(@RequestBody SegnalazioneRequest request, @PathVariable Long idElemento, @AuthenticationPrincipal AppUser user) {
        Segnalazione  segnalazione = new Segnalazione();
        segnalazione.setIdElemento(idElemento);
        segnalazione.setAutore(user);
        segnalazione.setTipoSegnalazione(request.getTipoSegnalazione());
        segnalazione.setDescrizione(request.getDescrizione());
        return segnalazioneService.segnala(segnalazione);
    }
    @GetMapping("/{id}")
    @PreAuthorize( "hasRole('ADMIN')")
    @ResponseStatus(HttpStatus.OK)
    public Segnalazione getSegnalazione(@PathVariable Long idElemento) {
        return segnalazioneService.findById(idElemento);
    }
    @GetMapping("")
    @PreAuthorize("hasRole('ADMIN')")
    @ResponseStatus(HttpStatus.OK)
    public Page<Segnalazione> getAll( int page, int size, String sort, String sortdir, String Autore, TipoSegnalazione tipoSegnalazione) {
        return segnalazioneService.getAllSegnalazioni( page, size, sort, sortdir, Autore, tipoSegnalazione);
    }
}
