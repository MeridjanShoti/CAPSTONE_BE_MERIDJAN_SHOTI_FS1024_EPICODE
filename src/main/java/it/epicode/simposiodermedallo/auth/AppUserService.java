package it.epicode.simposiodermedallo.auth;

import it.epicode.simposiodermedallo.common.EmailSenderService;
import it.epicode.simposiodermedallo.utenti.UtenteRepository;
import it.epicode.simposiodermedallo.utenti.persone.insegnanti.Insegnante;
import it.epicode.simposiodermedallo.utenti.persone.insegnanti.InsegnanteRepository;
import it.epicode.simposiodermedallo.utenti.persone.insegnanti.InsegnanteRequest;
import it.epicode.simposiodermedallo.utenti.persone.utentinormali.UtenteNormale;
import it.epicode.simposiodermedallo.utenti.persone.utentinormali.UtenteNormaleRepository;
import it.epicode.simposiodermedallo.utenti.persone.utentinormali.UtenteNormaleRequest;
import it.epicode.simposiodermedallo.utenti.servizi.ServizioRequest;
import it.epicode.simposiodermedallo.utenti.servizi.gestorisaleprove.GestoreSala;
import it.epicode.simposiodermedallo.utenti.servizi.gestorisaleprove.GestoreSalaRepository;
import it.epicode.simposiodermedallo.utenti.servizi.organizzatoreeventi.OrganizzatoreEventi;
import it.epicode.simposiodermedallo.utenti.servizi.organizzatoreeventi.OrganizzatoreEventiRepository;
import it.epicode.simposiodermedallo.utenti.servizi.scuole.Scuola;
import it.epicode.simposiodermedallo.utenti.servizi.scuole.ScuolaRepository;
import jakarta.mail.MessagingException;
import jakarta.persistence.EntityExistsException;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDate;
import java.util.Optional;
import java.util.Set;

@Service
@Validated
public class AppUserService {

    @Autowired
    private AppUserRepository appUserRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JwtTokenUtil jwtTokenUtil;

    @Autowired
    private UtenteNormaleRepository utenteNormaleRepository;

    @Autowired
    private InsegnanteRepository insegnanteRepository;

    @Autowired
    private ScuolaRepository scuolaRepository;

    @Autowired
    private GestoreSalaRepository gestoreSalaRepository;

    @Autowired
    private OrganizzatoreEventiRepository organizzatoreEventiRepository;

    @Autowired
    private UtenteRepository utenteRepository;

    @Autowired
    private EmailSenderService emailSenderService;


    public AppUser registerUser(String username, String password, Set<Role> roles) {
        if (appUserRepository.existsByUsername(username)) {
            throw new EntityExistsException("Username già in uso");
        }
        AppUser appUser = new AppUser();
        appUser.setUsername(username);
        appUser.setPassword(passwordEncoder.encode(password));
        appUser.setRoles(roles);
        return appUserRepository.save(appUser);
    }

    public UtenteNormale registerUtenteNormale(UtenteNormaleRequest request) throws MessagingException {
        AppUser appUser = registerUser(request.getUsername(), request.getPassword(), Set.of(Role.ROLE_USER));
        UtenteNormale utenteNormale = new UtenteNormale();

        utenteNormale.setNome(request.getNome());
        utenteNormale.setCognome(request.getCognome());
        utenteNormale.setEmail(request.getEmail());
        utenteNormale.setDataNascita(request.getDataNascita());
        utenteNormale.setBio(request.getBio());
        if (request.getAvatar() == null) {
            utenteNormale.setAvatar("https://ui-avatars.com/api/?name=" + utenteNormale.getNome() + "+" + utenteNormale.getCognome());
        } else {
            utenteNormale.setAvatar(request.getAvatar());
        }
        utenteNormale.setCopertina(request.getCopertina());
        utenteNormale.setDataRegistrazione(LocalDate.now());
        utenteNormale.setAppUser(appUser);
        UtenteNormale utenteSalvato = utenteNormaleRepository.save(utenteNormale);
        emailSenderService.sendEmail(utenteNormale.getEmail(), "Registrazione Simposio Der Medallo", "Grazie per esserti registrato, ora puoi accedere al tuo account e iniziare a utilizzare i nostri servizi MICIDIALIIIII!!!!!");
        return utenteSalvato;
    }

    public UtenteNormale updateUtenteNormale(UtenteNormaleRequest request, AppUser user, long id) {
        if (!request.getUsername().equals(user.getUsername())) {
            if (appUserRepository.existsByUsername(request.getUsername())) {
                throw new EntityExistsException("Username già in uso");
            } else {
                user.setUsername(request.getUsername());
            }
        }
        if (user.getId() != id) {
            throw new EntityNotFoundException("Non puoi modificare un altro utente");
        }
        UtenteNormale utenteNormale = utenteNormaleRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("Utente non trovato"));
        utenteNormale.setNome(request.getNome());
        utenteNormale.setCognome(request.getCognome());
        utenteNormale.setEmail(request.getEmail());
        utenteNormale.setDataNascita(request.getDataNascita());
        utenteNormale.setBio(request.getBio());
        if (request.getAvatar() != null) {
            utenteNormale.setAvatar(request.getAvatar());
        }
        if (request.getCopertina() != null) {
            utenteNormale.setCopertina(request.getCopertina());
        }
        if (request.getPassword() != null && !request.getPassword().isBlank()) {
            user.setPassword(passwordEncoder.encode(request.getPassword()));
        }
        appUserRepository.save(user);
        UtenteNormale utenteSalvato = utenteNormaleRepository.save(utenteNormale);
        return utenteSalvato;
    }

    public Optional<AppUser> findByUsername(String username) {
        return appUserRepository.findByUsername(username);
    }

    public Scuola registerScuola(ServizioRequest request) throws MessagingException {
        AppUser appUser = registerUser(request.getUsername(), request.getPassword(), Set.of(Role.ROLE_SCUOLA));
        Scuola scuola = new Scuola();
        scuola.setEmail(request.getEmail());
        scuola.setBio(request.getBio());
        scuola.setRagioneSociale(request.getRagioneSociale());
        if (request.getAvatar() == null) {
            scuola.setAvatar("https://ui-avatars.com/api/?name=" + request.getRagioneSociale());
        } else {
            scuola.setAvatar(request.getAvatar());
        }
        scuola.setCopertina(request.getCopertina());
        scuola.setIndirizzoPrincipale(request.getIndirizzoPrincipale());
        scuola.setAltreSedi(request.getAltreSedi());
        scuola.setNumeroTelefono(request.getNumeroTelefono());
        scuola.setLinkSocial(request.getLinkSocial());
        scuola.setPartitaIva(request.getPartitaIva());
        scuola.setDataRegistrazione(LocalDate.now());
        scuola.setAppUser(appUser);
        Scuola scuolaSalvata = scuolaRepository.save(scuola);
        emailSenderService.sendEmail(scuola.getEmail(), "Registrazione Simposio Der Medallo", "Grazie per averci dato fiducia, ora la tua scuola è pronta per formare nuovi talenti di cui il sommo Maestro sarebbe fiero!");
        return scuolaSalvata;
    }

    public Scuola updateScuola(ServizioRequest request, long id, AppUser user) {
        if (!request.getUsername().equals(user.getUsername())) {
            if (appUserRepository.existsByUsername(request.getUsername())) {
                throw new EntityExistsException("Username già in uso");
            } else {
                user.setUsername(request.getUsername());
            }
        }
        if (user.getId() != id) {
            throw new EntityNotFoundException("Non puoi modificare un altro utente");
        }

        Scuola scuola = scuolaRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("Scuola non trovata"));
        scuola.setEmail(request.getEmail());
        scuola.setBio(request.getBio());
        scuola.setRagioneSociale(request.getRagioneSociale());

        if (request.getAvatar() != null) {
            scuola.setAvatar(request.getAvatar());
        }
        if (request.getCopertina() != null) {
            scuola.setCopertina(request.getCopertina());
        }
        if (request.getPassword() != null && !request.getPassword().isBlank()) {
            user.setPassword(passwordEncoder.encode(request.getPassword()));
        }
        scuola.setIndirizzoPrincipale(request.getIndirizzoPrincipale());
        scuola.setAltreSedi(request.getAltreSedi());
        scuola.setNumeroTelefono(request.getNumeroTelefono());
        scuola.setLinkSocial(request.getLinkSocial());
        scuola.setPartitaIva(request.getPartitaIva());

        Scuola scuolaSalvata = scuolaRepository.save(scuola);
        return scuolaSalvata;
    }

    public Insegnante registerInsegnante(InsegnanteRequest request, AppUser user) throws MessagingException {
        AppUser appUser = registerUser(request.getUsername(), request.getPassword(), Set.of(Role.ROLE_INSEGNANTE));
        Insegnante insegnante = new Insegnante();

        insegnante.setNome(request.getNome());
        insegnante.setCognome(request.getCognome());
        insegnante.setEmail(request.getEmail());
        insegnante.setDataNascita(request.getDataNascita());
        insegnante.setBio(request.getBio());
        if (request.getAvatar() == null) {
            insegnante.setAvatar("https://ui-avatars.com/api/?name=" + insegnante.getNome() + "+" + insegnante.getCognome());
        } else {
            insegnante.setAvatar(request.getAvatar());
        }
        insegnante.setCopertina(request.getCopertina());
        insegnante.setDataRegistrazione(LocalDate.now());
        insegnante.setStrumenti(request.getStrumenti());
        insegnante.setAppUser(appUser);
        insegnante.setPagaOraria(request.getPagaOraria());
        MultipartFile curriculumFile = request.getCurriculum();
        if (curriculumFile != null && !curriculumFile.isEmpty()) {
            try {
                insegnante.setCurriculum(curriculumFile.getBytes()); // ✅ salva nel DB
            } catch (IOException e) {
                throw new RuntimeException("Errore lettura file curriculum", e);
            }
        }
        Set<Role> roles = user.getRoles();
        Scuola scuola = null;
        if (roles.contains(Role.ROLE_SCUOLA)) {

            scuola = scuolaRepository.findById(user.getId()).orElseThrow(() -> new EntityNotFoundException("Scuola non trovata"));
        } else {
            scuola = null;
        }
        insegnante.setScuola(scuola);
        Insegnante insegnanteSalvato = insegnanteRepository.save(insegnante);
        emailSenderService.sendEmail(insegnante.getEmail(), "Registrazione Simposio Der Medallo", "Sei stato registrato dalla tua scuola o da un admin. \nOra ti spetta il compito più bello di tutti: \nfar appassionare i tuoi studenti al proprio strumento, farli divertire e crescere come musicisti!\n i tuoi dati di accesso sono: \nUsername: " + request.getUsername() + "\nPassword: " + request.getPassword());
        return insegnanteSalvato;
    }

    public Insegnante updateInsegnante(InsegnanteRequest request, AppUser user, long id) {
        if (!request.getUsername().equals(user.getUsername())) {
            if (appUserRepository.existsByUsername(request.getUsername())) {
                throw new EntityExistsException("Username già in uso");
            } else {
                user.setUsername(request.getUsername());
            }
        }
        Set<Role> roles = user.getRoles();
        if (user.getId() != id && !roles.contains(Role.ROLE_SCUOLA)) {
            throw new IllegalArgumentException("Non puoi modificare un altro utente");
        }
        Insegnante insegnante = insegnanteRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("Insegnante non trovato"));
        if (roles.contains(Role.ROLE_SCUOLA)) {
            if (!insegnante.getScuola().getId().equals(user.getId())) {
                throw new IllegalArgumentException("Non puoi modificare un insegnante di un'altra scuola");
            }
        }
        insegnante.setNome(request.getNome());
        insegnante.setCognome(request.getCognome());
        insegnante.setEmail(request.getEmail());
        insegnante.setDataNascita(request.getDataNascita());
        insegnante.setStrumenti(request.getStrumenti());
        insegnante.setBio(request.getBio());
        if (request.getAvatar() != null) {
            insegnante.setAvatar(request.getAvatar());
        }
        if (request.getCopertina() != null) {
            insegnante.setCopertina(request.getCopertina());
        }
        if (request.getPassword() != null && !request.getPassword().isBlank()) {
            user.setPassword(passwordEncoder.encode(request.getPassword()));
        }
        MultipartFile curriculumFile = request.getCurriculum();
        if (curriculumFile != null && !curriculumFile.isEmpty()) {
            try {
                insegnante.setCurriculum(curriculumFile.getBytes());
            } catch (IOException e) {
                throw new RuntimeException("Errore lettura file curriculum", e);
            }
        }
        appUserRepository.save(user);
        Insegnante insegnanteSalvato = insegnanteRepository.save(insegnante);
        return insegnanteSalvato;
    }

    public GestoreSala registerGestoreSala(ServizioRequest request) throws MessagingException {
        AppUser appUser = registerUser(request.getUsername(), request.getPassword(), Set.of(Role.ROLE_GESTORE_SP));
        GestoreSala gestoreSala = new GestoreSala();
        gestoreSala.setEmail(request.getEmail());
        gestoreSala.setBio(request.getBio());
        gestoreSala.setRagioneSociale(request.getRagioneSociale());
        if (request.getAvatar() == null) {
            gestoreSala.setAvatar("https://ui-avatars.com/api/?name=" + request.getRagioneSociale());
        } else {
            gestoreSala.setAvatar(request.getAvatar());
        }
        gestoreSala.setCopertina(request.getCopertina());
        gestoreSala.setIndirizzoPrincipale(request.getIndirizzoPrincipale());
        gestoreSala.setAltreSedi(request.getAltreSedi());
        gestoreSala.setNumeroTelefono(request.getNumeroTelefono());
        gestoreSala.setLinkSocial(request.getLinkSocial());
        gestoreSala.setPartitaIva(request.getPartitaIva());
        gestoreSala.setDataRegistrazione(LocalDate.now());
        gestoreSala.setAppUser(appUser);
        GestoreSala gestoreSalaSalvata = gestoreSalaRepository.save(gestoreSala);
        emailSenderService.sendEmail(gestoreSala.getEmail(), "Registrazione Simposio Der Medallo", "Grazie per esserti registrato. " +
                "\nOra puoi affittare le tue sale prove ai Vitalij Kuprij, Michael Harris, Randy Coven e John Macaluso del futuro! " +
                "\nCosa vuoi di più se non LA GUERRA... LA GUERRA PIÙ TOTALEEEEEEE!!!!!!");
        return gestoreSalaSalvata;
    }

    public GestoreSala updateGestoreSala(ServizioRequest request, long id, AppUser user) {
        if (!request.getUsername().equals(user.getUsername())) {
            if (appUserRepository.existsByUsername(request.getUsername())) {
                throw new EntityExistsException("Username già in uso");
            } else {
                user.setUsername(request.getUsername());
            }
        }
        if (user.getId() != id && !user.getRoles().contains(Role.ROLE_ADMIN)) {
            throw new IllegalArgumentException("Non puoi modificare un altro utente");
        }
        GestoreSala gestoreSala = gestoreSalaRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("GestoreSala non trovato"));
        gestoreSala.setEmail(request.getEmail());
        gestoreSala.setBio(request.getBio());
        gestoreSala.setRagioneSociale(request.getRagioneSociale());
        if (request.getAvatar() != null) {
            gestoreSala.setAvatar(request.getAvatar());
        }
        if (request.getCopertina() != null) {
            gestoreSala.setCopertina(request.getCopertina());
        }
        if (request.getPassword() != null && !request.getPassword().isBlank()) {
            user.setPassword(passwordEncoder.encode(request.getPassword()));
        }
        gestoreSala.setIndirizzoPrincipale(request.getIndirizzoPrincipale());
        gestoreSala.setAltreSedi(request.getAltreSedi());
        gestoreSala.setNumeroTelefono(request.getNumeroTelefono());
        gestoreSala.setLinkSocial(request.getLinkSocial());
        gestoreSala.setPartitaIva(request.getPartitaIva());
        GestoreSala gestoreSalaSalvata = gestoreSalaRepository.save(gestoreSala);
        return gestoreSalaSalvata;
    }

    public OrganizzatoreEventi registerOrganizzatoreEventi(ServizioRequest request) throws MessagingException {
        AppUser appUser = registerUser(request.getUsername(), request.getPassword(), Set.of(Role.ROLE_ORGANIZZATORE));
        OrganizzatoreEventi organizzatoreEventi = new OrganizzatoreEventi();
        organizzatoreEventi.setEmail(request.getEmail());
        organizzatoreEventi.setBio(request.getBio());
        organizzatoreEventi.setRagioneSociale(request.getRagioneSociale());
        if (request.getAvatar() == null) {
            organizzatoreEventi.setAvatar("https://ui-avatars.com/api/?name=" + request.getRagioneSociale());
        } else {
            organizzatoreEventi.setAvatar(request.getAvatar());
        }
        organizzatoreEventi.setCopertina(request.getCopertina());
        organizzatoreEventi.setIndirizzoPrincipale(request.getIndirizzoPrincipale());
        organizzatoreEventi.setAltreSedi(request.getAltreSedi());
        organizzatoreEventi.setNumeroTelefono(request.getNumeroTelefono());
        organizzatoreEventi.setLinkSocial(request.getLinkSocial());
        organizzatoreEventi.setPartitaIva(request.getPartitaIva());
        organizzatoreEventi.setDataRegistrazione(LocalDate.now());
        organizzatoreEventi.setAppUser(appUser);
        OrganizzatoreEventi organizzatoreEventiSalvato = organizzatoreEventiRepository.save(organizzatoreEventi);
        emailSenderService.sendEmail(organizzatoreEventi.getEmail(), "Registrazione Simposio Der Medallo", "Grazie per esserti registrato. " +
                "\nOra puoi organizzare eventi in cui si spera non vengano lanciati polli! ");
        return organizzatoreEventiSalvato;
    }

    public OrganizzatoreEventi updateOrganizzatoreEventi(long id, ServizioRequest request, AppUser user) {
        if (!request.getUsername().equals(user.getUsername())) {
            if (appUserRepository.existsByUsername(request.getUsername())) {
                throw new EntityExistsException("Username già in uso");
            } else {
                user.setUsername(request.getUsername());
            }
        }
        if (user.getId() != id && !user.getRoles().contains(Role.ROLE_ADMIN)) {
            throw new IllegalArgumentException("Non puoi modificare un altro utente");
        }
        OrganizzatoreEventi organizzatoreEventi = organizzatoreEventiRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("Organizzatore non trovato"));
        organizzatoreEventi.setEmail(request.getEmail());
        organizzatoreEventi.setBio(request.getBio());
        organizzatoreEventi.setRagioneSociale(request.getRagioneSociale());
        if (request.getAvatar() != null) {
            organizzatoreEventi.setAvatar(request.getAvatar());
        }
        if (request.getCopertina() != null) {
            organizzatoreEventi.setCopertina(request.getCopertina());
        }
        if (request.getPassword() != null && !request.getPassword().isBlank()) {
            user.setPassword(passwordEncoder.encode(request.getPassword()));
        }
        organizzatoreEventi.setIndirizzoPrincipale(request.getIndirizzoPrincipale());
        organizzatoreEventi.setAltreSedi(request.getAltreSedi());
        organizzatoreEventi.setNumeroTelefono(request.getNumeroTelefono());
        organizzatoreEventi.setLinkSocial(request.getLinkSocial());
        organizzatoreEventi.setPartitaIva(request.getPartitaIva());
        return organizzatoreEventiRepository.save(organizzatoreEventi);
    }

    public String authenticateUser(String username, String password) {
        try {
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(username, password)
            );
            UserDetails userDetails = (UserDetails) authentication.getPrincipal();
            return jwtTokenUtil.generateToken(userDetails);
        } catch (AuthenticationException e) {
            throw new SecurityException("Credenziali non valide", e);
        }
    }

    public AppUser loadUserByUsername(String username) {
        AppUser appUser = appUserRepository.findByUsername(username)
                .orElseThrow(() -> new EntityNotFoundException("Utente non trovato con username: " + username));
        return appUser;
    }
}
