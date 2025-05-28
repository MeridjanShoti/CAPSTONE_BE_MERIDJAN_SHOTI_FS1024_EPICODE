package it.epicode.simposiodermedallo.utenti.servizi.gestorisaleprove;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/gestori")
public class GestoreSalaController {
    @Autowired
    private GestoreSalaService gestoreSalaService;
    @GetMapping("/{id}")
    public GestoreSala getGestoreSalaById(@PathVariable Long id) {
        return gestoreSalaService.getGestoreSala(id);
    }
}
