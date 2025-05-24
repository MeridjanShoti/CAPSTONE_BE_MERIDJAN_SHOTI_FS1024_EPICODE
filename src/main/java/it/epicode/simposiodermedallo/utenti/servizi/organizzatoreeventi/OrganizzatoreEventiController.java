package it.epicode.simposiodermedallo.utenti.servizi.organizzatoreeventi;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/organizzatori")
public class OrganizzatoreEventiController {
    @Autowired
    private OrganizzatoreEventiService organizzatoreEventiService;
    @GetMapping("/{id}")
        public OrganizzatoreEventi getOrganizzatoreById(@PathVariable Long id) {
            return organizzatoreEventiService.getOrganizzatoreEventi(id);
        }
}
