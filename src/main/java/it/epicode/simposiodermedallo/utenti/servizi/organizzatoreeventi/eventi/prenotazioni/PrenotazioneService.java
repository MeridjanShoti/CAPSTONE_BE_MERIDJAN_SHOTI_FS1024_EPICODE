package it.epicode.simposiodermedallo.utenti.servizi.organizzatoreeventi.eventi.prenotazioni;

import com.github.javafaker.App;
import it.epicode.simposiodermedallo.auth.AppUser;
import it.epicode.simposiodermedallo.common.EmailSenderService;
import it.epicode.simposiodermedallo.utenti.persone.utentinormali.UtenteNormale;
import it.epicode.simposiodermedallo.utenti.persone.utentinormali.UtenteNormaleRepository;
import it.epicode.simposiodermedallo.utenti.servizi.organizzatoreeventi.eventi.Evento;
import it.epicode.simposiodermedallo.utenti.servizi.organizzatoreeventi.eventi.EventoRepository;
import jakarta.mail.MessagingException;
import jakarta.persistence.criteria.Join;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import java.time.LocalDate;

@Service
@Validated
public class PrenotazioneService {
    @Autowired
    PrenotazioneEventoRepository prenotazioneEventoRepository;
    @Autowired
    EventoRepository eventoRepository;
    @Autowired
    UtenteNormaleRepository utenteNormaleRepository;
    @Autowired
    EmailSenderService emailSenderService;

    public PrenotazioneEvento save(PrenotazioneEventoRequest request, Long idEvento, AppUser user) throws MessagingException {
        PrenotazioneEvento prenotazione = new PrenotazioneEvento();
        Evento evento = eventoRepository.findById(idEvento).orElseThrow(() -> new IllegalArgumentException("Evento non trovato"));
        if (evento.getPartecipanti().size() >= evento.getMaxPartecipanti()) {
            throw new IllegalArgumentException("Evento Sold Out");
        }
        UtenteNormale utenteNormale = utenteNormaleRepository.findById(user.getId()).orElseThrow(() -> new IllegalArgumentException("Utente non trovato"));
        prenotazione.setEvento(evento);
        prenotazione.setUtenteNormale(utenteNormale);
        prenotazione.setNumeroBiglietti(request.getNumeroBiglietti());
        prenotazione.setUtenteNormale(utenteNormale);
        prenotazione.setPrezzoPagato(evento.getPrezzoBiglietto() * request.getNumeroBiglietti());

        PrenotazioneEvento prenotazioneSalvata = prenotazioneEventoRepository.save(prenotazione);
        utenteNormaleRepository.save(utenteNormale);
        eventoRepository.save(evento);
        emailSenderService.sendEmail(utenteNormale.getEmail(), "Prenotazione confermata", "La prenotazione per l'evento " + evento.getNomeEvento() + " è stata confermata. Ti invitiamo a controllare in piattaforma.");
        return prenotazioneSalvata;
    }

    public PrenotazioneEvento getPrenotazione(Long id, AppUser user) {
        PrenotazioneEvento prenotazione = prenotazioneEventoRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("Prenotazione non trovata"));
        if (!prenotazione.getUtenteNormale().getId().equals(user.getId())) {
            throw new IllegalArgumentException("Non sei autorizzato a visualizzare questa prenotazione");
        }
        return prenotazioneEventoRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("Prenotazione non trovata"));
    }

    public Page<PrenotazioneEvento> getPrenotazioniUtenteFiltrate(AppUser utente, PrenotazioneFilter filtro, Pageable pageable) {
        Specification<PrenotazioneEvento> spec = Specification.where((root, query, cb) ->
                cb.equal(root.get("utenteNormale").get("id"), utente.getId())
        );

        if (filtro.getData1() != null) {
            spec = spec.and((root, query, cb) ->
                    cb.greaterThanOrEqualTo(root.get("evento").get("dataEvento"), filtro.getData1())
            );
        }

        if (filtro.getData2() != null) {
            spec = spec.and((root, query, cb) ->
                    cb.lessThanOrEqualTo(root.get("evento").get("dataEvento"), filtro.getData2())
            );
        }

        if ((filtro.getSoloFuturi())) {
            spec = spec.and((root, query, cb) ->
                    cb.greaterThanOrEqualTo(root.get("evento").get("dataEvento"), LocalDate.now())
            );
        }

        if (filtro.getArtista() != null && !filtro.getArtista().isBlank()) {
            spec = spec.and((root, query, cb) -> {
                // JOIN con la collection di artistiPartecipanti
                Join<Object, Object> evento = root.join("evento");
                Join<Object, String> artistiJoin = evento.join("artistiPartecipanti");
                return cb.like(cb.lower(artistiJoin), "%" + filtro.getArtista().toLowerCase() + "%");
            });
        }

        if (filtro.getNomeParziale() != null && !filtro.getNomeParziale().isBlank()) {
            spec = spec.and((root, query, cb) ->
                    cb.like(cb.lower(root.get("evento").get("nomeEvento")), "%" + filtro.getNomeParziale().toLowerCase() + "%")
            );
        }

        return prenotazioneEventoRepository.findAll(spec, pageable);
    }
}

