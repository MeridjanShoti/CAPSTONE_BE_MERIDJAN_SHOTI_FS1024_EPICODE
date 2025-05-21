package it.epicode.simposiodermedallo.utenti.servizi.organizzatoreeventi.eventi;

import it.epicode.simposiodermedallo.auth.AppUser;
import it.epicode.simposiodermedallo.auth.Role;
import it.epicode.simposiodermedallo.common.CommonResponse;
import it.epicode.simposiodermedallo.common.EmailSenderService;
import it.epicode.simposiodermedallo.utenti.persone.utentinormali.UtenteNormale;
import it.epicode.simposiodermedallo.utenti.servizi.organizzatoreeventi.OrganizzatoreEventi;
import it.epicode.simposiodermedallo.utenti.servizi.organizzatoreeventi.OrganizzatoreEventiRepository;
import jakarta.mail.MessagingException;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

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

    public Evento creaEvento(EventoRequest eventoRequest, AppUser user) {
        Evento evento = new Evento();
        OrganizzatoreEventi organizzatore = organizzatoreEventiRepository.findById(user.getId()).orElseThrow(() -> new EntityNotFoundException("Organizzatore non trovato"));
        BeanUtils.copyProperties(eventoRequest, evento);
        evento.setOrganizzatore(organizzatore);
        return eventoRepository.save(evento);
    }
    public Evento modificaEvento(Long id, EventoRequest eventoRequest, AppUser user) throws MessagingException {
        Evento evento = eventoRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("Evento non trovato"));
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
                emailSenderService.sendEmail(partecipante.getEmail(), "Evento modificato", "L'evento " + evento.getNomeEvento() + " è stato modificato. Ti invitiamo a controllare in piattaforma.");
        }
        }
        return eventoRepository.save(evento);
    }
    public Evento getEvento(Long id) {
        return eventoRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("Evento non trovato"));
    }
    public Page<Evento> getEventi( int page, int size, String sort) {
        Pageable pageable = PageRequest.of(page, size, Sort.by(sort));
        return eventoRepository.findAll(pageable);
    }
    public CommonResponse deleteEvento(Long id, AppUser user) {
        Evento evento = eventoRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("Evento non trovato"));
        if (evento.getOrganizzatore().getId().equals(user.getId())) {
            eventoRepository.deleteById(id);
        }
        return new CommonResponse(id);
    }
    public Page<Evento> getEventiByOrganizzatore(AppUser user, int page, int size, String sort) {
        Pageable pageable = PageRequest.of(page, size, Sort.by(sort));
        return eventoRepository.findByOrganizzatoreId(user.getId(), pageable);
    }
}
