package it.epicode.simposiodermedallo.segnalazioni;

import it.epicode.simposiodermedallo.auth.AppUserRepository;
import it.epicode.simposiodermedallo.commenti.CommentoRepository;
import it.epicode.simposiodermedallo.common.EmailSenderService;
import it.epicode.simposiodermedallo.recensioni.sale.RecensioneSalaRepository;
import it.epicode.simposiodermedallo.recensioni.scuole.RecensioneScuolaRepository;
import it.epicode.simposiodermedallo.utenti.persone.insegnanti.Insegnante;
import it.epicode.simposiodermedallo.utenti.persone.insegnanti.InsegnanteRepository;
import it.epicode.simposiodermedallo.utenti.persone.utentinormali.UtenteNormale;
import it.epicode.simposiodermedallo.utenti.persone.utentinormali.UtenteNormaleRepository;
import it.epicode.simposiodermedallo.utenti.persone.utentinormali.UtenteNormaleService;
import it.epicode.simposiodermedallo.utenti.servizi.gestorisaleprove.GestoreSala;
import it.epicode.simposiodermedallo.utenti.servizi.gestorisaleprove.GestoreSalaRepository;
import it.epicode.simposiodermedallo.utenti.servizi.gestorisaleprove.saleprove.SalaProve;
import it.epicode.simposiodermedallo.utenti.servizi.gestorisaleprove.saleprove.SalaProveRepository;
import it.epicode.simposiodermedallo.utenti.servizi.gestorisaleprove.saleprove.prenotazioni.PrenotazioneSalaProve;
import it.epicode.simposiodermedallo.utenti.servizi.gestorisaleprove.saleprove.prenotazioni.PrenotazioneSalaProveRepository;
import it.epicode.simposiodermedallo.utenti.servizi.organizzatoreeventi.OrganizzatoreEventi;
import it.epicode.simposiodermedallo.utenti.servizi.organizzatoreeventi.OrganizzatoreEventiRepository;
import it.epicode.simposiodermedallo.utenti.servizi.organizzatoreeventi.eventi.Evento;
import it.epicode.simposiodermedallo.utenti.servizi.organizzatoreeventi.eventi.EventoRepository;
import it.epicode.simposiodermedallo.utenti.servizi.organizzatoreeventi.eventi.prenotazioni.PrenotazioneEventoRepository;
import it.epicode.simposiodermedallo.utenti.servizi.scuole.Scuola;
import it.epicode.simposiodermedallo.utenti.servizi.scuole.ScuolaRepository;
import it.epicode.simposiodermedallo.utenti.servizi.scuole.corsi.Corso;
import it.epicode.simposiodermedallo.utenti.servizi.scuole.corsi.CorsoRepository;
import it.epicode.simposiodermedallo.utenti.servizi.scuole.corsi.iscrizioni.IscrizioneRepository;
import jakarta.mail.MessagingException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

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
    @Autowired
    private SalaProveRepository salaProveRepository;
    @Autowired
    private GestoreSalaRepository gestoreSalaRepository;
    @Autowired
    private ScuolaRepository scuolaRepository;
    @Autowired
    private InsegnanteRepository insegnanteRepository;
    @Autowired
    private CorsoRepository corsoRepository;
    @Autowired
    private OrganizzatoreEventiRepository organizzatoreEventiRepository;

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
            prenotazioneEventoRepository.findAllByUtenteNormaleId(segnalazione.getIdElemento()).forEach(prenotazioneEventoRepository::delete);
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
            SalaProve  salaProve = salaProveRepository.findById(segnalazione.getIdElemento()).orElseThrow(() -> new IllegalArgumentException("Sala non trovata"));
            GestoreSala gestoreSala = salaProve.getGestoreSala();
            recensioneSalaRepository.findAllBySalaProveId(segnalazione.getIdElemento()).forEach(recensioneSalaRepository::delete);
            prenotazioneSalaProveRepository.findAllBySalaProveId(segnalazione.getIdElemento()).forEach(p -> {
                try {
                    emailSenderService.sendEmail(p.getUtente().getEmail(), "Prenotazione cancellata", "La prenotazione alla sala prove " + p.getSalaProve().getNomeSala() + " è stata cancellata");
                    prenotazioneSalaProveRepository.delete(p);
                } catch (MessagingException ex) {
                    log.error("Errore durante l'invio dell'email a {} per la sala {}", p.getUtente().getEmail(), p.getSalaProve().getNomeSala(), ex);
                }
            });
            try {
                emailSenderService.sendEmail(gestoreSala.getEmail(), "Sala cancellata", "La sala " + salaProveRepository.findById(segnalazione.getIdElemento()).orElseThrow(() -> new IllegalArgumentException("Sala non trovata")).getNomeSala() + " è stata cancellata dopo aver controllato le segnalazioni. Gli utenti verranno risarciti");
            } catch (MessagingException e) {
                log.error("Errore durante l'invio dell'email a {} per la sala {}", gestoreSala.getEmail(), salaProveRepository.findById(segnalazione.getIdElemento()).orElseThrow(() -> new IllegalArgumentException("Sala non trovata")).getNomeSala(), e);
            }
            salaProveRepository.deleteById(segnalazione.getIdElemento());
            segnalazioneRepository.deleteById(segnalazione.getId());

        } else if (segnalazione.getTipoSegnalazione() == TipoSegnalazione.SCUOLA){
            segnalazioneRepository.findAllByAutoreId(segnalazione.getIdElemento()).forEach(segnalazioneRepository::delete);
            Scuola scuola = scuolaRepository.findById(segnalazione.getIdElemento()).orElseThrow(() -> new IllegalArgumentException("Scuola non trovata"));
            recensioneScuolaRepository.findAllByScuolaId(segnalazione.getIdElemento()).forEach(recensioneScuolaRepository::delete);
            try {
                emailSenderService.sendEmail(scuola.getEmail(), "Scuola cancellata", "La scuola " + scuola.getRagioneSociale() + " è stata cancellata dopo aver controllato le segnalazioni. Gli utenti iscritti ai corsi verranno risarciti");
            } catch (MessagingException e) {
                log.error("Errore durante l'invio dell'email a {} per la scuola {}", scuola.getEmail(), scuola.getRagioneSociale(), e);
            }
            insegnanteRepository.findAllByScuolaId( segnalazione.getIdElemento()).forEach(insegnante -> {
                corsoRepository.findAllByInsegnanteId(insegnante.getId()).forEach(corso -> {
                    corso.setInsegnante(null);
                    corsoRepository.save(corso);
                });
                insegnanteRepository.deleteById(insegnante.getId());
            });
            corsoRepository.findAllByScuolaId(segnalazione.getIdElemento()).forEach(corso -> {
                iscrizioneRepository.findAllByCorsoId(corso.getId()).forEach(i -> {
                    try {
                        emailSenderService.sendEmail(i.getUtente().getEmail(), "Corso cancellato", "Il corso " + corso.getNomeCorso() + " è stato cancellato. Ti invitiamo a controllare in piattaforma");
                    } catch (MessagingException e) {
                        log.error("Errore durante l'invio dell'email a {} per il corso {}", i.getUtente().getEmail(), corso.getNomeCorso(), e);
                    }
                    iscrizioneRepository.delete(i);
                });
                corsoRepository.deleteById(corso.getId());
            });
            scuolaRepository.deleteById(segnalazione.getIdElemento());
            segnalazioneRepository.deleteById(segnalazione.getId());

        } else if (segnalazione.getTipoSegnalazione() == TipoSegnalazione.ORGANIZZATORE){
            segnalazioneRepository.findAllByAutoreId(segnalazione.getIdElemento()).forEach(segnalazioneRepository::delete);
            OrganizzatoreEventi  organizzatoreEventi = organizzatoreEventiRepository.findById(segnalazione.getIdElemento()).orElseThrow(() -> new IllegalArgumentException("Organizzatore non trovato"));
            List<Evento> eventi = eventoRepository.findAllByOrganizzatoreId(segnalazione.getIdElemento());
            eventi.forEach(evento -> {
                commentoRepository.findAllByEventoId(evento.getId()).forEach(commentoRepository::delete);
                prenotazioneEventoRepository.findAllByEventoId(evento.getId()).forEach(p ->
                {
                    try {
                        emailSenderService.sendEmail(p.getUtenteNormale().getEmail(), "Evento cancellato", "L'evento " + p.getEvento().getNomeEvento() + " è stato cancellato. Ti invitiamo a controllare in piattaforma. Sarai risarcito sul metodo di pagamento utilizzato durante la prenotazione");
                    } catch (MessagingException e) {
                        log.error("Errore durante l'invio dell'email a {} per l'evento {}", p.getUtenteNormale().getEmail(), p.getEvento().getNomeEvento(), e);
                    }
                    prenotazioneEventoRepository.delete(p);
                }
                );
                eventoRepository.deleteById(evento.getId());
            });
            organizzatoreEventiRepository.deleteById(segnalazione.getIdElemento());
            segnalazioneRepository.deleteById(segnalazione.getId());
        } else if (segnalazione.getTipoSegnalazione() == TipoSegnalazione.INSEGNANTE) {
            segnalazioneRepository.findAllByAutoreId(segnalazione.getIdElemento()).forEach(segnalazioneRepository::delete);
            Insegnante insegnante = insegnanteRepository.findById(segnalazione.getIdElemento()).orElseThrow(() -> new IllegalArgumentException("Insegnante non trovato"));
            Scuola scuola = insegnante.getScuola();
            corsoRepository.findAllByInsegnanteId(segnalazione.getIdElemento()).forEach(corso -> {
                corso.setInsegnante(null);
                corsoRepository.save(corso);
            });
            try {
                emailSenderService.sendEmail(scuola.getEmail(), "Insegnante cancellato", "L'insegnante " + insegnante.getNome() + " " + insegnante.getCognome() + " è stato cancellato dopo aver controllato le segnalazioni.");
            } catch (MessagingException e) {
                log.error("Errore durante l'invio dell'email a {} per l'insegnante {}", scuola.getEmail(), insegnante.getNome() + " " + insegnante.getCognome(), e);
            }
            insegnanteRepository.deleteById(segnalazione.getIdElemento());
            segnalazioneRepository.deleteById(segnalazione.getId());
        } else if (segnalazione.getTipoSegnalazione() == TipoSegnalazione.RECENSIONE_SALA) {
            recensioneSalaRepository.deleteById(segnalazione.getIdElemento());
        } else if (segnalazione.getTipoSegnalazione() == TipoSegnalazione.RECENSIONE_SCUOLA) {
            recensioneScuolaRepository.deleteById(segnalazione.getIdElemento());
        } else if (segnalazione.getTipoSegnalazione() == TipoSegnalazione.CORSO) {
            Corso corso = corsoRepository.findById(segnalazione.getIdElemento()).orElseThrow(() -> new IllegalArgumentException("Corso non trovato"));
            corso.getPartecipanti().forEach(utenteNormale -> {
                try {
                    emailSenderService.sendEmail(utenteNormale.getEmail(), "Corso cancellato", "Il corso " + corso.getNomeCorso() + "è stato cancellato");
                } catch (MessagingException e) {
                    log.error("Errore durante l'invio dell'email a {} per il corso {}", utenteNormale.getEmail(), corso.getNomeCorso(), e);
                }
            });
            iscrizioneRepository.findAllByCorsoId(segnalazione.getIdElemento()).forEach(iscrizioneRepository::delete);
            corsoRepository.deleteById(segnalazione.getIdElemento());
            segnalazioneRepository.deleteById(segnalazione.getId());
        } else if (segnalazione.getTipoSegnalazione() == TipoSegnalazione.GESTORE_SP) {
            segnalazioneRepository.findAllByAutoreId(segnalazione.getIdElemento()).forEach(segnalazioneRepository::delete);
            GestoreSala gestoreSala = gestoreSalaRepository.findById(segnalazione.getIdElemento()).orElseThrow(() -> new IllegalArgumentException("Gestore non trovato"));
            salaProveRepository.findAllByGestoreSalaId(segnalazione.getIdElemento()).forEach(salaProve -> {
                recensioneSalaRepository.findAllBySalaProveId(salaProve.getId()).forEach(recensioneSalaRepository::delete);
                prenotazioneSalaProveRepository.findAllBySalaProveId(salaProve.getId()).forEach( p -> {
                    try {
                        emailSenderService.sendEmail(p.getUtente().getEmail(), "Sala cancellata", "La sala " + salaProve.getNomeSala() + " è stata cancellata dopo aver controllato le segnalazioni. Gli utenti verranno risarciti");
                    } catch (MessagingException e) {
                        log.error("Errore durante l'invio dell'email a {} per la sala {}", p.getUtente().getEmail(), salaProve.getNomeSala(), e);
                    }
                    prenotazioneSalaProveRepository.delete(p);
                });
                salaProveRepository.deleteById(salaProve.getId());
            });
            gestoreSalaRepository.deleteById(segnalazione.getIdElemento());
            segnalazioneRepository.deleteById(segnalazione.getId());
        }
        List<Segnalazione> segnalazioniDaEliminare = segnalazioneRepository.findAllByTipoSegnalazioneAndIdElemento(segnalazione.getTipoSegnalazione(), segnalazione.getIdElemento());
        segnalazioniDaEliminare.forEach(segnalazioneRepository::delete);
    }
    public Page<Segnalazione> getAllSegnalazioni(int page, int size, String sort, String sortDir, String autore, TipoSegnalazione  tipoSegnalazione) {
        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.fromString(sortDir), sort));
        SegnalazioneFilter filter = new SegnalazioneFilter();
        filter.setAutore(autore);
        filter.setTipoSegnalazione(tipoSegnalazione);
        Specification<Segnalazione> spec = SegnalazioneSpecifications.filterBy(filter);
        return segnalazioneRepository.findAll( spec, pageable);
    }
}
