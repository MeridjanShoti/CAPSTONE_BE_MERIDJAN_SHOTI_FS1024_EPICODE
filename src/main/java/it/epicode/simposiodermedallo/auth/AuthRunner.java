package it.epicode.simposiodermedallo.auth;

import com.github.javafaker.Faker;
import it.epicode.simposiodermedallo.utenti.Utente;
import it.epicode.simposiodermedallo.utenti.persone.utentinormali.UtenteNormale;
import it.epicode.simposiodermedallo.utenti.persone.utentinormali.UtenteNormaleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
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
    Faker faker;


    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public void run(ApplicationArguments args) throws Exception {
        if (!appUserRepository.existsByRolesContaining(Role.ROLE_ADMIN)) {
            appUserService.registerUser("admin", "adminpwd", Set.of(Role.ROLE_ADMIN));
        }


        if (!appUserRepository.existsByRolesContaining(Role.ROLE_USER)) {
            for(int i = 0; i < 10; i++) {
                UtenteNormale utente = new UtenteNormale();
                String nome = faker.name().firstName();
                String cognome = faker.name().lastName();
                String email = (nome + cognome + "@gmail.com").toLowerCase(Locale.ROOT).replaceAll("[^a-z0-9]", "");
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


    }
}
