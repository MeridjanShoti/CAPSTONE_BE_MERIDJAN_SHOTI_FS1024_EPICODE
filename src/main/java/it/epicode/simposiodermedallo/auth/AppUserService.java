package it.epicode.simposiodermedallo.auth;

import it.epicode.simposiodermedallo.utenti.persone.utentinormali.UtenteNormale;
import it.epicode.simposiodermedallo.utenti.persone.utentinormali.UtenteNormaleRepository;
import it.epicode.simposiodermedallo.utenti.persone.utentinormali.UtenteNormaleRequest;
import it.epicode.simposiodermedallo.utenti.servizi.ServizioRequest;
import it.epicode.simposiodermedallo.utenti.servizi.scuole.Scuola;
import it.epicode.simposiodermedallo.utenti.servizi.scuole.ScuolaRepository;
import it.epicode.simposiodermedallo.utenti.servizi.scuole.ScuolaService;
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
    private ScuolaRepository scuolaRepository;

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
        utenteNormale.setId(appUser.getId());
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
        utenteNormale.setAppUser(appUser);
        return utenteNormaleRepository.save(utenteNormale);
    }
    public Optional<AppUser> findByUsername(String username) {
        return appUserRepository.findByUsername(username);
    }
    public Scuola registerScuola(ServizioRequest request) {
        AppUser appUser = registerUser(request.getUsername(), request.getPassword(), Set.of(Role.ROLE_SCUOLA));
        Scuola scuola = new Scuola();
        scuola.setId(appUser.getId());

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
        scuola.setAppUser(appUser);
        return scuolaRepository.save(scuola);
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
