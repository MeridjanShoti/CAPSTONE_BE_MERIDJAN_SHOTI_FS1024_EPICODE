package it.epicode.simposiodermedallo.utenti.servizi.gestorisaleprove.saleprove.prenotazioni;

import it.epicode.simposiodermedallo.auth.AppUser;
import it.epicode.simposiodermedallo.auth.Role;
import it.epicode.simposiodermedallo.common.CommonResponse;
import it.epicode.simposiodermedallo.common.EmailSenderService;
import it.epicode.simposiodermedallo.utenti.persone.utentinormali.UtenteNormale;
import it.epicode.simposiodermedallo.utenti.persone.utentinormali.UtenteNormaleRepository;
import it.epicode.simposiodermedallo.utenti.servizi.gestorisaleprove.saleprove.SalaProve;
import it.epicode.simposiodermedallo.utenti.servizi.gestorisaleprove.saleprove.SalaProveRepository;
import it.epicode.simposiodermedallo.utenti.servizi.gestorisaleprove.saleprove.prenotazioni.slot.SlotDisponibile;
import jakarta.mail.MessagingException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

@Service
public class PrenotazioneSalaProveService {
    @Autowired
    private PrenotazioneSalaProveRepository prenotazioneSalaProveRepository;
    @Autowired
    private SalaProveRepository salaProveRepository;
    @Autowired
    private UtenteNormaleRepository utenteRepository;
    @Autowired
    private EmailSenderService emailSenderService;
    public PrenotazioneSalaProve creaPrenotazione(PrenotazioneSalaRequest request, AppUser user, Long salaId) {
        SalaProve sala = salaProveRepository.findById(salaId)
                .orElseThrow(() -> new IllegalArgumentException("Sala non trovata"));

        UtenteNormale utente = utenteRepository.findById(user.getId())
                .orElseThrow(() -> new IllegalArgumentException("Utente non trovato"));

        LocalDateTime inizio = request.getInizio();
        LocalDateTime fine = request.getFine();

        if (!inizio.isBefore(fine)) {
            throw new IllegalArgumentException("L'orario di inizio deve precedere quello di fine");
        }

        if (request.getNumMembri() > sala.getCapienzaMax()) {
            throw new IllegalArgumentException("La prenotazione supera la capienza della sala");
        }


        DayOfWeek giorno = inizio.getDayOfWeek();
        if (!sala.getGiorniApertura().contains(giorno)) {
            throw new IllegalArgumentException("La sala è chiusa il " + giorno);
        }

        boolean chiusuraDopoMezzanotte = sala.getOrarioChiusura().isBefore(sala.getOrarioApertura());

        LocalDateTime apertura = inizio.toLocalDate().atTime(sala.getOrarioApertura());
        LocalDateTime chiusura = chiusuraDopoMezzanotte
                ? apertura.plusDays(1).withHour(sala.getOrarioChiusura().getHour()).withMinute(sala.getOrarioChiusura().getMinute())
                : apertura.withHour(sala.getOrarioChiusura().getHour()).withMinute(sala.getOrarioChiusura().getMinute());

        if (inizio.isBefore(apertura) || fine.isAfter(chiusura)) {
            throw new IllegalArgumentException("La prenotazione deve essere tra " +
                    apertura + " e " + chiusura);
        }
        List<PrenotazioneSalaProve> conflitti = prenotazioneSalaProveRepository.findPrenotazioniConflittuali(
                sala.getId(), inizio, fine);
        if (!conflitti.isEmpty()) {
            throw new IllegalStateException("Esiste già una prenotazione in quell'orario");
        }

        PrenotazioneSalaProve prenotazione = new PrenotazioneSalaProve();
        prenotazione.setPagata(request.getPagata());
        prenotazione.setInizio(inizio);
        prenotazione.setFine(fine);
        prenotazione.setNumMembri(request.getNumMembri());
        prenotazione.setSalaProve(sala);
        prenotazione.setUtente(utente);
        return prenotazioneSalaProveRepository.save(prenotazione);
    }
    public Page<PrenotazioneSalaProve> getPrenotazioniGestore(AppUser user, PrenotazioneSalaFilter filter, Pageable pageable) {
        Specification<PrenotazioneSalaProve> spec = Specification.where((root, query, cb) -> cb.equal(root.get("salaProve").get("gestore").get("id"), user.getId()));
        if (filter.getNomeSala() != null && !filter.getNomeSala().isBlank()) {
            spec = spec.and((root, query, cb) -> cb.like(cb.lower(root.get("salaProve").get("nomeSala")), "%" + filter.getNomeSala().toLowerCase() + "%"));
        }
        if (filter.getIdSala() != null) {
            spec = spec.and((root, query, cb) -> cb.equal(root.get("salaProve").get("id"), filter.getIdSala()));
        }
        if (filter.getData1() != null) {
            spec = spec.and((root, query, cb) -> cb.greaterThanOrEqualTo(root.get("inizio"), filter.getData1()));
        }
        if (filter.getData2() != null) {
            spec = spec.and((root, query, cb) -> cb.lessThanOrEqualTo(root.get("inizio"), filter.getData2().atTime(LocalTime.MAX)));
        }
        if (filter.getSoloFuturi() != null) {
            spec = spec.and((root, query, cb) -> cb.greaterThanOrEqualTo(root.get("inizio"), LocalDateTime.now()));
        }
        return prenotazioneSalaProveRepository.findAll(spec, pageable);
    }
    public Page<PrenotazioneSalaProve> getPrenotazioniUtente(AppUser user, PrenotazioneSalaFilter filter, Pageable pageable) {
        Specification<PrenotazioneSalaProve> spec = Specification.where((root, query, cb) -> cb.equal(root.get("utente").get("id"), user.getId()));
        if (filter.getNomeSala() != null && !filter.getNomeSala().isBlank()) {
            spec = spec.and((root, query, cb) -> cb.like(cb.lower(root.get("salaProve").get("nomeSala")), "%" + filter.getNomeSala().toLowerCase() + "%"));
        }
        if (filter.getIdSala() != null) {
            spec = spec.and((root, query, cb) -> cb.equal(root.get("salaProve").get("id"), filter.getIdSala()));
        }
        if (filter.getData1() != null) {
            spec = spec.and((root, query, cb) -> cb.greaterThanOrEqualTo(root.get("inizio"), filter.getData1()));
        }
        if (filter.getData2() != null) {
            spec = spec.and((root, query, cb) -> cb.lessThanOrEqualTo(root.get("inizio"), filter.getData2().atTime(LocalTime.MAX)));
        }
        if (filter.getSoloFuturi() != null && filter.getSoloFuturi()) {
            spec = spec.and((root, query, cb) -> cb.greaterThanOrEqualTo(root.get("inizio"), LocalDateTime.now()));
        }
        return prenotazioneSalaProveRepository.findAll(spec, pageable);
    }
    public PrenotazioneSalaProve getPrenotazioneById(Long id, AppUser user) {
        PrenotazioneSalaProve prenotazione = prenotazioneSalaProveRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Prenotazione non trovata"));
        if (user.getRoles().contains(Role.ROLE_USER) && !prenotazione.getUtente().getId().equals(user.getId())) {
            throw new IllegalArgumentException("Non sei l'utente che ha effettuato la prenotazione");
        }
        if (user.getRoles().contains(Role.ROLE_GESTORE_SP) && !prenotazione.getSalaProve().getGestoreSala().getId().equals(user.getId())) {
            throw new IllegalArgumentException("Non sei il gestore della sala");
        }
        return prenotazione;
    }

public CommonResponse deletePrenotazione(Long id, AppUser user) throws MessagingException {
    PrenotazioneSalaProve prenotazione = prenotazioneSalaProveRepository.findById(id)
            .orElseThrow(() -> new IllegalArgumentException("Prenotazione non trovata"));
    if (user.getId().equals(prenotazione.getSalaProve().getGestoreSala().getId())){
        throw new IllegalArgumentException("Non sei il gestore della sala prove");
    }
    prenotazioneSalaProveRepository.delete(prenotazione);
    emailSenderService.sendEmail(prenotazione.getUtente().getEmail(), "Prenotazione cancellata", "La prenotazione alla sala prove " + prenotazione.getSalaProve().getNomeSala() + " è stata cancellata");
    return new CommonResponse(id);
}
    public PrenotazioneSalaProve updatePrenotazione(Long prenotazioneId, PrenotazioneSalaRequest request, AppUser user) {
        PrenotazioneSalaProve prenotazione = prenotazioneSalaProveRepository.findById(prenotazioneId)
                .orElseThrow(() -> new IllegalArgumentException("Prenotazione non trovata"));


        if (!prenotazione.getSalaProve().getGestoreSala().getId().equals(user.getId())) {
            throw new IllegalStateException("Non puoi modificare questa prenotazione");
        }

        SalaProve sala = prenotazione.getSalaProve();

        LocalDateTime inizio = request.getInizio();
        LocalDateTime fine = request.getFine();

        if (!inizio.isBefore(fine)) {
            throw new IllegalArgumentException("L'orario di inizio deve precedere quello di fine");
        }

        if (request.getNumMembri() > sala.getCapienzaMax()) {
            throw new IllegalArgumentException("La prenotazione supera la capienza della sala");
        }

        DayOfWeek giorno = inizio.getDayOfWeek();
        if (!sala.getGiorniApertura().contains(giorno)) {
            throw new IllegalArgumentException("La sala è chiusa il " + giorno);
        }

        boolean chiusuraDopoMezzanotte = sala.getOrarioChiusura().isBefore(sala.getOrarioApertura());

        LocalDateTime apertura = inizio.toLocalDate().atTime(sala.getOrarioApertura());
        LocalDateTime chiusura = chiusuraDopoMezzanotte
                ? apertura.plusDays(1).withHour(sala.getOrarioChiusura().getHour()).withMinute(sala.getOrarioChiusura().getMinute())
                : apertura.withHour(sala.getOrarioChiusura().getHour()).withMinute(sala.getOrarioChiusura().getMinute());

        if (inizio.isBefore(apertura) || fine.isAfter(chiusura)) {
            throw new IllegalArgumentException("La prenotazione deve essere tra " + apertura + " e " + chiusura);
        }

        List<PrenotazioneSalaProve> conflitti = prenotazioneSalaProveRepository.findPrenotazioniConflittuali(
                sala.getId(), inizio, fine
        ).stream().filter(p -> !p.getId().equals(prenotazioneId)).toList();

        if (!conflitti.isEmpty()) {
            throw new IllegalStateException("Esiste già una prenotazione in quell'orario");
        }


        prenotazione.setInizio(inizio);
        prenotazione.setFine(fine);
        prenotazione.setNumMembri(request.getNumMembri());
        prenotazione.setPagata(request.getPagata());

        return prenotazioneSalaProveRepository.save(prenotazione);
    }
    public List<SlotDisponibile> getDisponibilita(Long salaId, LocalDate giorno) {
        SalaProve sala = salaProveRepository.findById(salaId)
                .orElseThrow(() -> new IllegalArgumentException("Sala non trovata"));

        if (!sala.getGiorniApertura().contains(giorno.getDayOfWeek())) {
            return List.of();
        }

        LocalDateTime apertura = giorno.atTime(sala.getOrarioApertura());
        LocalDateTime chiusura = sala.getOrarioChiusura().isBefore(sala.getOrarioApertura())
                ? giorno.plusDays(1).atTime(sala.getOrarioChiusura())
                : giorno.atTime(sala.getOrarioChiusura());

        List<PrenotazioneSalaProve> prenotazioni = prenotazioneSalaProveRepository
                .findPrenotazioniConflittuali(salaId, apertura, chiusura);

        List<SlotDisponibile> slotDisponibili = new ArrayList<>();

        LocalDateTime slotStart = apertura;

        while (!slotStart.plusMinutes(30).isAfter(chiusura)) {
            LocalDateTime currentStart = slotStart;
            LocalDateTime currentEnd = currentStart.plusMinutes(30);

            boolean occupato = prenotazioni.stream().anyMatch(p ->
                    p.getInizio().isBefore(currentEnd) && p.getFine().isAfter(currentStart));

            if (!occupato) {
                slotDisponibili.add(new SlotDisponibile(currentStart, currentEnd));
            }

            slotStart = slotStart.plusMinutes(30);
        }

        return slotDisponibili;
    }
}


