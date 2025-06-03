package it.epicode.simposiodermedallo.segnalazioni;

import it.epicode.simposiodermedallo.auth.AppUserRepository;
import it.epicode.simposiodermedallo.commenti.CommentoRepository;
import it.epicode.simposiodermedallo.common.EmailSenderService;
import it.epicode.simposiodermedallo.recensioni.sale.RecensioneSalaRepository;
import it.epicode.simposiodermedallo.recensioni.scuole.RecensioneScuolaRepository;
import it.epicode.simposiodermedallo.utenti.persone.utentinormali.UtenteNormale;
import it.epicode.simposiodermedallo.utenti.persone.utentinormali.UtenteNormaleRepository;
import it.epicode.simposiodermedallo.utenti.persone.utentinormali.UtenteNormaleService;
import it.epicode.simposiodermedallo.utenti.servizi.gestorisaleprove.saleprove.prenotazioni.PrenotazioneSalaProve;
import it.epicode.simposiodermedallo.utenti.servizi.gestorisaleprove.saleprove.prenotazioni.PrenotazioneSalaProveRepository;
import it.epicode.simposiodermedallo.utenti.servizi.organizzatoreeventi.eventi.Evento;
import it.epicode.simposiodermedallo.utenti.servizi.organizzatoreeventi.eventi.EventoRepository;
import it.epicode.simposiodermedallo.utenti.servizi.organizzatoreeventi.eventi.prenotazioni.PrenotazioneEventoRepository;
import it.epicode.simposiodermedallo.utenti.servizi.scuole.corsi.iscrizioni.IscrizioneRepository;
import jakarta.mail.MessagingException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
@Slf4j
@Service
public class SegnalazioneService {
    @Autowired
    private SegnalazioneRepository segnalazioneRepository;
    @Autowired
    private CommentoRepository commentoRepository;
    @Autowired
    private AppUserRepository appUserRepository;
    @Autowired
    private UtenteNormaleRepository utenteNormaleRepository;
    @Autowired
    private IscrizioneRepository iscrizioneRepository;
    @Autowired
    private PrenotazioneSalaProveRepository prenotazioneSalaProveRepository;
    @Autowired
    private PrenotazioneEventoRepository prenotazioneEventoRepository;
    @Autowired
    private RecensioneSalaRepository recensioneSalaRepository;
    @Autowired
    private RecensioneScuolaRepository recensioneScuolaRepository;
    @Autowired
    private EmailSenderService emailSenderService;
    @Autowired
    private EventoRepository eventoRepository;
    public Segnalazione segnala(Segnalazione segnalazione) {
        return segnalazioneRepository.save(segnalazione);
    }
    public Segnalazione findById(Long id) {
        return segnalazioneRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("Segnalazione non trovata"));
    }
    public void delete(Long id) {
        segnalazioneRepository.deleteById(id);
    }
    @Transactional
    public void approve(Long id) {
        Segnalazione segnalazione = segnalazioneRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("Segnalazione non trovata"));
        if (segnalazione.getTipoSegnalazione() == TipoSegnalazione.COMMENTO) {
            commentoRepository.deleteById(segnalazione.getIdElemento());
        } else if (segnalazione.getTipoSegnalazione() == TipoSegnalazione.UTENTE){
            segnalazioneRepository.findAllByAutoreId(segnalazione.getIdElemento()).forEach(s -> {
                segnalazioneRepository.deleteById(s.getId());
            });
            commentoRepository.findAllByAutoreId(segnalazione.getIdElemento()).forEach(commentoRepository::delete);
            recensioneSalaRepository.findAllByAutoreId(segnalazione.getIdElemento()).forEach(recensioneSalaRepository::delete);
            recensioneScuolaRepository.findAllByAutoreId(segnalazione.getIdElemento()).forEach(recensioneScuolaRepository::delete);
            iscrizioneRepository.findAllByUtenteId(segnalazione.getIdElemento()).forEach(iscrizioneRepository::delete);
            prenotazioneSalaProveRepository.findAllByUtenteId(segnalazione.getIdElemento()).forEach(prenotazioneSalaProveRepository::delete);
            prenotazioneEventoRepository.findAllByUtenteId(segnalazione.getIdElemento()).forEach(prenotazioneEventoRepository::delete);
            appUserRepository.deleteById(segnalazione.getIdElemento());
            utenteNormaleRepository.deleteById(segnalazione.getIdElemento());
            segnalazioneRepository.deleteById(segnalazione.getId());

        } else if (segnalazione.getTipoSegnalazione() == TipoSegnalazione.EVENTO){
            Evento evento = eventoRepository.findById(segnalazione.getIdElemento()).orElseThrow(() -> new IllegalArgumentException("Evento non trovato"));
            commentoRepository.findAllByEventoId(segnalazione.getIdElemento()).forEach(commentoRepository::delete);
            prenotazioneEventoRepository.findAllByEventoId(segnalazione.getIdElemento()).forEach(e -> {
                try {
                    emailSenderService.sendEmail(e.getUtenteNormale().getEmail(), "Evento cancellato", "L'evento " + e.getEvento().getNomeEvento() + " è stato cancellato. Ti invitiamo a controllare in piattaforma. Sarai risarcito sul metodo di pagamento utilizzato durante la prenotazione");
                    prenotazioneEventoRepository.delete(e);
                } catch (MessagingException ex) {
                    log.error("Errore durante l'invio dell'email a {} per l'evento {}", e.getUtenteNormale().getEmail(), e.getEvento().getNomeEvento(), ex);
                }
            });
            try {
                emailSenderService.sendEmail(evento.getOrganizzatore().getEmail(), "Evento cancellato", "L'evento " + evento.getNomeEvento() + " è stato cancellato dopo aver controllato le segnalazioni. Gli utenti verranno risarciti");
            } catch (MessagingException e) {
                log.error("Errore durante l'invio dell'email a {} per l'evento {}", evento.getOrganizzatore().getEmail(), evento.getNomeEvento(), e);
            }
            eventoRepository.deleteById(segnalazione.getIdElemento());
            segnalazioneRepository.deleteById(segnalazione.getId());
            
        } else if (segnalazione.getTipoSegnalazione() == TipoSegnalazione.SALA){
            //eliminazione sala
        } else if (segnalazione.getTipoSegnalazione() == TipoSegnalazione.SCUOLA){
            //eliminazione scuola
        } else if (segnalazione.getTipoSegnalazione() == TipoSegnalazione.ORGANIZZATORE){
            //eliminazione organizzatore
        } else if (segnalazione.getTipoSegnalazione() == TipoSegnalazione.INSEGNANTE) {
            //eliminazione insegnante
        } else if (segnalazione.getTipoSegnalazione() == TipoSegnalazione.RECENSIONE_SALA) {
            //eliminazione recensione
        } else if (segnalazione.getTipoSegnalazione() == TipoSegnalazione.RECENSIONE_SCUOLA) {
            //eliminazione recensione
        } else if (segnalazione.getTipoSegnalazione() == TipoSegnalazione.CORSO) {
            //eliminazione corso
        }
        segnalazioneRepository.save(segnalazione);
    }
}
