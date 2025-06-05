package it.epicode.simposiodermedallo.recensioni.scuole;

import it.epicode.simposiodermedallo.auth.AppUser;
import it.epicode.simposiodermedallo.recensioni.sale.MediaRecensioniResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.annotation.RequestMapping;

@RestController
@RequestMapping("/recensioni-scuola")
public class RecensioneScuolaController {
    @Autowired
    private RecensioneScuolaService recensioneScuolaService;
    @GetMapping("/scuole/{id}")
    @ResponseStatus(HttpStatus.OK)
    @PreAuthorize("isAuthenticated()")
    public Page<RecensioneScuola> getRecensioniScuola(@PathVariable Long id, @RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "10") int size, @RequestParam(defaultValue = "id") String sort, @RequestParam(defaultValue = "asc") String sortDir) {
        return recensioneScuolaService.getAll(id, page, size, sort, sortDir);
    }
    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    @PreAuthorize("isAuthenticated()")
    public RecensioneScuola getRecensioneScuola(@PathVariable Long id) {
        return recensioneScuolaService.getRecensioneScuola(id);
    }
    @PostMapping("/{id}")
    @ResponseStatus(HttpStatus.CREATED)
    @PreAuthorize("isAuthenticated()")
    public RecensioneScuola creaRecensioneSala(@PathVariable Long id, @RequestBody RecensioneScuolaRequest request, @AuthenticationPrincipal AppUser user) {
        return recensioneScuolaService.save(id, request, user);
    }
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @PreAuthorize("isAuthenticated()")
    public void deleteRecensioneScuola(@PathVariable Long id, @AuthenticationPrincipal AppUser user) {
        recensioneScuolaService.deleteRecensioneScuola(id, user);
    }
    @PutMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    @PreAuthorize("isAuthenticated()")
    public RecensioneScuola updateRecensioneSala(@PathVariable Long id, @RequestBody RecensioneScuolaRequest request, @AuthenticationPrincipal AppUser user) {
        return recensioneScuolaService.update(id, request, user);
    }
    @GetMapping("/media/{id}")
    @ResponseStatus(HttpStatus.OK)
    @PreAuthorize("isAuthenticated()")
    public MediaRecensioniResponse getMediaRecensioniSala(@PathVariable Long id) {
        return recensioneScuolaService.calcolaMediaRecensioni(id);
    }
}
