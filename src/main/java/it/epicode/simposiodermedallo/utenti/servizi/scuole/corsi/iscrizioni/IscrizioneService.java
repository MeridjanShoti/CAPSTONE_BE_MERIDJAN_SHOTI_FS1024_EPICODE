package it.epicode.simposiodermedallo.utenti.servizi.scuole.corsi.iscrizioni;

import it.epicode.simposiodermedallo.auth.AppUser;
import it.epicode.simposiodermedallo.utenti.persone.utentinormali.UtenteNormale;
import it.epicode.simposiodermedallo.utenti.persone.utentinormali.UtenteNormaleRepository;
import it.epicode.simposiodermedallo.utenti.servizi.scuole.corsi.Corso;
import it.epicode.simposiodermedallo.utenti.servizi.scuole.corsi.CorsoRepository;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

@Service
public class IscrizioneService {
    @Autowired
    private IscrizioneRepository iscrizioneRepository;
    @Autowired
    private CorsoRepository corsoRepository;
    @Autowired
    private UtenteNormaleRepository utenteNormaleRepository;
    public Iscrizione save(Long idCorso, AppUser user) {
        Corso corso = corsoRepository.findById(idCorso).orElseThrow(() -> new IllegalArgumentException("Corso non trovato"));
        if(iscrizioneRepository.existsByUtenteIdAndCorsoId(user.getId(), idCorso)){
            throw new IllegalArgumentException("Sei già iscritto a questo corso");
        }
        if (corso.getDataInizio().isBefore(LocalDate.now())) {
            throw new IllegalArgumentException("Il corso è già iniziato");
        }
        if (corso.getPartecipanti().size() >= corso.getMaxPartecipanti()) {
            throw new IllegalArgumentException("Il corso è già al completo");
        }
        UtenteNormale utente = utenteNormaleRepository.findById(user.getId()).orElseThrow(() -> new IllegalArgumentException("Utente non trovato"));
        Iscrizione iscrizione = new Iscrizione();
        iscrizione.setCorso(corso);
        iscrizione.setUtente(utente);
        corso.getPartecipanti().add(utente);
        corsoRepository.save(corso);
        return iscrizioneRepository.save(iscrizione);
    }
}
