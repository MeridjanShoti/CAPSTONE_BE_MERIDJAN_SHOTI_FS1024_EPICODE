package it.epicode.simposiodermedallo.utenti.persone.insegnanti;

import it.epicode.simposiodermedallo.auth.AppUser;
import it.epicode.simposiodermedallo.auth.Role;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@RestController
@RequestMapping("/insegnanti")
public class InsegnanteController {
    @Autowired
    private InsegnanteRepository insegnanteRepository;
    @Autowired
    private InsegnanteService insegnanteService;
    @PreAuthorize("isAuthenticated()")
    @GetMapping("/curriculum/{id}")
    public ResponseEntity<byte[]> downloadCurriculum(@PathVariable Long id) {
        Insegnante i = insegnanteRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Insegnante non trovato"));
        byte[] curriculum = i.getCurriculum();

        if (curriculum == null || curriculum.length == 0) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Curriculum non disponibile");
        }
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"curriculum.pdf\"")
                .contentType(MediaType.APPLICATION_PDF)
                .body(i.getCurriculum());
    }
    @GetMapping("/{id}")
    public InsegnanteResponse getInsegnanteById(@PathVariable Long id) {
        return insegnanteService.getInsegnante(id);
    }
    @GetMapping("/complete/{id}")
    @PreAuthorize("isAuthenticated()")
    public Insegnante getCompleteInsegnante(@PathVariable Long id , @AuthenticationPrincipal AppUser user) {
        if (user.getRoles().contains(Role.ROLE_SCUOLA)) {
            if (!insegnanteRepository.existsByScuolaIdAndId(user.getId(), id)) {
                throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Non sei la scuola che gestisce questo insegnante");
            }
        } else if (user.getRoles().contains(Role.ROLE_INSEGNANTE)) {
            if (!user.getId().equals(id)) {
                throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Non puoi visualizzare un altro insegnante");
            }
        } else {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Non sei autorizzato a visualizzare questo insegnante");
        }

        return insegnanteService.getInsegnanteComplete(id);
    }
    @GetMapping("")
    @PreAuthorize("isAuthenticated()")
    public List<Insegnante> getMieiInsegnanti(@AuthenticationPrincipal AppUser user) {
        return insegnanteService.getAllInsegnanti(user);
    }
    @GetMapping("/page")
    @PreAuthorize("isAuthenticated()")
    public Page<Insegnante> getMieiInsegnantiPage(@AuthenticationPrincipal AppUser user, @RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "10") int size, @RequestParam(defaultValue = "id") String sort) {
        return insegnanteService.getAllInsegnantiPage(user, page, size, sort);
    }
}
