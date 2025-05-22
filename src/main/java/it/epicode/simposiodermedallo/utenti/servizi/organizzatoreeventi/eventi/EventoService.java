package it.epicode.simposiodermedallo.utenti.servizi.organizzatoreeventi.eventi;

import it.epicode.simposiodermedallo.auth.AppUser;
import it.epicode.simposiodermedallo.auth.Role;
import it.epicode.simposiodermedallo.common.CommonResponse;
import it.epicode.simposiodermedallo.common.EmailSenderService;
import it.epicode.simposiodermedallo.utenti.persone.utentinormali.UtenteNormale;
import it.epicode.simposiodermedallo.utenti.persone.utentinormali.UtenteNormaleRepository;
import it.epicode.simposiodermedallo.utenti.servizi.organizzatoreeventi.OrganizzatoreEventi;
import it.epicode.simposiodermedallo.utenti.servizi.organizzatoreeventi.OrganizzatoreEventiRepository;
import it.epicode.simposiodermedallo.utenti.servizi.organizzatoreeventi.eventi.prenotazioni.PrenotazioneEventoRepository;
import it.epicode.simposiodermedallo.utenti.servizi.organizzatoreeventi.eventi.prenotazioni.PrenotazioneService;
import jakarta.mail.MessagingException;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import java.time.LocalDate;
import java.util.List;

@Service
@Validated
public class EventoService {
    @Autowired
    private OrganizzatoreEventiRepository organizzatoreEventiRepository;
    @Autowired
    private EventoRepository eventoRepository;
    @Autowired
    private EmailSenderService emailSenderService;
    @Autowired
    private PrenotazioneEventoRepository prenotazioneEventoRepository;
    @Autowired
    private UtenteNormaleRepository utenteNormaleRepository;

    public Evento creaEvento(EventoRequest eventoRequest, AppUser user) {
        if (eventoRequest.getDataEvento().isBefore(LocalDate.now())) {
            throw new IllegalArgumentException("Non puoi creare un evento in una data passata");
        }
        Evento evento = new Evento();
        OrganizzatoreEventi organizzatore = organizzatoreEventiRepository.findById(user.getId()).orElseThrow(() -> new EntityNotFoundException("Organizzatore non trovato"));
        BeanUtils.copyProperties(eventoRequest, evento);
        evento.setOrganizzatore(organizzatore);
        return eventoRepository.save(evento);
    }
    public Evento modificaEvento(Long id, EventoRequest eventoRequest, AppUser user) throws MessagingException {
        Evento evento = eventoRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("Evento non trovato"));
        String nomeEvento = evento.getNomeEvento();
        OrganizzatoreEventi organizzatore = organizzatoreEventiRepository.findById(user.getId()).orElseThrow(() -> new EntityNotFoundException("Organizzatore non trovato"));
        if (!evento.getOrganizzatore().equals(organizzatore) && !user.getRoles().contains(Role.ROLE_ADMIN)) {
            throw new IllegalArgumentException("Non sei autorizzato a modificare questo evento");
        }

        evento.setNomeEvento(eventoRequest.getNomeEvento());
        evento.setMaxPartecipanti(eventoRequest.getMaxPartecipanti());
        evento.setMinPartecipanti(eventoRequest.getMinPartecipanti());
        evento.setDataEvento(eventoRequest.getDataEvento());
        evento.setNote(eventoRequest.getNote());
        evento.setArtistiPartecipanti(eventoRequest.getArtistiPartecipanti());
        evento.setLuogo(eventoRequest.getLuogo());
        evento.setCitta(eventoRequest.getCitta());
        evento.setTipoEvento(eventoRequest.getTipoEvento());
        evento.setPrezzoBiglietto(eventoRequest.getPrezzoBiglietto());
        evento.setAperturaPorte(eventoRequest.getAperturaPorte());
        evento.setFineEvento(eventoRequest.getFineEvento());
        if (eventoRequest.getLocandina() != null) {
            evento.setLocandina(eventoRequest.getLocandina());
        }
        List<UtenteNormale> partecipanti = evento.getPartecipanti();
        if (partecipanti.size() > 0) {
            for (UtenteNormale partecipante : partecipanti) {
                emailSenderService.sendEmail(partecipante.getEmail(), "Evento modificato", "L'evento " + nomeEvento + " è stato modificato. Ti invitiamo a controllare in piattaforma.");
        }
        }
        return eventoRepository.save(evento);
    }
    public Evento getEvento(Long id) {
        return eventoRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("Evento non trovato"));
    }
    public Page<Evento> getEventi( int page, int size, String sort, EventoFilter filter) {
        Pageable pageable = PageRequest.of(page, size, Sort.by(sort));
        Specification<Evento> spec = EventoSpecifications.filterBy(filter);
        return eventoRepository.findAll(spec, pageable);
    }
    public CommonResponse deleteEvento(Long id, AppUser user) {
        Evento evento = eventoRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("Evento non trovato"));
        Evento datiEvento = new Evento();
        String nomeEvento = evento.getNomeEvento();
        List<UtenteNormale> partecipanti = evento.getPartecipanti();
        if (evento.getOrganizzatore().getId().equals(user.getId())) {
            eventoRepository.deleteById(id);
            prenotazioneEventoRepository.findAllByEventoId(id).forEach(prenotazioneEvento -> prenotazioneEventoRepository.deleteById(prenotazioneEvento.getId()));
            partecipanti.forEach(partecipante -> {
                utenteNormaleRepository.save(partecipante);
                try {
                    emailSenderService.sendEmail(partecipante.getEmail(), "Evento cancellato", "L'evento " + nomeEvento + " è stato cancellato. Ti invitiamo a controllare in piattaforma. Sarai risarcito sul metodo di pagamento utilizzato durante la prenotazione");
                } catch (MessagingException e) {
                    throw new RuntimeException(e);
                }
            });
        } else {
            throw new IllegalArgumentException("Non sei autorizzato a eliminare questo evento");
        }
        return new CommonResponse(id);
    }
    public Page<Evento> getEventiByOrganizzatore(AppUser user, int page, int size, String sort) {
        Pageable pageable = PageRequest.of(page, size, Sort.by(sort));
        return eventoRepository.findByOrganizzatoreId(user.getId(), pageable);
    }
}
