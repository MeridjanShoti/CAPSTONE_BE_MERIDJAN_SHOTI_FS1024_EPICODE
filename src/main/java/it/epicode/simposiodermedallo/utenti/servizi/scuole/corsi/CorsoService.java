package it.epicode.simposiodermedallo.utenti.servizi.scuole.corsi;

import it.epicode.simposiodermedallo.auth.AppUser;
import it.epicode.simposiodermedallo.auth.Role;
import it.epicode.simposiodermedallo.common.CommonResponse;
import it.epicode.simposiodermedallo.common.EmailSenderService;
import it.epicode.simposiodermedallo.utenti.UtenteRepository;
import it.epicode.simposiodermedallo.utenti.persone.insegnanti.InsegnanteRepository;
import it.epicode.simposiodermedallo.utenti.persone.utentinormali.UtenteNormaleRepository;
import it.epicode.simposiodermedallo.utenti.servizi.scuole.ScuolaService;
import it.epicode.simposiodermedallo.utenti.servizi.scuole.corsi.enums.Livello;
import it.epicode.simposiodermedallo.utenti.servizi.scuole.corsi.enums.StatoCorso;
import it.epicode.simposiodermedallo.utenti.servizi.scuole.corsi.iscrizioni.IscrizioneRepository;
import jakarta.mail.MessagingException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
@Slf4j
@Service
@Validated
public class CorsoService {
    @Autowired
    private CorsoRepository corsoRepository;
    @Autowired
    private ScuolaService scuolaService;
    @Autowired
    private InsegnanteRepository insegnanteRepository;
    @Autowired
    IscrizioneRepository iscrizioneRepository;
    @Autowired
    UtenteNormaleRepository utenteNormaleRepository;
    @Autowired
    EmailSenderService emailSenderService;
    public Corso save(CorsoRequest request, AppUser user) {
        Corso corso = new Corso();
        corso.setNomeCorso(request.getNomeCorso());
        corso.setStrumenti(request.getStrumenti());
        corso.setScuola(scuolaService.getScuola(user.getId()));
        corso.setMaxPartecipanti(request.getMaxPartecipanti());
        corso.setMinPartecipanti(request.getMinPartecipanti());
        corso.setLivello(request.getLivello());
        corso.setCosto(request.getCosto());
        corso.setDataInizio(request.getDataInizio());
        corso.setDataFine(request.getDataFine());
        corso.setOrarioFine(request.getOrarioFine());
        corso.setOrarioInizio(request.getOrarioInizio());
        corso.setLinkLezione(request.getLinkLezione());
        corso.setObiettivi(request.getObiettivi());
        corso.setLocandina(request.getLocandina());
        corso.setGiorniLezione(request.getGiorniLezione());
        corso.setNote(request.getNote());
        corso.setStatoCorso(StatoCorso.IN_PROGRAMMA);
        return corsoRepository.save(corso);
    }
    @Scheduled(cron = "0 0 0 * * *")
    public void aggiornaStatoCorsi() {
        LocalDate oggi = LocalDate.now();
        List<Corso> corsi = corsoRepository.findCorsiDaAggiornare(oggi, StatoCorso.IN_PROGRAMMA, StatoCorso.IN_CORSO);

        for (Corso corso : corsi) {
            if (corso.getDataInizio().isEqual(oggi)) {
                corso.setStatoCorso(StatoCorso.IN_CORSO);
            } else if (corso.getDataFine().isEqual(oggi)) {
                corso.setStatoCorso(StatoCorso.TERMINATO);
            }
            corsoRepository.save(corso);
        }
    }
    public CorsoResponse findByIdNoLink(Long id) {
        Corso corso = corsoRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("Corso non trovato"));
        CorsoResponse corsoResponse = new CorsoResponse();
        BeanUtils.copyProperties(corso, corsoResponse);
        return corsoResponse;
    }
    public Corso findByIdCorso(Long id, AppUser user) {
        Corso corso = corsoRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("Corso non trovato"));
        if (user.getRoles().contains(Role.ROLE_SCUOLA) && !corso.getScuola().getId().equals(user.getId())) {
            throw new IllegalArgumentException("Non sei la scuola che gestisce questo corso");
        }
        if (user.getRoles().contains(Role.ROLE_USER) && !corsoRepository.existsByIdAndPartecipantiId(id, user.getId())) {
            throw new IllegalArgumentException("Non sei  iscritto a questo corso");
        }
        return corso;
    }
    public Corso update(CorsoRequest request, Long id, AppUser user) {
        Corso corso = corsoRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("Corso non trovato"));
        if (!corso.getScuola().getId().equals(user.getId())) {
            throw new IllegalArgumentException("Non sei la scuola che gestisce questo corso");
        }
        corso.setNomeCorso(request.getNomeCorso());
        corso.setStrumenti(request.getStrumenti());
        corso.setMaxPartecipanti(request.getMaxPartecipanti());
        corso.setMinPartecipanti(request.getMinPartecipanti());
        corso.setLivello(request.getLivello());
        corso.setCosto(request.getCosto());
        corso.setDataInizio(request.getDataInizio());
        corso.setDataFine(request.getDataFine());
        corso.setGiorniLezione(request.getGiorniLezione());
        corso.setNote(request.getNote());
        corso.setOrarioInizio(request.getOrarioInizio());
        corso.setOrarioFine(request.getOrarioFine());
        corso.setLinkLezione(request.getLinkLezione());
        corso.setObiettivi(request.getObiettivi());
        corso.setLocandina(request.getLocandina());
        return corsoRepository.save(corso);
    }
    public void delete(Long id, AppUser user) {
        Corso corso = corsoRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("Corso non trovato"));
        if (!corso.getScuola().getId().equals(user.getId())) {
            throw new IllegalArgumentException("Non sei la scuola che gestisce questo corso");
        }
        iscrizioneRepository.findAllByCorsoId(id).forEach(iscrizione -> {
            utenteNormaleRepository.findAll().forEach(utente -> {
                try {
                    emailSenderService.sendEmail(utente.getEmail(), "Cancellazione corso", "Il corso " + corso.getNomeCorso() + " è stato cancellato. A breve riceverai un rimborso.");
                } catch (MessagingException e) {
                    log.error("Errore nell'invio della mail", e);
                }
            });
            iscrizioneRepository.delete(iscrizione);
        });
        corsoRepository.delete(corso);
    }
public Page<Corso> getAllCorsiByUser(int page, int size, String nomeCorso, Livello livello, Integer giorniASettimana, Double costo, String strumenti, LocalDate dataInizio, LocalDate dataFine, StatoCorso statoCorso, Sort sort, AppUser user) {
        if(user.getRoles().contains( Role.ROLE_ORGANIZZATORE) || user.getRoles().contains( Role.ROLE_ADMIN) || user.getRoles().contains( Role.ROLE_GESTORE_SP)) {
            throw new IllegalArgumentException("Non ti serve cercare i corsi");
        }
        Long scuolaId = null;
        Long partecipanteId = null;
        Long insegnanteId = null;
        if (user.getRoles().contains(Role.ROLE_SCUOLA)) {
           scuolaId = user.getId();
        } else if (user.getRoles().contains(Role.ROLE_USER)) {
            partecipanteId = user.getId();
        } else if (user.getRoles().contains(Role.ROLE_INSEGNANTE)) {
            insegnanteId = user.getId();
        }
        CorsoFilter filter = new CorsoFilter();
        filter.setNomeCorso(nomeCorso);
        filter.setLivello(livello);
        filter.setGiorniASettimana(giorniASettimana);
        filter.setCosto(costo);
        filter.setStrumenti(strumenti);
        filter.setDataInizio(dataInizio);
        filter.setDataFine(dataFine);
        filter.setStatoCorso(statoCorso);
        filter.setScuolaId(scuolaId);
        filter.setPartecipanteId(partecipanteId);
        filter.setInsegnanteId(insegnanteId);
        PageRequest pageable = PageRequest.of(page, size, sort);
        return corsoRepository.findAll(CorsoSpecifications.filterBy(filter), pageable);
    }
    public Page<CorsoResponse> getAllCorsi(int page, int size, String nomeCorso, Livello livello, Integer giorniASettimana, Double costo, String strumenti, LocalDate dataInizio, LocalDate dataFine, Sort sort) {
        CorsoFilter filter = new CorsoFilter();
        filter.setNomeCorso(nomeCorso);
        filter.setLivello(livello);
        filter.setGiorniASettimana(giorniASettimana);
        filter.setCosto(costo);
        filter.setStrumenti(strumenti);
        filter.setDataInizio(dataInizio);
        filter.setDataFine(dataFine);
        filter.setStatoCorso(StatoCorso.IN_PROGRAMMA);
        PageRequest pageable = PageRequest.of(page, size, sort);
        Page<Corso> corsi = corsoRepository.findAll(CorsoSpecifications.filterBy(filter), pageable);
        return corsi.map(this::toCorsoResponse);
    }
    public CorsoResponse toCorsoResponse(Corso corso) {
        CorsoResponse corsoResponse = new CorsoResponse();
        BeanUtils.copyProperties(corso, corsoResponse);
        return corsoResponse;
    }
    public List<LocalDate> getGiorniLezione(Long id) {
        Corso corso = corsoRepository.findById(id).orElseThrow();

        LocalDate inizio = corso.getDataInizio();
        LocalDate fine = corso.getDataFine();
        Set<DayOfWeek> giorniLezione = corso.getGiorniLezione();

        List<LocalDate> dateLezione = new ArrayList<>();
        LocalDate current = inizio;

        while (!current.isAfter(fine)) {
            if (giorniLezione.contains(current.getDayOfWeek())) {
                dateLezione.add(current);
            }
            current = current.plusDays(1);
        }
        return dateLezione;
    }
    public CommonResponse assegnaInsegnante(Long corsoId, Long insegnanteId, AppUser user) {
        Corso corso = corsoRepository.findById(corsoId).orElseThrow(() -> new IllegalArgumentException("Corso non trovato"));
        if (!corso.getScuola().getId().equals(user.getId())) {
            throw new IllegalArgumentException("Non sei la scuola che gestisce questo corso");
        }
        corso.setInsegnante(insegnanteRepository.findById(insegnanteId).orElseThrow(() -> new IllegalArgumentException("Insegnante non trovato")));
        corsoRepository.save(corso);
        return new CommonResponse(corsoId);
    }
    public List<Corso> getCorsiByInsegnante(AppUser user) {
        return corsoRepository.findAllByInsegnanteId(user.getId());
    }
}
