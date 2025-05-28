package it.epicode.simposiodermedallo.auth;

import com.github.javafaker.Faker;
import it.epicode.simposiodermedallo.utenti.Utente;
import it.epicode.simposiodermedallo.utenti.persone.insegnanti.Insegnante;
import it.epicode.simposiodermedallo.utenti.persone.insegnanti.InsegnanteRepository;
import it.epicode.simposiodermedallo.utenti.persone.utentinormali.UtenteNormale;
import it.epicode.simposiodermedallo.utenti.persone.utentinormali.UtenteNormaleRepository;
import it.epicode.simposiodermedallo.utenti.servizi.gestorisaleprove.GestoreSala;
import it.epicode.simposiodermedallo.utenti.servizi.gestorisaleprove.GestoreSalaRepository;
import it.epicode.simposiodermedallo.utenti.servizi.gestorisaleprove.saleprove.SalaProve;
import it.epicode.simposiodermedallo.utenti.servizi.gestorisaleprove.saleprove.SalaProveRepository;
import it.epicode.simposiodermedallo.utenti.servizi.gestorisaleprove.saleprove.Strumentazione;
import it.epicode.simposiodermedallo.utenti.servizi.organizzatoreeventi.OrganizzatoreEventi;
import it.epicode.simposiodermedallo.utenti.servizi.organizzatoreeventi.OrganizzatoreEventiRepository;
import it.epicode.simposiodermedallo.utenti.servizi.organizzatoreeventi.eventi.Evento;
import it.epicode.simposiodermedallo.utenti.servizi.organizzatoreeventi.eventi.EventoRepository;
import it.epicode.simposiodermedallo.utenti.servizi.organizzatoreeventi.eventi.TipoEvento;
import it.epicode.simposiodermedallo.utenti.servizi.scuole.Scuola;
import it.epicode.simposiodermedallo.utenti.servizi.scuole.ScuolaRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Locale;
import java.util.Optional;
import java.util.Set;
@Slf4j
@Component
public class AuthRunner implements ApplicationRunner {
    @Autowired
    private AppUserService appUserService;
    @Autowired
    private AppUserRepository appUserRepository;
    @Autowired
    UtenteNormaleRepository utenteNormaleRepository;
    @Autowired
    ScuolaRepository scuolaRepository;
    @Autowired
    InsegnanteRepository insegnanteRepository;
    @Autowired
    GestoreSalaRepository gestoreSalaRepository;
    @Autowired
    OrganizzatoreEventiRepository organizzatoreEventiRepository;
    @Autowired
    EventoRepository eventoRepository;
    @Autowired
    Faker faker;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private SalaProveRepository salaProveRepository;
    @Override
    public void run(ApplicationArguments args) throws Exception {
        if (!appUserRepository.existsByRolesContaining(Role.ROLE_ADMIN)) {
            appUserService.registerUser("admin", "adminpwd", Set.of(Role.ROLE_ADMIN));
        }


        if (!appUserRepository.existsByRolesContaining(Role.ROLE_USER)) {
            for (int i = 0; i < 10; i++) {
                try {
                    UtenteNormale utente = new UtenteNormale();
                    String nome = faker.name().firstName();
                    String cognome = faker.name().lastName();
                    String email = (nome + cognome + "@email.com").toLowerCase(Locale.ROOT).replaceAll("[^a-z0-9_@.]", "");
                    LocalDate dataNascita = LocalDate.of(faker.number().numberBetween(1950, 2000), faker.number().numberBetween(1, 12), faker.number().numberBetween(1, 28));
                    String username = (nome + "." + cognome + faker.number().digits(2)).toLowerCase(Locale.ROOT).replaceAll("[^a-z0-9]", "");
                    AppUser appUser = new AppUser();
                    appUser.setUsername(username);
                    appUser.setPassword(passwordEncoder.encode("userpwd"));
                    appUser.setRoles(Set.of(Role.ROLE_USER));
                    utente.setNome(nome);
                    utente.setCognome(cognome);
                    utente.setEmail(email);
                    utente.setDataNascita(dataNascita);
                    utente.setBio(faker.lorem().paragraph());
                    utente.setAvatar("https://ui-avatars.com/api/?name=" + utente.getNome() + "+" + utente.getCognome());
                    utente.setCopertina("https://m.media-amazon.com/images/S/pv-target-images/fb3e18571fefeb973850615b530a8c6440fc338e33b00438d52152f38f58fca8._SX1080_FMjpg_.jpg");
                    utente.setDataRegistrazione(LocalDate.now());
                    utente.setAppUser(appUser);
                    utente.setId(appUser.getId());
                    utenteNormaleRepository.save(utente);
                } catch (Exception e) {
                    log.error(e.getMessage());
                }

            }
        }
        if (!appUserRepository.existsByRolesContaining(Role.ROLE_SCUOLA)) {
            for (int i = 0; i < 10; i++) {
                try {
                    Scuola scuola = new Scuola();
                    AppUser appUser = new AppUser();
                    String nomeScuola = faker.funnyName().name();
                    String email = (nomeScuola + "@email.com").toLowerCase(Locale.ROOT).replaceAll("[^a-z0-9_@.]", "");
                    String username = (nomeScuola + faker.number().digits(2)).toLowerCase(Locale.ROOT).replaceAll("[^a-z0-9]", "");
                    appUser.setUsername(username);
                    appUser.setPassword(passwordEncoder.encode("scuolapwd"));
                    appUser.setRoles(Set.of(Role.ROLE_SCUOLA));
                    scuola.setEmail(email);
                    scuola.setDataRegistrazione(LocalDate.now());
                    scuola.setBio(faker.lorem().paragraph());
                    scuola.setRagioneSociale("Scuola " + nomeScuola);
                    scuola.setAvatar("https://ui-avatars.com/api/?name=" + scuola.getRagioneSociale().replaceAll("[^a-zA-Z0-9]", "+"));
                    scuola.setCopertina("https://m.media-amazon.com/images/S/pv-target-images/fb3e18571fefeb973850615b530a8c6440fc338e33b00438d52152f38f58fca8._SX1080_FMjpg_.jpg");
                    scuola.setIndirizzoPrincipale(faker.address().streetAddress());
                    scuola.setAltreSedi(List.of(faker.address().streetAddress(), faker.address().streetAddress(), faker.address().streetAddress()));
                    scuola.setNumeroTelefono(faker.phoneNumber().cellPhone());
                    scuola.setLinkSocial(List.of(faker.internet().url(), faker.internet().url(), faker.internet().url()));
                    scuola.setPartitaIva(faker.idNumber().ssnValid());
                    scuola.setAppUser(appUser);
                    scuola.setId(appUser.getId());
                    scuolaRepository.save(scuola);
                    for (int j = 0; j < 3; j++) {
                        try {
                            Insegnante insegnante = new Insegnante();
                            String nome = faker.name().firstName();
                            String cognome = faker.name().lastName();
                            String emailInsegnante = (nome + cognome + "@email.com").toLowerCase(Locale.ROOT).replaceAll("[^a-z0-9_@.]", "");
                            LocalDate dataNascita = LocalDate.of(faker.number().numberBetween(1950, 2000), faker.number().numberBetween(1, 12), faker.number().numberBetween(1, 28));
                            String usernameInsegnante = (nome + "." + cognome + faker.number().digits(2)).toLowerCase(Locale.ROOT).replaceAll("[^a-z0-9]", "");
                            AppUser appUserInsegnante = new AppUser();
                            appUserInsegnante.setUsername(usernameInsegnante);
                            appUserInsegnante.setPassword(passwordEncoder.encode("insegnantepwd"));
                            appUserInsegnante.setRoles(Set.of(Role.ROLE_INSEGNANTE));
                            insegnante.setNome(nome);
                            insegnante.setCognome(cognome);
                            insegnante.setEmail(emailInsegnante);
                            insegnante.setDataNascita(dataNascita);
                            insegnante.setBio(faker.lorem().paragraph());
                            insegnante.setAvatar("https://ui-avatars.com/api/?name=" + insegnante.getNome() + "+" + insegnante.getCognome());
                            insegnante.setCopertina("https://m.media-amazon.com/images/S/pv-target-images/fb3e18571fefeb973850615b530a8c6440fc338e33b00438d52152f38f58fca8._SX1080_FMjpg_.jpg");
                            insegnante.setDataRegistrazione(LocalDate.now());
                            insegnante.setAppUser(appUserInsegnante);
                            insegnante.setId(appUserInsegnante.getId());
                            insegnante.setPagaOraria(faker.number().numberBetween(10, 30));
                            insegnante.setScuola(scuola);
                            insegnante.setStrumenti(List.of(faker.music().instrument(), faker.music().instrument(), faker.music().instrument()));
                            insegnanteRepository.save(insegnante);
                            scuolaRepository.save(scuola);
                        } catch (Exception e) {
                            log.error(e.getMessage());
                        }
                    }
                } catch (Exception e) {
                    log.error(e.getMessage());
                }

            }
        }
        if (!appUserRepository.existsByRolesContaining(Role.ROLE_ORGANIZZATORE)) {
            for (int i = 0; i < 10; i++) {
                try {
                    OrganizzatoreEventi organizzatoreEventi = new OrganizzatoreEventi();
                    AppUser appUser = new AppUser();
                    String nomeOrganizzatore = faker.funnyName().name();
                    String email = (nomeOrganizzatore + "@email.com").toLowerCase(Locale.ROOT).replaceAll("[^a-z0-9_@.]", "");
                    String username = (nomeOrganizzatore + faker.number().digits(2)).toLowerCase(Locale.ROOT).replaceAll("[^a-z0-9]", "");
                    appUser.setUsername(username);
                    appUser.setPassword(passwordEncoder.encode("organizzatorepwd"));
                    appUser.setRoles(Set.of(Role.ROLE_ORGANIZZATORE));
                    organizzatoreEventi.setEmail(email);
                    organizzatoreEventi.setDataRegistrazione(LocalDate.now());
                    organizzatoreEventi.setBio(faker.lorem().paragraph());
                    organizzatoreEventi.setRagioneSociale(nomeOrganizzatore + " Eventi");
                    organizzatoreEventi.setAvatar("https://ui-avatars.com/api/?name=" + organizzatoreEventi.getRagioneSociale().replaceAll("[^a-zA-Z0-9]", "+"));
                    organizzatoreEventi.setCopertina("https://m.media-amazon.com/images/S/pv-target-images/fb3e18571fefeb973850615b530a8c6440fc338e33b00438d52152f38f58fca8._SX1080_FMjpg_.jpg");
                    organizzatoreEventi.setIndirizzoPrincipale(faker.address().streetAddress());
                    organizzatoreEventi.setAltreSedi(List.of(faker.address().streetAddress(), faker.address().streetAddress(), faker.address().streetAddress()));
                    organizzatoreEventi.setNumeroTelefono(faker.phoneNumber().cellPhone());
                    organizzatoreEventi.setLinkSocial(List.of(faker.internet().url(), faker.internet().url(), faker.internet().url()));
                    organizzatoreEventi.setPartitaIva(faker.idNumber().ssnValid());
                    organizzatoreEventi.setAppUser(appUser);
                    organizzatoreEventi.setId(appUser.getId());
                    organizzatoreEventiRepository.save(organizzatoreEventi);
                    for (int j = 0; j < 20; j++) {
                        try {
                            Evento evento = new Evento();
                            evento.setNomeEvento(faker.funnyName().name() + faker.music().genre());
                            evento.setMaxPartecipanti(faker.number().numberBetween(50, 1000));
                            evento.setMinPartecipanti(faker.number().numberBetween(1, 10));
                            evento.setDataEvento(LocalDate.of(faker.number().numberBetween(2025, 2026), faker.number().numberBetween(1, 12), faker.number().numberBetween(1, 28)));
                            evento.setNote(faker.lorem().paragraph());
                            evento.setArtistiPartecipanti(List.of(faker.name().fullName(), faker.name().username(), faker.dragonBall().character()));
                            evento.setLuogo(faker.address().streetAddress());
                            evento.setCitta(faker.address().city());
                            evento.setTipoEvento(faker.options().option(TipoEvento.values()));
                            evento.setPrezzoBiglietto(faker.number().numberBetween(10, 30));
                            evento.setAperturaPorte(LocalTime.of(faker.number().numberBetween(9, 22), faker.number().numberBetween(0, 59)));
                            evento.setFineEvento(LocalTime.of(faker.number().numberBetween(0, 23), faker.number().numberBetween(0, 59)));
                            evento.setLocandina("https://edit.org/img/blog/2019060514-cool-retro-concert-poster.webp");
                            evento.setOrganizzatore(organizzatoreEventi);
                            eventoRepository.save(evento);
                            organizzatoreEventiRepository.save(organizzatoreEventi);
                        } catch (Exception e) {
                            log.error(e.getMessage());
                        }
                    }
                } catch (Exception e) {
                    log.error(e.getMessage());
                }
            }
        }
        if (!appUserRepository.existsByRolesContaining(Role.ROLE_GESTORE_SP)) {
            for (int i = 0; i < 10; i++) {
                try {
                    GestoreSala gestoreSala = new GestoreSala();
                    AppUser appUser = new AppUser();
                    String nomeGestore = faker.funnyName().name();
                    String email = (nomeGestore + "@email.com").toLowerCase(Locale.ROOT).replaceAll("[^a-z0-9_@.]", "");
                    String username = (nomeGestore + faker.number().digits(2)).toLowerCase(Locale.ROOT).replaceAll("[^a-z0-9]", "");
                    appUser.setUsername(username);
                    appUser.setPassword(passwordEncoder.encode("gestorepwd"));
                    appUser.setRoles(Set.of(Role.ROLE_GESTORE_SP));
                    gestoreSala.setEmail(email);
                    gestoreSala.setDataRegistrazione(LocalDate.now());
                    gestoreSala.setBio(faker.lorem().paragraph());
                    gestoreSala.setRagioneSociale("Sala Prove" + nomeGestore);
                    gestoreSala.setAvatar("https://ui-avatars.com/api/?name=" + gestoreSala.getRagioneSociale().replaceAll("[^a-zA-Z0-9]", "+"));
                    gestoreSala.setCopertina("https://m.media-amazon.com/images/S/pv-target-images/fb3e18571fefeb973850615b530a8c6440fc338e33b00438d52152f38f58fca8._SX1080_FMjpg_.jpg");
                    gestoreSala.setIndirizzoPrincipale(faker.address().streetAddress());
                    gestoreSala.setAltreSedi(List.of(faker.address().streetAddress(), faker.address().streetAddress(), faker.address().streetAddress()));
                    gestoreSala.setNumeroTelefono(faker.phoneNumber().cellPhone());
                    gestoreSala.setLinkSocial(List.of(faker.internet().url(), faker.internet().url(), faker.internet().url()));
                    gestoreSala.setPartitaIva(faker.idNumber().ssnValid());
                    gestoreSala.setAppUser(appUser);
                    gestoreSala.setId(appUser.getId());
                    gestoreSalaRepository.save(gestoreSala);
                    for (int j = 0; j < 3; j++) {
                        try {
                            SalaProve salaProve = new SalaProve();
                            salaProve.setNomeSala(faker.funnyName().name() + faker.music().genre());
                            salaProve.setCapienzaMax(faker.number().numberBetween(5, 20));
                            salaProve.setDescrizione(faker.lorem().paragraph());
                            salaProve.setRegolamento(faker.lorem().paragraph());
                            salaProve.setIndirizzoSala(faker.address().streetAddress());
                            salaProve.setCitta(faker.address().city());
                            salaProve.setGestoreSala(gestoreSala);
                            salaProve.setPrezzoOrario(faker.number().numberBetween(10, 30));
                            salaProve.setCopertinaSala("https://m.media-amazon.com/images/S/pv-target-images/fb3e18571fefeb973850615b530a8c6440fc338e33b00438d52152f38f58fca8._SX1080_FMjpg_.jpg");
                            salaProve.setGiorniApertura(Set.of(DayOfWeek.MONDAY, DayOfWeek.TUESDAY, DayOfWeek.WEDNESDAY, DayOfWeek.THURSDAY, DayOfWeek.FRIDAY));
                            salaProve.setOrarioApertura(LocalTime.of(faker.number().numberBetween(9, 13), 0));
                            salaProve.setOrarioChiusura(LocalTime.of(faker.number().numberBetween(18, 23), 0));
                            Strumentazione strumentazione = new Strumentazione();
                            strumentazione.setAmpliETestate(List.of("Marshall", "Mesa Boogie", "Randall"));
                            strumentazione.setMicrofoni(List.of("Shure", "Rode", "AKG"));
                            strumentazione.setMixer(List.of("Shure", "Rode", "AKG"));
                            strumentazione.setSetBatteria(List.of("Evans", "Zildjian", "Yamaha"));
                            strumentazione.setAltro(List.of("Tastiera Korg"));
                            salaProve.setStrumentazione(strumentazione);
                            salaProveRepository.save(salaProve);
                        } catch (Exception e) {
                            log.error(e.getMessage());
                        }
                    }
                } catch (Exception e) {
                    log.error(e.getMessage());
                }
            }
        }

    }
}
