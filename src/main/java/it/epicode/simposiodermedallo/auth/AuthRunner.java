package it.epicode.simposiodermedallo.auth;

import com.github.javafaker.Faker;
import it.epicode.simposiodermedallo.utenti.Utente;
import it.epicode.simposiodermedallo.utenti.persone.insegnanti.Insegnante;
import it.epicode.simposiodermedallo.utenti.persone.insegnanti.InsegnanteRepository;
import it.epicode.simposiodermedallo.utenti.persone.utentinormali.UtenteNormale;
import it.epicode.simposiodermedallo.utenti.persone.utentinormali.UtenteNormaleRepository;
import it.epicode.simposiodermedallo.utenti.servizi.gestorisaleprove.GestoreSala;
import it.epicode.simposiodermedallo.utenti.servizi.gestorisaleprove.GestoreSalaRepository;
import it.epicode.simposiodermedallo.utenti.servizi.organizzatoreeventi.OrganizzatoreEventi;
import it.epicode.simposiodermedallo.utenti.servizi.organizzatoreeventi.OrganizzatoreEventiRepository;
import it.epicode.simposiodermedallo.utenti.servizi.scuole.Scuola;
import it.epicode.simposiodermedallo.utenti.servizi.scuole.ScuolaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDate;
import java.util.List;
import java.util.Locale;
import java.util.Optional;
import java.util.Set;

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
    Faker faker;


    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public void run(ApplicationArguments args) throws Exception {
        if (!appUserRepository.existsByRolesContaining(Role.ROLE_ADMIN)) {
            appUserService.registerUser("admin", "adminpwd", Set.of(Role.ROLE_ADMIN));
        }


        if (!appUserRepository.existsByRolesContaining(Role.ROLE_USER)) {
            for (int i = 0; i < 10; i++) {
                UtenteNormale utente = new UtenteNormale();
                String nome = faker.name().firstName();
                String cognome = faker.name().lastName();
                String email = (nome + cognome + "@email.com").toLowerCase(Locale.ROOT).replaceAll("[^a-z0-9_@.]", "");
                LocalDate dataNascita = LocalDate.of(faker.number().numberBetween(1950, 2000), faker.number().numberBetween(1, 12), faker.number().numberBetween(1, 28));
                String username = (nome + "." + cognome).toLowerCase(Locale.ROOT).replaceAll("[^a-z0-9]", "");
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

            }
        }
        if (!appUserRepository.existsByRolesContaining(Role.ROLE_SCUOLA)) {
            for (int i = 0; i < 10; i++) {
                Scuola scuola = new Scuola();
                AppUser appUser = new AppUser();
                String nomeScuola = faker.funnyName().name();
                String email = (nomeScuola + "@email.com").toLowerCase(Locale.ROOT).replaceAll("[^a-z0-9_@.]", "");
                String username = (nomeScuola).toLowerCase(Locale.ROOT).replaceAll("[^a-z0-9]", "");
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
                        Insegnante insegnante = new Insegnante();
                        String nome = faker.name().firstName();
                        String cognome = faker.name().lastName();
                        String emailInsegnante = (nome + cognome + "@email.com").toLowerCase(Locale.ROOT).replaceAll("[^a-z0-9_@.]", "");
                        LocalDate dataNascita = LocalDate.of(faker.number().numberBetween(1950, 2000), faker.number().numberBetween(1, 12), faker.number().numberBetween(1, 28));
                        String usernameInsegnante = (nome + "." + cognome).toLowerCase(Locale.ROOT).replaceAll("[^a-z0-9]", "");
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
                    }

            }
        }
        if (!appUserRepository.existsByRolesContaining(Role.ROLE_ORGANIZZATORE)) {
            for (int i = 0; i < 10; i++) {
                OrganizzatoreEventi organizzatoreEventi = new OrganizzatoreEventi();
                AppUser appUser = new AppUser();
                String nomeOrganizzatore = faker.funnyName().name();
                String email = (nomeOrganizzatore + "@email.com").toLowerCase(Locale.ROOT).replaceAll("[^a-z0-9_@.]", "");
                String username = (nomeOrganizzatore).toLowerCase(Locale.ROOT).replaceAll("[^a-z0-9]", "");
                appUser.setUsername(username);
                appUser.setPassword(passwordEncoder.encode("organizzatorepwd"));
                appUser.setRoles(Set.of(Role.ROLE_ORGANIZZATORE));
                organizzatoreEventi.setEmail(email);
                organizzatoreEventi.setDataRegistrazione(LocalDate.now());
                organizzatoreEventi.setBio(faker.lorem().paragraph());
                organizzatoreEventi.setRagioneSociale(nomeOrganizzatore + "Eventi");
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
            }
        }
        if (!appUserRepository.existsByRolesContaining(Role.ROLE_GESTORE_SP)) {
            for (int i = 0; i < 10; i++) {
                GestoreSala gestoreSala = new GestoreSala();
                AppUser appUser = new AppUser();
                String nomeOrganizzatore = faker.funnyName().name();
                String email = (nomeOrganizzatore + "@email.com").toLowerCase(Locale.ROOT).replaceAll("[^a-z0-9_@.]", "");
                String username = (nomeOrganizzatore).toLowerCase(Locale.ROOT).replaceAll("[^a-z0-9]", "");
                appUser.setUsername(username);
                appUser.setPassword(passwordEncoder.encode("gestorepwd"));
                appUser.setRoles(Set.of(Role.ROLE_GESTORE_SP));
                gestoreSala.setEmail(email);
                gestoreSala.setDataRegistrazione(LocalDate.now());
                gestoreSala.setBio(faker.lorem().paragraph());
                gestoreSala.setRagioneSociale("Sala Prove" + nomeOrganizzatore);
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
            }
        }

    }
}
