package it.epicode.simposiodermedallo.utenti.servizi.scuole;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/scuole")
public class ScuolaController {
    @Autowired
    private ScuolaService scuolaService;
    @GetMapping("/{id}")
    public Scuola getScuolaById(@PathVariable Long id) {
        return scuolaService.getScuola(id);
    }
}
