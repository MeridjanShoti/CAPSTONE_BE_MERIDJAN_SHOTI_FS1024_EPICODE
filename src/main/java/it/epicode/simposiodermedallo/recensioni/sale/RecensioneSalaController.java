package it.epicode.simposiodermedallo.recensioni.sale;

import it.epicode.simposiodermedallo.auth.AppUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/recensioni-sala")
public class RecensioneSalaController {
    @Autowired
    private RecensioneSalaService recensioneSalaService;
    @GetMapping("/sale/{id}")
    @ResponseStatus(HttpStatus.OK)
    @PreAuthorize("isAuthenticated()")
    public Page<RecensioneSala> getRecensioniSala(@PathVariable Long id, @RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "10") int size, @RequestParam(defaultValue = "id") String sort, @RequestParam(defaultValue = "asc") String sortDir) {
        return recensioneSalaService.getAll(id, page, size, sort, sortDir);
    }
    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    @PreAuthorize("isAuthenticated()")
    public RecensioneSala getRecensioneSala(@PathVariable Long id) {
        return recensioneSalaService.getRecensioneSala(id);
    }
    @PostMapping("/{id}")
    @ResponseStatus(HttpStatus.CREATED)
    @PreAuthorize("isAuthenticated()")
    public RecensioneSala creaRecensioneSala(@PathVariable Long id, @RequestBody RecensioneSalaRequest request, @AuthenticationPrincipal AppUser user) {
        return recensioneSalaService.save(id, request, user);
    }
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @PreAuthorize("isAuthenticated()")
    public void deleteRecensioneSala(@PathVariable Long id, @AuthenticationPrincipal AppUser user) {
        recensioneSalaService.deleteRecensioneSala(id, user);
    }
    @PutMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    @PreAuthorize("isAuthenticated()")
    public RecensioneSala updateRecensioneSala(@PathVariable Long id, @RequestBody RecensioneSalaRequest request, @AuthenticationPrincipal AppUser user) {
        return recensioneSalaService.update(id, request, user);
    }
    @GetMapping("/media/{id}")
    @ResponseStatus(HttpStatus.OK)
    @PreAuthorize("isAuthenticated()")
    public MediaRecensioniResponse getMediaRecensioniSala(@PathVariable Long id) {
        return recensioneSalaService.calcolaMediaRecensioni(id);
    }

}
