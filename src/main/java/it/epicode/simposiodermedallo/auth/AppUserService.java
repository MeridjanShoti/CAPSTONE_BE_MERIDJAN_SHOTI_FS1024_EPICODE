package it.epicode.simposiodermedallo.auth;

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
import it.epicode.simposiodermedallo.utenti.servizi.scuole.ScuolaService;
import jakarta.persistence.EntityExistsException;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cglib.core.Local;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Service
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
    public UtenteNormale registerUtenteNormale(UtenteNormaleRequest request) {
        AppUser appUser = registerUser(request.getUsername(), request.getPassword(), Set.of(Role.ROLE_USER));
        UtenteNormale utenteNormale = new UtenteNormale();

        utenteNormale.setNome(request.getNome());
        utenteNormale.setCognome(request.getCognome());
        utenteNormale.setEmail(request.getEmail());
        utenteNormale.setDataNascita(request.getDataNascita());
        utenteNormale.setBio(request.getBio());
        if(request.getAvatar() == null){
            utenteNormale.setAvatar("https://ui-avatars.com/api/?name=" + utenteNormale.getNome() + "+" + utenteNormale.getCognome());
        } else {
            utenteNormale.setAvatar(request.getAvatar());
        }
        utenteNormale.setCopertina(request.getCopertina());
        utenteNormale.setDataRegistrazione(LocalDate.now());
        utenteNormale.setAppUser(appUser);
        return utenteNormaleRepository.save(utenteNormale);
    }
    public Optional<AppUser> findByUsername(String username) {
        return appUserRepository.findByUsername(username);
    }
    public Scuola registerScuola(ServizioRequest request) {
        AppUser appUser = registerUser(request.getUsername(), request.getPassword(), Set.of(Role.ROLE_SCUOLA));
        Scuola scuola = new Scuola();


        scuola.setEmail(request.getEmail());

        scuola.setBio(request.getBio());


        if (request.getRagioneSociale() != null) {
            scuola.setRagioneSociale(request.getRagioneSociale());
        }
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
        return scuolaRepository.save(scuola);
    }

    public Insegnante  registerInsegnante(InsegnanteRequest request) {
        AppUser appUser = registerUser(request.getUsername(), request.getPassword(), Set.of(Role.ROLE_INSEGNANTE));
        Insegnante insegnante = new Insegnante();

        insegnante.setNome(request.getNome());
        insegnante.setCognome(request.getCognome());
        insegnante.setEmail(request.getEmail());
        insegnante.setDataNascita(request.getDataNascita());
        insegnante.setBio(request.getBio());
        if(request.getAvatar() == null){
            insegnante.setAvatar("https://ui-avatars.com/api/?name=" + insegnante.getNome() + "+" + insegnante.getCognome());
        } else {
            insegnante.setAvatar(request.getAvatar());
        }
        insegnante.setCopertina(request.getCopertina());
        insegnante.setDataRegistrazione(LocalDate.now());


        insegnante.setAppUser(appUser);
        return insegnanteRepository.save(insegnante);
    }

    public GestoreSala registerGestoreSala(ServizioRequest request) {
        AppUser appUser = registerUser(request.getUsername(), request.getPassword(), Set.of(Role.ROLE_GESTORE_SP));
        GestoreSala gestoreSala = new GestoreSala();


        gestoreSala.setEmail(request.getEmail());

        gestoreSala.setBio(request.getBio());


        if (request.getRagioneSociale() != null) {
            gestoreSala.setRagioneSociale(request.getRagioneSociale());
        }
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
        return gestoreSalaRepository.save(gestoreSala);
    }

    public OrganizzatoreEventi registerOrganizzatoreEventi(ServizioRequest request) {
        AppUser appUser = registerUser(request.getUsername(), request.getPassword(), Set.of(Role.ROLE_ORGANIZZATORE));
        OrganizzatoreEventi organizzatoreEventi = new OrganizzatoreEventi();


        organizzatoreEventi.setEmail(request.getEmail());

        organizzatoreEventi.setBio(request.getBio());


        if (request.getRagioneSociale() != null) {
            organizzatoreEventi.setRagioneSociale(request.getRagioneSociale());
        }
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
        return organizzatoreEventiRepository.save(organizzatoreEventi);
    }

    public String authenticateUser(String username, String password)  {
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


    public AppUser loadUserByUsername(String username)  {
        AppUser appUser = appUserRepository.findByUsername(username)
            .orElseThrow(() -> new EntityNotFoundException("Utente non trovato con username: " + username));


        return appUser;
    }
}
