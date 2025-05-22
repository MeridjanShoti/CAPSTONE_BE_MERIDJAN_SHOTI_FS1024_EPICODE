package it.epicode.simposiodermedallo.commenti;

import it.epicode.simposiodermedallo.auth.AppUser;
import it.epicode.simposiodermedallo.common.CommonResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/commenti")
public class CommentoController {
    @Autowired
    private CommentoRepository commentoRepository;
    @Autowired
    private CommentoService commentoService;
    @PostMapping("/{eventoId}")
    @PreAuthorize("isAuthenticated()")
    public Commento createCommento(@RequestBody CommentoRequest commentoRequest, @PathVariable Long eventoId, @AuthenticationPrincipal AppUser user) {
        return commentoService.save(eventoId, commentoRequest, user);
    }
    @PutMapping("/{id}")
    @PreAuthorize("isAuthenticated()")
    public Commento updateCommento(@RequestBody CommentoRequest commentoRequest, @PathVariable Long id, @AuthenticationPrincipal AppUser user) {
        return commentoService.update(id, commentoRequest, user);
    }
    @DeleteMapping("/{id}")
    @PreAuthorize("isAuthenticated()")
    public CommonResponse deleteCommento(@PathVariable Long id, @AuthenticationPrincipal AppUser user) {
        return commentoService.delete(id, user);
    }
    @GetMapping("/{id}")
    @PreAuthorize("isAuthenticated()")
    public Commento getCommento(@PathVariable Long id) {
        return commentoService.getById(id);
    }
    @GetMapping("/eventi/{id}")
    @PreAuthorize("isAuthenticated()")
        public Page<Commento> getAllByEvento(@PathVariable Long id, @RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "10") int size, @RequestParam(defaultValue = "id") String sort, @RequestParam(defaultValue = "asc") String sortDir) {
        return commentoService.getAllByEvento(id, page, size, sort, sortDir);
    }

}
