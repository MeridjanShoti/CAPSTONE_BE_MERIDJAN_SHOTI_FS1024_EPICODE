package it.epicode.simposiodermedallo.utenti.servizi.gestorisaleprove.saleprove;

import it.epicode.simposiodermedallo.auth.AppUser;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Sort;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.time.DayOfWeek;

@RestController
@RequestMapping("/saleprove")
public class SalaProveController {
    @Autowired
    private SalaProveService salaProveService;

    @PostMapping("")
    @PreAuthorize("hasAuthority('ROLE_GESTORE_SP')")
    public SalaProve creaSalaProve(@Valid @RequestBody SalaProveRequest salaProveRequest, @AuthenticationPrincipal AppUser user) {
        return salaProveService.createSalaProve(salaProveRequest, user);
    }
    @PutMapping("/{id}")
    @PreAuthorize("hasAuthority('ROLE_GESTORE_SP')")
    public SalaProve updateSalaProve(@RequestBody SalaProveRequest salaProveRequest, @PathVariable Long id, @AuthenticationPrincipal AppUser user) {
        return salaProveService.updateSalaProve(salaProveRequest, id, user);
    }
    @DeleteMapping("/{id}")
    @PreAuthorize("hasAuthority('ROLE_GESTORE_SP')")
    public void deleteSalaProve(@PathVariable Long id, @AuthenticationPrincipal AppUser user) {
        salaProveService.deleteSalaProve(id, user);
    }
    @GetMapping("/{id}")
    public SalaProve getSalaProve(@PathVariable Long id) {
        return salaProveService.getSalaProve(id);
    }
    @GetMapping("")
    public Page<SalaProve> getAllSaleProve(@RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "10") int size, @RequestParam(defaultValue = "id") String sortBy, @RequestParam(defaultValue = "asc") String sortDir, @RequestParam(required = false) String citta, @RequestParam(required = false) Integer capienzaMin, @RequestParam(required = false) Double prezzoOrarioMax, @RequestParam(required = false)DayOfWeek giornoApertura, @RequestParam(required = false) String nomeSala, @AuthenticationPrincipal AppUser user) {
        Sort.Direction direction = Sort.Direction.fromString(sortDir);
        Sort sort = Sort.by(direction, sortBy);
        return salaProveService.getAllSaleProve( page, size, sort, new SalaProveFilter(citta, capienzaMin, prezzoOrarioMax, giornoApertura));
    }
    @GetMapping("/gestore")
    public Page<SalaProve> getAllSaleProveByGestoreSala(@RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "10") int size, @RequestParam(defaultValue = "id") String sortBy, @AuthenticationPrincipal AppUser user) {
        Sort.Direction direction = Sort.Direction.fromString("asc");
        Sort sort = Sort.by(direction, sortBy);
        return salaProveService.getAllSaleProveByGestoreSala(user, page, size, sort);
    }

}
