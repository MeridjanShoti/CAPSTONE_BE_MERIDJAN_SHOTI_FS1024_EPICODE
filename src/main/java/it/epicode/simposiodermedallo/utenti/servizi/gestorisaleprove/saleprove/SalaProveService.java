package it.epicode.simposiodermedallo.utenti.servizi.gestorisaleprove.saleprove;

import it.epicode.simposiodermedallo.auth.AppUser;
import it.epicode.simposiodermedallo.common.EmailSenderService;
import it.epicode.simposiodermedallo.utenti.servizi.gestorisaleprove.GestoreSala;
import it.epicode.simposiodermedallo.utenti.servizi.gestorisaleprove.GestoreSalaRepository;
import it.epicode.simposiodermedallo.utenti.servizi.gestorisaleprove.saleprove.prenotazioni.PrenotazioneSalaProve;
import it.epicode.simposiodermedallo.utenti.servizi.gestorisaleprove.saleprove.prenotazioni.PrenotazioneSalaProveRepository;
import jakarta.mail.MessagingException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import java.util.List;
@Slf4j
@Service
@Validated
public class SalaProveService {
    @Autowired
    private SalaProveRepository salaProveRepository;
    @Autowired
    private GestoreSalaRepository gestoreSalaRepository;
    @Autowired
    private PrenotazioneSalaProveRepository prenotazioneSalaProveRepository;
    @Autowired
    private EmailSenderService emailSenderService;
    public SalaProve createSalaProve(SalaProveRequest request, AppUser user) {
        SalaProve salaProve = new SalaProve();
        GestoreSala gestoreSala = gestoreSalaRepository.findById(user.getId()).orElseThrow(() -> new IllegalArgumentException("GestoreSala non trovato"));
        salaProve.setGestoreSala(gestoreSala);
        salaProve.setNomeSala(request.getNomeSala());
        salaProve.setIndirizzoSala(request.getIndirizzoSala());
        salaProve.setCitta(request.getCitta());
        salaProve.setDescrizione(request.getDescrizione());
        salaProve.setRegolamento(request.getRegolamento());
        salaProve.setCapienzaMax(request.getCapienzaMax());
        salaProve.setPrezzoOrario(request.getPrezzoOrario());
        salaProve.setCopertinaSala(request.getCopertinaSala());
        salaProve.setGiorniApertura(request.getGiorniApertura());
        salaProve.setOrarioApertura(request.getOrarioApertura());
        salaProve.setOrarioChiusura(request.getOrarioChiusura());
        Strumentazione strumentazione = new Strumentazione();
        strumentazione.setAmpliETestate(request.getAmpliETestate());
        strumentazione.setMicrofoni(request.getMicrofoni());
        strumentazione.setMixer(request.getMixer());
        strumentazione.setSetBatteria(request.getSetBatteria());
        strumentazione.setAltro(request.getAltro());
        salaProve.setStrumentazione(strumentazione);
        salaProve.setGestoreSala(gestoreSala);
        salaProveRepository.save(salaProve);

        return salaProve;
    }
    public SalaProve updateSalaProve(SalaProveRequest request, Long id, AppUser user) {
        SalaProve salaProve = salaProveRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("SalaProve non trovata"));
        GestoreSala gestoreSala = gestoreSalaRepository.findById(user.getId()).orElseThrow(() -> new IllegalArgumentException("GestoreSala non trovato"));
        if (!salaProve.getGestoreSala().getId().equals(gestoreSala.getId())) {
            throw new IllegalArgumentException("Non sei il gestore di questa sala");
        }
        salaProve.setNomeSala(request.getNomeSala());
        salaProve.setIndirizzoSala(request.getIndirizzoSala());
        salaProve.setCitta(request.getCitta());
        salaProve.setDescrizione(request.getDescrizione());
        salaProve.setRegolamento(request.getRegolamento());
        salaProve.setCapienzaMax(request.getCapienzaMax());
        salaProve.setPrezzoOrario(request.getPrezzoOrario());
        salaProve.setCopertinaSala(request.getCopertinaSala());
        salaProve.setGiorniApertura(request.getGiorniApertura());
        salaProve.setOrarioApertura(request.getOrarioApertura());
        salaProve.setOrarioChiusura(request.getOrarioChiusura());
        Strumentazione strumentazione = new Strumentazione();
        strumentazione.setAmpliETestate(request.getAmpliETestate());
        strumentazione.setMicrofoni(request.getMicrofoni());
        strumentazione.setMixer(request.getMixer());
        strumentazione.setSetBatteria(request.getSetBatteria());
        strumentazione.setAltro(request.getAltro());
        salaProve.setStrumentazione(strumentazione);
        salaProve.setGestoreSala(gestoreSala);
        salaProveRepository.save(salaProve);
        return salaProve;
    }
    public void deleteSalaProve(Long id, AppUser user) {
        SalaProve salaProve = salaProveRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("SalaProve non trovata"));
        GestoreSala gestoreSala = gestoreSalaRepository.findById(user.getId()).orElseThrow(() -> new IllegalArgumentException("GestoreSala non trovato"));
        if (!salaProve.getGestoreSala().getId().equals(gestoreSala.getId())) {
            throw new IllegalArgumentException("Non sei il gestore di questa sala");
        }
        if (prenotazioneSalaProveRepository.existsBySalaProveId(id)) {
            List<PrenotazioneSalaProve> prenotazioni = prenotazioneSalaProveRepository.findAllBySalaProveId(id);
            prenotazioni.forEach(prenotazione -> {
                try {
                    emailSenderService.sendEmail(prenotazione.getUtente().getEmail(), "Prenotazione cancellata", "La prenotazione alla sala prove " + prenotazione.getSalaProve().getNomeSala() + " è stata cancellata");
                    prenotazioneSalaProveRepository.delete(prenotazione);
                } catch (MessagingException e) {
                    log.error("Errore nell'invio dell'email", e);
                }
            });

        }

        salaProveRepository.delete(salaProve);
    }
    public SalaProve getSalaProve(Long id) {
        return salaProveRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("SalaProve non trovata"));
    }
    public Page<SalaProve> getAllSaleProve(int page, int size, Sort sort, SalaProveFilter filter) {
        Pageable pageable = PageRequest.of(page, size, sort);
        Specification<SalaProve> spec = SalaProveSpecifications.filterBy(filter);
        return salaProveRepository.findAll(spec, pageable);
    }
    public Page<SalaProve> getAllSaleProveByGestoreSala(AppUser user, int page, int size, Sort sort) {
        Pageable pageable = PageRequest.of(page, size, sort);
        return salaProveRepository.findAllByGestoreSalaId(user.getId(), pageable);
    }
}
