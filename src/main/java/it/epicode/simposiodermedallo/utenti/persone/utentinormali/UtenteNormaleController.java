package it.epicode.simposiodermedallo.utenti.persone.utentinormali;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/utenti")
public class UtenteNormaleController {
    @Autowired
    private UtenteNormaleService utenteNormaleService;
    @GetMapping("/{id}")
    @PreAuthorize("isAuthenticated()")
    public UtenteNormale getUtenteNormaleById(@PathVariable Long id) {
        return utenteNormaleService.getUtenteNormaleById(id);
    }


}
