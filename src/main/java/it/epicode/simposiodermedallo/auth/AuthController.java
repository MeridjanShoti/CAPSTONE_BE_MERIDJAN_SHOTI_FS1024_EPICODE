package it.epicode.simposiodermedallo.auth;

import it.epicode.simposiodermedallo.utenti.persone.utentinormali.UtenteNormaleRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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

    @PreAuthorize("isAuthenticated()")
    @GetMapping("/current-user")
    public AppUser getCurrentUser(@AuthenticationPrincipal AppUser appUser) {
        return appUser;
    }
    @PostMapping("/register")
    public ResponseEntity<String> register(@RequestBody UtenteNormaleRequest registerRequest) {
        appUserService.registerUser(
                registerRequest.getUsername(),
                registerRequest.getPassword(),
                Set.of(Role.ROLE_USER) // Assegna il ruolo di default
        );
        return ResponseEntity.ok("Registrazione utente avvenuta con successo");
    }
    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/register-admin")
    public ResponseEntity<String> registerAdmin(@RequestBody RegisterRequest registerRequest) {
        appUserService.registerUser(
                registerRequest.getUsername(),
                registerRequest.getPassword(),
                Set.of(Role.ROLE_ADMIN) // Assegna il ruolo di default
        );
        return ResponseEntity.ok("Registrazione admin avvenuta con successo");
    }
    @PostMapping("/register-scuola")
    public ResponseEntity<String> registerScuola(@RequestBody RegisterRequest registerRequest) {
        appUserService.registerUser(
                registerRequest.getUsername(),
                registerRequest.getPassword(),
                Set.of(Role.ROLE_SCUOLA) // Assegna il ruolo di default
        );
        return ResponseEntity.ok("Registrazione scuola avvenuta con successo");
    }
    @PreAuthorize("hasRole('ADMIN') or hasRole('SCUOLA')")
    @PostMapping("/register-insegnante")
    public ResponseEntity<String> registerInsegnante(@RequestBody RegisterRequest registerRequest) {
        appUserService.registerUser(
                registerRequest.getUsername(),
                registerRequest.getPassword(),
                Set.of(Role.ROLE_INSEGNANTE) // Assegna il ruolo di default
        );
        return ResponseEntity.ok("Registrazione insegnante avvenuta con successo");
    }
    @PostMapping("/register-gestore-sp")
    public ResponseEntity<String> registerGestoreSp(@RequestBody RegisterRequest registerRequest) {
        appUserService.registerUser(
                registerRequest.getUsername(),
                registerRequest.getPassword(),
                Set.of(Role.ROLE_GESTORE_SP) // Assegna il ruolo di default
        );
        return ResponseEntity.ok("Registrazione avvenuta con successo");
    }
    @PostMapping("/register-organizzatore")
    public ResponseEntity<String> registerGestore(@RequestBody RegisterRequest registerRequest) {
        appUserService.registerUser(
                registerRequest.getUsername(),
                registerRequest.getPassword(),
                Set.of(Role.ROLE_GESTORE_SP) // Assegna il ruolo di default
        );
        return ResponseEntity.ok("Registrazione avvenuta con successo");
    }
    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@RequestBody LoginRequest loginRequest) {
        log.info("Login request:");
        String token = appUserService.authenticateUser(
                loginRequest.getUsername(),
                loginRequest.getPassword()
        );
        return ResponseEntity.ok(new AuthResponse(token));
    }
}
