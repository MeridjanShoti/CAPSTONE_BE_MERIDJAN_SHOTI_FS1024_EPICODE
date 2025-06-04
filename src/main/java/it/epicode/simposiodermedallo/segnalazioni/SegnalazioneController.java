package it.epicode.simposiodermedallo.segnalazioni;

import it.epicode.simposiodermedallo.auth.AppUser;
import it.epicode.simposiodermedallo.common.CommonResponse;
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
    public Page<Segnalazione> getAll( @RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "10") int size, @RequestParam(defaultValue = "id") String sort, @RequestParam(defaultValue = "asc") String sortdir, @RequestParam(required = false) String autore, @RequestParam(required = false) TipoSegnalazione tipoSegnalazione) {
        return segnalazioneService.getAllSegnalazioni( page, size, sort, sortdir, autore, tipoSegnalazione);
    }
    @DeleteMapping("/approva/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public CommonResponse approva(@PathVariable Long id) {
        segnalazioneService.approve(id);
        return new CommonResponse(id);
    }
    @DeleteMapping("/rifiuta/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public CommonResponse rifiuta(@PathVariable Long id) {
        segnalazioneService.delete(id);
        return new CommonResponse(id);
    }
}
