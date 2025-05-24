package it.epicode.simposiodermedallo.utenti.servizi.organizzatoreeventi;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class OrganizzatoreEventiService {
    @Autowired
    private OrganizzatoreEventiRepository organizzatoreEventiRepository;
    public OrganizzatoreEventi getOrganizzatoreEventi(Long id) {
        return organizzatoreEventiRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("Organizzatore non trovato"));
    }
}
