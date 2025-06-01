package it.epicode.simposiodermedallo.utenti.servizi.scuole.corsi.iscrizioni;

import it.epicode.simposiodermedallo.auth.AppUser;
import it.epicode.simposiodermedallo.auth.Role;
import it.epicode.simposiodermedallo.common.CommonResponse;
import it.epicode.simposiodermedallo.utenti.persone.insegnanti.InsegnanteRepository;
import it.epicode.simposiodermedallo.utenti.persone.utentinormali.UtenteNormale;
import it.epicode.simposiodermedallo.utenti.persone.utentinormali.UtenteNormaleRepository;
import it.epicode.simposiodermedallo.utenti.servizi.scuole.corsi.Corso;
import it.epicode.simposiodermedallo.utenti.servizi.scuole.corsi.CorsoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;

@Service
public class IscrizioneService {
    @Autowired
    private IscrizioneRepository iscrizioneRepository;
    @Autowired
    private CorsoRepository corsoRepository;
    @Autowired
    private UtenteNormaleRepository utenteNormaleRepository;
    @Autowired
    private InsegnanteRepository insegnanteRepository;
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
    @Transactional
    public CommonResponse delete(Long idCorso, AppUser user) {
        Corso corso = corsoRepository.findById(idCorso).orElseThrow(() -> new IllegalArgumentException("Corso non trovato"));
        if(!corso.getScuola().getId().equals(user.getId())){
            throw new IllegalArgumentException("Non sei la scuola che gestisce questo corso");
        }
        Iscrizione iscrizione = iscrizioneRepository.findByUtenteIdAndCorsoId(user.getId(), idCorso);
        iscrizioneRepository.delete(iscrizione);
        return new CommonResponse();
    }
    public Iscrizione takePresenze(Long idCorso, Long idUtente, Boolean isPresente, AppUser user) {
        Corso corso = corsoRepository.findById(idCorso).orElseThrow(() -> new IllegalArgumentException("Corso non trovato"));
        if(!corso.getInsegnante().getId().equals(user.getId())){
            throw new IllegalArgumentException("Non sei l'insegnante che gestisce questo corso");
        }
        Iscrizione iscrizione = iscrizioneRepository.findByUtenteIdAndCorsoId(idUtente, idCorso);
        if (iscrizione.getPresenze().stream().anyMatch(p -> p.getData().isEqual(LocalDate.now()))) {
            throw new IllegalArgumentException("Hai già registrato la presenza di questo utente oggi");
        }
        Presenze presenze = new Presenze(LocalDate.now(), isPresente);
        iscrizione.getPresenze().add(presenze);
        return iscrizioneRepository.save(iscrizione);
    }
    public Iscrizione assegnaVoto(Long idCorso, Long idUtente, Double voto, AppUser user) {
        Corso corso = corsoRepository.findById(idCorso).orElseThrow(() -> new IllegalArgumentException("Corso non trovato"));
        if(!corso.getInsegnante().getId().equals(user.getId())){
            throw new IllegalArgumentException("Non sei l'insegnante che gestisce questo corso");
        }
        Iscrizione iscrizione = iscrizioneRepository.findByUtenteIdAndCorsoId(idUtente, idCorso);
        iscrizione.getValutazioni().add(voto);
        return iscrizioneRepository.save(iscrizione);
    }
    public Page<Iscrizione> getAllIscrizioniByCorso(int page, int size, Long idCorso, AppUser user) {
        Corso corso = corsoRepository.findById(idCorso).orElseThrow(() -> new IllegalArgumentException("Corso non trovato"));
        if (user.getRoles().contains( Role.ROLE_SCUOLA) && !corso.getScuola().getId().equals(user.getId())) {
            throw new IllegalArgumentException("Non sei la scuola che gestisce questo corso");
        }
        if (user.getRoles().contains( Role.ROLE_INSEGNANTE) && !corso.getInsegnante().getId().equals(user.getId())) {
            throw new IllegalArgumentException("Non sei l'insegnante che gestisce questo corso");
        }
         if (!user.getRoles().contains( Role.ROLE_SCUOLA) && !user.getRoles().contains( Role.ROLE_INSEGNANTE)) {
            throw new IllegalArgumentException("Non sei l'insegnante o la scuola che gestisce questo corso");
         }
        return iscrizioneRepository.findAllByCorsoId(idCorso, PageRequest.of(page, size));
    }
    public Page<Iscrizione> getAllIscrizioniByUtente(int page, int size, AppUser user) {
        if (!user.getRoles().contains( Role.ROLE_USER)) {
            throw new IllegalArgumentException("Non sei un utente");
        }
        return iscrizioneRepository.findAllByUtenteId(user.getId(), PageRequest.of(page, size));
    }
    public Iscrizione getIscrizioneById(Long id, AppUser user) {
        Iscrizione iscrizione = iscrizioneRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("Iscrizione non trovata"));
        if (user.getRoles().contains( Role.ROLE_SCUOLA) && !iscrizione.getCorso().getScuola().getId().equals(user.getId())) {
            throw new IllegalArgumentException("Non sei la scuola che gestisce questo corso");
        }
        if (user.getRoles().contains( Role.ROLE_INSEGNANTE) && !iscrizione.getCorso().getInsegnante().getId().equals(user.getId())) {
            throw new IllegalArgumentException("Non sei l'insegnante che gestisce questo corso");
        }
         if (!user.getRoles().contains( Role.ROLE_USER) && !user.getId().equals(iscrizione.getUtente().getId())) {
            throw new IllegalArgumentException("Non sei l'utente che ha effettuato l'iscrizione");
         }
        return iscrizione;
    }
}
