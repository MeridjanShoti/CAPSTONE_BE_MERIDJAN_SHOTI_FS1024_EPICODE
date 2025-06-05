package it.epicode.simposiodermedallo.auth;

import it.epicode.simposiodermedallo.utenti.persone.insegnanti.InsegnanteRepository;
import it.epicode.simposiodermedallo.utenti.persone.insegnanti.InsegnanteRequest;
import it.epicode.simposiodermedallo.utenti.persone.utentinormali.UtenteNormaleRepository;
import it.epicode.simposiodermedallo.utenti.persone.utentinormali.UtenteNormaleRequest;
import it.epicode.simposiodermedallo.utenti.servizi.ServizioRequest;
import it.epicode.simposiodermedallo.utenti.servizi.gestorisaleprove.GestoreSalaRepository;
import it.epicode.simposiodermedallo.utenti.servizi.organizzatoreeventi.OrganizzatoreEventiRepository;
import it.epicode.simposiodermedallo.utenti.servizi.scuole.ScuolaRepository;
import jakarta.mail.MessagingException;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.Set;

@RestController
@RequestMapping("")
@RequiredArgsConstructor
@Slf4j
public class AuthController {
    private final AppUserService appUserService;
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
    private AppUserRepository appUserRepository;

    @PreAuthorize("isAuthenticated()")
    @GetMapping("/current-user")
    @ResponseStatus(HttpStatus.OK)
    public AppUser getCurrentUser(@AuthenticationPrincipal AppUser appUser) {
        return appUser;
    }

    @GetMapping("/current-user-complete")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<?> getCurrentUserComplete(@AuthenticationPrincipal AppUser user) {
        Long id = user.getId();
        if (utenteNormaleRepository.existsById(id)) {
            return utenteNormaleRepository.findById(id)
                    .map(ResponseEntity::ok)
                    .orElseThrow();
        }
        if (insegnanteRepository.existsById(id)) {
            return insegnanteRepository.findById(id)
                    .map(ResponseEntity::ok)
                    .orElseThrow();
        }
        if (scuolaRepository.existsById(id)) {
            return scuolaRepository.findById(id)
                    .map(ResponseEntity::ok)
                    .orElseThrow();
        }
        if (gestoreSalaRepository.existsById(id)) {
            return gestoreSalaRepository.findById(id)
                    .map(ResponseEntity::ok)
                    .orElseThrow();
        }
        if (organizzatoreEventiRepository.existsById(id)) {
            return organizzatoreEventiRepository.findById(id)
                    .map(ResponseEntity::ok)
                    .orElseThrow();
        }
        if (appUserRepository.findByIdAndRolesContaining(id, Role.ROLE_ADMIN).isPresent()) {
            return appUserRepository.findById(id)
                    .map(ResponseEntity::ok)
                    .orElseThrow();
        }

        return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Utente non trovato");
    }

    @PostMapping("/register")

    public ResponseEntity<String> register(@Valid @RequestBody UtenteNormaleRequest registerRequest) throws MessagingException {
        appUserService.registerUtenteNormale(registerRequest);
        return ResponseEntity.ok("Registrazione utente avvenuta con successo");
    }

    @PutMapping("/utenti/{id}")
    @PreAuthorize("isAuthenticated()")

    public ResponseEntity<String> update(@RequestBody UtenteNormaleRequest registerRequest, @AuthenticationPrincipal AppUser user, @PathVariable Long id) {
        appUserService.updateUtenteNormale(registerRequest, user, id);
        return ResponseEntity.ok("Modifica utente avvenuta con successo");
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/register-admin")

    public ResponseEntity<String> registerAdmin(@Valid @RequestBody RegisterRequest registerRequest) {
        appUserService.registerUser(
                registerRequest.getUsername(),
                registerRequest.getPassword(),
                Set.of(Role.ROLE_ADMIN) // Assegna il ruolo di default
        );
        return ResponseEntity.ok("Registrazione admin avvenuta con successo");
    }

    @PostMapping("/register-scuola")
    public ResponseEntity<String> registerScuola(@Valid @RequestBody ServizioRequest registerRequest) throws MessagingException {
        appUserService.registerScuola(registerRequest);
        return ResponseEntity.ok("Registrazione scuola avvenuta con successo");
    }
    @PreAuthorize("isAuthenticated()")
    @PutMapping("/scuole/{id}")
    public ResponseEntity<String> updateScuola(@RequestBody ServizioRequest registerRequest, @AuthenticationPrincipal AppUser user, @PathVariable Long id) {
        appUserService.updateScuola(registerRequest, id, user);
        return ResponseEntity.ok("Registrazione scuola avvenuta con successo");
    }

    @PreAuthorize("hasRole('ADMIN') or hasRole('SCUOLA')")
    @PostMapping(value = "/register-insegnante", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)

    public ResponseEntity<String> registerInsegnante(@Valid@ModelAttribute InsegnanteRequest registerRequest, @AuthenticationPrincipal AppUser user) throws MessagingException {
        appUserService.registerInsegnante(registerRequest, user);
        return ResponseEntity.ok("Registrazione insegnante avvenuta con successo");
    }

    @PutMapping(value = "/insegnanti/{id}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)

    public ResponseEntity<String> updateInsegnante(@ModelAttribute InsegnanteRequest registerRequest, @AuthenticationPrincipal AppUser user, @PathVariable Long id) {
        appUserService.updateInsegnante(registerRequest, user, id);
        return ResponseEntity.ok("Modifica insegnante avvenuta con successo");
    }

    @PostMapping("/register-gestore-sp")

    public ResponseEntity<String> registerGestoreSp(@Valid @RequestBody ServizioRequest registerRequest) throws MessagingException {
        appUserService.registerGestoreSala(registerRequest);
        return ResponseEntity.ok("Registrazione avvenuta con successo");
    }

    @PutMapping("/gestori/{id}")
    @PreAuthorize("isAuthenticated()")

    public ResponseEntity<String> updateGestoreSp(@RequestBody ServizioRequest registerRequest, @AuthenticationPrincipal AppUser user, @PathVariable Long id) {
        appUserService.updateGestoreSala(registerRequest, id, user);
        return ResponseEntity.ok("Modifica gestore avvenutacon successo");
    }

    @PostMapping("/register-organizzatore")

    public ResponseEntity<String> registerOrganizzatore(@Valid @RequestBody ServizioRequest registerRequest) throws MessagingException {
        appUserService.registerOrganizzatoreEventi(registerRequest);
        return ResponseEntity.ok("Registrazione avvenuta con successo");
    }

    @PutMapping("/organizzatori/{id}")

    public ResponseEntity<String> registerOrganizzatore(@PathVariable Long id, @RequestBody ServizioRequest registerRequest, @AuthenticationPrincipal AppUser user) {
        appUserService.updateOrganizzatoreEventi(id, registerRequest, user);
        return ResponseEntity.ok("Modifica organizzatore avvenuta con successo");
    }

    @PostMapping("/login")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<AuthResponse> login(@Valid @RequestBody LoginRequest loginRequest) {
        log.info("Login request:");
        String token = appUserService.authenticateUser(
                loginRequest.getUsername(),
                loginRequest.getPassword()
        );
        return ResponseEntity.ok(new AuthResponse(token));
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/utenti")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<?> getAllUtenti() {
        return ResponseEntity.ok(utenteNormaleRepository.findAll());
    }
}
